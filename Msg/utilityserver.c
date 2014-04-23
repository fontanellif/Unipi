/** \file utilityserver.c
 \author Filippo Fontanelli mat 422385
 Si dichiara che il contenuto di questo file e' in ogni sua parte opera
 originale dell' autore.  */
#include "utilityserver.h"
/*#define DEBUG*/

int if_null_char(char * e, char* mess) {
	if (e == NULL) {
		fprintf(stderr, "%s\n", mess);
		return 1;
	}
	return 0;
}

char *logtoqueue(char *client, char* dest, char* message) {

	int n = (strlen(client) + strlen(dest) + strlen(message) + 3);
	char *out = calloc(sizeof(char), n);

	if (out == NULL) {
		fprintf(stderr, "ERROR : msgserv : logtoqueue, calloc return NULL.\n");
		return NULL;
	}
	out[0] = '\0';
	strcat(out, client);
	strcat(out, ":");
	strcat(out, dest);
	strcat(out, ":");
	strcat(out, message);

	return out;
}

int s_riempiMessage(message_t *msg, char *testo, char type) {

#ifdef DEBUG
	printf("OK : comsock : riempiMessage(message_t *msg, %s, %c) attiva\n",
			testo, type);
#endif
	if (msg == NULL)
		return 1;
	switch (type) {
	case MSG_NO:
	case MSG_EXIT:
	case MSG_OK: {
		msg->type = type;
		msg->length = 0;
		msg->buffer = NULL;
		break;
	}
	case MSG_ERROR:
	case MSG_CONNECT: {
		msg->type = type;
		msg->length = strlen(testo) + 1;
		msg->buffer = calloc(sizeof(char), msg->length);
		strcpy(msg->buffer, testo);

		break;
	}
	case MSG_TO_ONE:
	case MSG_LIST: {
		msg->type = type;
		msg->length = strlen(testo)+1;
		msg->buffer = calloc(sizeof(char), msg->length+1);
		memcpy(msg->buffer, testo,strlen(testo));
		break;
	}
	case MSG_BCAST: {
		msg->type = type;

		msg->length = strlen(testo) + 1;
		msg->buffer = calloc(sizeof(char), msg->length);
		strcpy(msg->buffer, testo);
		break;
	}
	default: {
		printf("ERROR : comsock : riempiMessage, tipo : %c inaspettato.\n",
				type);
		return 1;
		break;
	}
	}
#ifdef DEBUG
	printf(
			"OK : comsock : riempiMessage(message_t *msg, %s, char type) terminata\n",
			msg->buffer);
#endif
	return 0;

}

int s_control_send_message(message_t* msg, char* testo_mess, char type,
		int socket) {
	if (msg != NULL)
		s_riempiMessage(msg, testo_mess, type);
	else
		return 1;

	switch (sendMessage(socket, msg)) {
	case SEOF: {
		fprintf(stderr, NOSERV);
		return 1;
	}
	case -1: {
		fprintf(stderr, ERRO);
		return 1;
	}
	}
	return 0;
}

int s_control_recive_message(message_t* msg, int socket) {

	switch (receiveMessage(socket, msg)) {
	case SEOF: {
		fprintf(stderr, NOSERV);
		return 1;
	}
	case -1: {
		fprintf(stderr, ERRO);
		return 1;
	}
	}
	return 0;
}
