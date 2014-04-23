/** \file server.c
 \author Filippo Fontanelli mat 422385
 Si dichiara che il contenuto di questo file e' in ogni sua parte opera
 originale dell' autore.  */



#include "server.h"
/*#define DEBUG*/

/**START - Funzioni utilizzate per preparare le strutture dati necessarie al server*/
int preparing_data_structures(int argc, char *argv[], hashTable_t **hashaut,
		FILE **faut, FILE **flog, pthread_mutex_t mtx_autor) {
	if (argc != 3) {
		fprintf(stderr, "ERROR : msgserv : (argc != 3), argc = %d.\n", argc);
		return 1;
	}

	/*

	 * Apertura del file delle autorizzazioni, in caso di errore "return 1"

	 * In assenza di errori procedo con la creazione (controllata) della tabella Hash

	 * delle autorizzazioni

	 */
	errno = 0;
	if (openfile(faut, argv[1], "r"))
		return 1;

	if ((*hashaut = new_hashTable(SIZEH, compare_string_hash, copy_string_hash,
			copy_int_hash, hash_string)) == NULL) {
		fclose(*faut);
		return 1;
	}
	/*Scanzione del file e salvataggio dei nomi degli utenti autorizzati nella tabella Hash*/

	if (preparing_hash_aut(*faut, hashaut, mtx_autor))
		return 1;
	/*

	 * Apertura del filelog, in caso di errore return 1

	 */
	if (openfile(flog, argv[2], "w")) {
		fprintf(stderr, ERRLOGFILE);
		;
		pthread_mutex_lock(&mtx_autor);
		if (*hashaut != NULL)
			free_hashTable(&(*hashaut));
		free(*hashaut);
		return 1;
	}

	return 0;
}

int openfile(FILE **f, char *path, char* mode) {
	*f = fopen(path, mode);
	if (*f == NULL) {
		fprintf(stderr, ERRAUTFILE);
		free(*f);
		return 1;
	}
	return 0;
}

int preparing_hash_aut(FILE *faut, hashTable_t **hashaut,
		pthread_mutex_t mtx_autor) {
	char *username;
	username = calloc(sizeof(char), MAXLENGTHUSER);
	if (if_null_char(username,
			"ERROR : msgserv : username = calloc(sizeof(char), MAXLENGTHUSER).\n")
			== 1) {
		fclose(faut);
		pthread_mutex_lock(&mtx_autor);
		if (*hashaut != NULL)
			free_hashTable(&(*hashaut));
		free(*hashaut);
		pthread_mutex_unlock(&mtx_autor);
		return 1;
	}

	while (fscanf(faut, "%s\n", username) != EOF) {
		int *fd_skt = malloc(sizeof(int));
		*fd_skt = -1;/*Utente disconnesso*/
		if (check_username(username) == 1) {
			fclose(faut);
			free(fd_skt);
			free(username);
			pthread_mutex_lock(&mtx_autor);
			if (*hashaut != NULL)
				free_hashTable(&(*hashaut));
			free(*hashaut);
			fprintf(stderr, "ERROR : faut non valido\n");
			return 1;
		}
		if (add_hashElement(*hashaut, username, fd_skt) == -1) {
			fclose(faut);
			free(fd_skt);
			free(username);
			pthread_mutex_lock(&mtx_autor);
			if (*hashaut != NULL)
				free_hashTable(&(*hashaut));
			free(*hashaut);
			fprintf(
					stderr,
					"ERROR : msgserv : utente : %s , addhashelement non riuscita, free_all e free usernanme e fd_skt riuscite.\n",
					username);
			return 1;
		}
		free(fd_skt);
	}
	fclose(faut);
	free(username);
	return 0;
}

int check_username(char* username) {
	int n = strlen(username);
	int i = 1;
	while (i < n) {
		if ((isalpha(username[i]) == 0) && (isdigit(username[i]) == 0))
			return 1;
		i++;
	}
	return 0;

}

/**END - Funzioni utilizzate per preparare le strutture dati necessarie al server*/

/**START - Funzioni utilizzate la creazione della socket del server e i thread handler e writer*/
int creating_main_structures(int *sockets, hashTable_t **allworker,
		queue **coda, pthread_t **tid_writer, pthread_t **tid_worker,
		pthread_t **tid_handler, void *(*handler)(), void *(*writer)()) {
	int tmp;

	if (CreateServerChannel(sockets, "./tmp/msgsock"))
		return 1;

	/*Faccio partire in thread dell'handler*/
	if (PthreadCreate(tid_handler, handler, NULL)) {
		closeSocket(*sockets);
		return 1;
	}

	/*Eseguo il writer, il quale si pone in attesa di tutti gli eventuali messaggi di log dai vari worker*/
	*coda = malloc(sizeof(queue));
	tmp = initialize(*coda);
	if ((tmp == 1 || *coda == NULL)) {
		fprintf(stderr,
				"ERROR : msgserv : creazione della coda dei messaggi non riuscita.\n");

		free(*tid_handler);
		closeSocket(*sockets);
		return 1;
	}

	if (PthreadCreate(tid_writer, writer, NULL)) {
		closeSocket(*sockets);
		free(coda);
		free(*tid_handler);
		return 1;
	}
	/*Creo la tabella hash destinata a contenere tutti i worker attivi*/
	if ((*allworker = new_hashTable(SIZEH, compare_string_hash,
			copy_string_hash, copy_int_hash, hash_string)) == NULL) {
		fprintf(stderr, "ERROR : msgserv : allworker = new_hashTable.\n");
		/*start free*/
		closeSocket(*sockets);
		free(*coda);
		free(*tid_handler);
		free(*tid_writer);
		/*End free*/
		return 1;
	}
	return 0;
}

