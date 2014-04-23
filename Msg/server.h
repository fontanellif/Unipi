/** \file server.h
       \author Filippo Fontanelli mat 422385
     Si dichiara che il contenuto di questo file e' in ogni sua parte opera
     originale dell' autore.  */

#ifndef SERVER_H_
#define SERVER_H_
#include "genHash.h"
#include "queue.h"
#include "utilityserver.h"
#include <signal.h>
#include <pthread.h>
#include <ctype.h>

#define SIZEH 50
/** Controlla l'username 
 *	\param username stringa da controllare
 *
 *	\retval 0  se tutto ok,
 *	\retval 1  se errore
 */
int check_username(char* username);

/** Prepara le strutture dati iniziali del server 
 *
 *	\retval 0  se tutto ok,
 *	\retval 1  se errore
 */
int preparing_data_structures(int argc, char *argv[], hashTable_t **hashaut,
		FILE **faut, FILE **flog, pthread_mutex_t mtx_autor);

/** Apre un file
 *
 *	\retval 0  se tutto ok,
 *	\retval 1  se errore
 */
int openfile(FILE **f, char *path, char* mode);

/** Prepara la hash table delle autorizzazioni 
 *
 *	\retval 0  se tutto ok,
 *	\retval 1  se errore
 */
int preparing_hash_aut(FILE *faut, hashTable_t **hashaut,
		pthread_mutex_t mtx_autor);

/** Crea le strutture principale del server, ed i thread handler e writer
 *
 *	\retval 0  se tutto ok,
 *	\retval 1  se errore
 */
int creating_main_structures(int *sockets, hashTable_t **allworker,
		queue **coda, pthread_t **tid_writer, pthread_t **tid_worker,
		pthread_t **tid_handler, void*(*handler)(), void*(*writer)());

/** Crea un un thread 
 *
 *	\retval 0  se tutto ok,
 *	\retval 1  se errore
 */
int PthreadCreate(pthread_t **tid, void *(*fun)(), void*arg);

/** Crea un server Channel
 *
 *	\retval 0  se tutto ok,
 *	\retval 1  se errore
 */
int CreateServerChannel(int *sockets, char* path);

/** Accetta le connessioni 
 *
 *	\retval 0  se tutto ok,
 *	\retval 1  se errore
 */
int AcceptConnection(int * socketc, int sockets, pthread_t **tid_worker,
		void *(*workers)(), int termina_sign);

/** Modulo relativo al msg connect 
 *
 *	\retval 0  se tutto ok,
 *	\retval 1  se errore
 */
int msg_connect(message_t * domanda, message_t **risposta, int socketw,
		int *termina, int *userduplicate, elem_t * client_aut, char** client,
		pthread_t *mytid, hashTable_t **allworker, hashTable_t *hashaut,
		pthread_mutex_t mtx_autor, pthread_mutex_t mtx_worker);

/**Modulo relativo al msg list
 *
 *	\retval 0  se tutto ok,
 *	\retval 1  se errore
 */
int msg_list(message_t **risposta, int socketc, int *termina,
		hashTable_t *hashaut, pthread_mutex_t mtx_autor);

/** Determina tutti gli utenti connessi, modificando names, aggiungendo tutti gli username dei connessi
 *
 */
void hash_to_string_connect(hashTable_t *hashaut, pthread_mutex_t mtx_autor,
		char **names);
/** Modulo relativo al msg exit
 *
 *	\retval 0  se tutto ok,
 *	\retval 1  se errore
 */
int msg_exit(message_t **risposta, int socketw, int *termina,
		elem_t * client_aut, char* client, hashTable_t *hashaut,
		pthread_mutex_t mtx_autor);

/** Modulo relativo al msg to one
 *
 *	\retval 0  se tutto ok,
 *	\retval 1  se errore
 */
int msg_to_one(message_t *domanda, message_t **risposta, int socketw,
		char* client, int *termina, hashTable_t *hashaut,
		pthread_mutex_t mtx_autor, queue *coda, pthread_mutex_t mtx_queue,
		pthread_cond_t cond_queue);

#endif /* SERVER_H_ */