int CreateServerChannel(int *sockets, char* path) {
	/*Creo la soket AF_UNIX utilizzando i metodi di comsock.h , utilizzando come path : ./tmp/msgsock*/
	*sockets = createServerChannel(path);
	if (*sockets == SNAMETOOLONG) {
		fprintf(
				stderr,
				"ERROR : msgserv : Impossible create a ServerChannel, lenght(path) is true long.\n");
		return 1;
	}
	if (*sockets == -1) {
		fprintf(stderr,
				"ERROR : msgserv : Impossible create a ServerChannel, return -1.\n");

		return 1;
	}

	return 0;
}

int PthreadCreate(pthread_t **tid, void *(*fun)(), void*arg) {
	*tid = malloc(sizeof(pthread_t));
	if (*tid == NULL || (pthread_create(*tid, NULL, fun, arg) != 0)) {
		fprintf(stderr,
				"ERROR : msgserv : parenza del thread handler non riuscita.\n");
		free(*tid);
		return 1;
	}
	return 0;
}

/**END - Funzioni utilizzate la creazione della socket del server e i thread handler e writer*/

int AcceptConnection(int * socketc, int sockets, pthread_t **tid_worker,
		void *(*workers)(), int termina_sign) {
	*socketc = acceptConnection(sockets);
	if (*socketc == -1 && !termina_sign) {
		return 1;
	}
	if (!termina_sign) {
		/*Ricevuta richiesta di connessione, provvedo a creare il thread worker*/
		if (PthreadCreate(tid_worker, workers, (void*) *socketc)) {

			return 1;
		}
	}
	return 0;
}

int msg_connect(message_t * domanda, message_t **risposta, int socketw,
		int *termina, int *userduplicate, elem_t * client_aut, char** client,
		pthread_t *mytid, hashTable_t **allworker, hashTable_t *hashaut,
		pthread_mutex_t mtx_autor, pthread_mutex_t mtx_worker) {
	int *testconnection;
	/*Determino il nome utente.*/
	if (strlen(domanda->buffer) > MAXLENGTHUSER) {
		*termina = 1;
		return *termina;
	} else {
		if ((*client = calloc(sizeof(char), strcspn(domanda->buffer, "\0") + 2))
				== NULL)
			return *termina;;
		strcpy(*client, domanda->buffer);
		/*Devo controllare l'autorizzazione del client*/
		pthread_mutex_lock(&mtx_autor);
		if (hashaut == NULL || (client_aut = point_find_hashElement(hashaut,
				*client)) == NULL) {
			pthread_mutex_unlock(&mtx_autor);
			fprintf(
					stderr,
					"ERROR : WORKER : MSG_CONNECT : Cliente %s non autorizzato.\n",
					*client);

			if (!(*termina = s_control_send_message(*risposta,
					"Error : Autorizzazione Negata", MSG_ERROR, socketw))){
				return 1;
			}
		} else {
			pthread_mutex_unlock(&mtx_autor);
			/*Devo controllare che il client non sia gia' connesso*/
			pthread_mutex_lock(&mtx_autor);
			testconnection = (int*) (client_aut->payload);
			pthread_mutex_unlock(&mtx_autor);

			if (*testconnection >= 0) {
				/*Gestione del caso in cui il client sia gia' connesso*/
				fprintf(
						stderr,
						"ERROR : WORKER : MSG_CONNECT : Cliente %s gia' connesso.\n",
						*client);
				if (!(*termina = s_control_send_message(*risposta,
						"Error: Utente gia' connesso", MSG_ERROR, socketw))) {
					*userduplicate = 1;
					return 1;
				}
			} else {
				/*Utente non connesso e autorizzato*/
				pthread_mutex_lock(&mtx_worker);
				if (add_hashElement(*allworker, *client, mytid) == -1) {
					pthread_mutex_unlock(&mtx_worker);
					fprintf(stderr,
							"ERROR : WORKER : MSG_CONNECT : add_hashElement(allworker, client, mytid).\n");
					*termina = 1;
					return *termina;
				} else {
					pthread_mutex_unlock(&mtx_worker);
					pthread_mutex_lock(&mtx_autor);
					*(int*) (client_aut->payload) = socketw;
					pthread_mutex_unlock(&mtx_autor);

					if (!(*termina = s_control_send_message(*risposta, NULL,
							MSG_OK, socketw)))
						return 1;

				}
			}
		}
	}
	return 0;
}

int msg_list(message_t **risposta, int socketw, int *termina,
		hashTable_t *hashaut, pthread_mutex_t mtx_autor) {
	char * names, *tmp;
	names = calloc(sizeof(char), BUFSIZE);
	/*Scansiono la tabella hash delle autorizzazioni per determinare l'utenti connessi*/

	hash_to_string_connect(hashaut, mtx_autor, &names);

	if (strlen(names) > BUFSIZE) {
		fprintf(stderr,
				"ERROR : WORKER : MSG_LIST : strlen(names) > BUFSIZE.\n");
	} else
		names[strlen(names) - 1] = '\0';
	tmp = calloc(sizeof(char), strlen(names) + 1);
	memcpy(tmp, names, strlen(names));
	if ((*termina = s_control_send_message(*risposta, tmp, MSG_LIST, socketw)))
		return 1;
	free(names);
	free(tmp);
	return 0;
}

void hash_to_string_connect(hashTable_t *hashaut, pthread_mutex_t mtx_autor,
		char **names) {
	int i, *fd_skt;
	pthread_mutex_lock(&mtx_autor);
	for (i = 0; i < hashaut->size; i++) {
		elem_t *e;
		if (hashaut->table[i] != NULL) {
			e = hashaut->table[i]->head;
			while (e != NULL) {
				fd_skt = e->payload;
				if (*fd_skt > 0) {
					strcat(*names, (char*) e->key);
					strcat(*names, " ");
				}
				e = e->next;
			}
		}
	}
	pthread_mutex_unlock(&mtx_autor);
}

int msg_exit(message_t **risposta, int socketw, int *termina,
		elem_t * client_aut, char* client, hashTable_t *hashaut,
		pthread_mutex_t mtx_autor) {
	pthread_mutex_lock(&mtx_autor);
	if ((client_aut = point_find_hashElement(hashaut, client)) == NULL) {
		fprintf(stderr,
				"Error: Type = MSG_EXIT, client non presente nella hashaut\n");
		*termina = 1;
	} else {
		if ((*termina
				= s_control_send_message(*risposta, NULL, MSG_NO, socketw)))
			return 1;
		pthread_mutex_unlock(&mtx_autor);
		*((int*) (client_aut->payload)) = -1;
#ifdef DEBUG
		printf(
				"OK : WORKER : MSG_EXIT : Disconnessione del client %s avvenuta con successo, payload : %d\n",
				client, *((int*) (client_aut->payload)));
#endif
		*termina = 1;
	}
	return *termina;
}

int msg_to_one(message_t *domanda, message_t **risposta, int socketw,
		char* client, int *termina, hashTable_t *hashaut,
		pthread_mutex_t mtx_autor, queue *coda, pthread_mutex_t mtx_queue,
		pthread_cond_t cond_queue) {

	char *allmessage, *dest_to_one, *mess_to_one, *msglog;
	elem_t * client_dest;
	int socket_dest, lname;

	lname = strcspn((domanda->buffer) + 1, " ");

	dest_to_one = calloc(sizeof(char), MAXLENGTHUSER);
	if (dest_to_one == NULL) {
		fprintf(stderr,
				"ERROR : WORKER : MSG_TO_ONE : dest_to_one = calloc(sizeof(char), lname+1)\n");
		*termina = 1;
	} else {
		strncpy(dest_to_one, (domanda->buffer) + 1, lname);
		pthread_mutex_lock(&mtx_autor);

		if ((client_dest = point_find_hashElement(hashaut, dest_to_one))
				== NULL) {
			strcat(dest_to_one, ": utente non connesso");

			s_control_send_message(*risposta, dest_to_one, MSG_ERROR, socketw);

			pthread_mutex_unlock(&mtx_autor);
		} else {

			pthread_mutex_unlock(&mtx_autor);
			socket_dest = *(int*) (client_dest->payload);
			if (socket_dest < 0) {
				strcat(dest_to_one, ": utente non connesso");
				s_control_send_message(*risposta, dest_to_one, MSG_ERROR,
						socketw);

				pthread_mutex_unlock(&mtx_autor);
			} else {

				mess_to_one = calloc(sizeof(char), (domanda->length - lname
						- LENGTHEADER));
				if (mess_to_one == NULL) {
					*termina = 1;
				} else {
					memcpy(mess_to_one, ((domanda->buffer) + lname
							+ LENGTHEADER), (domanda->length - lname
							- LENGTHEADER));
					allmessage = calloc(sizeof(char), BUFSIZE);
					strcat(allmessage, "[");
					strcat(allmessage, client);
					strcat(allmessage, "] ");
					if (mess_to_one[0] == ' ')
						strcat(allmessage, (mess_to_one + 1));
					else
						strcat(allmessage, (mess_to_one));
					if (!(*termina = s_control_send_message(*risposta,
							allmessage, MSG_TO_ONE, socket_dest))) {
						msglog = logtoqueue(client, dest_to_one, (mess_to_one));
						enqueue(msglog, coda, &mtx_queue, &cond_queue);
					}
					free(allmessage);
				}

				if (mess_to_one != NULL)
					free(mess_to_one);
			}
		}
	}
	if (dest_to_one != NULL)
		free(dest_to_one);
	return 0;
}

