/** \file utilityserver.h
 \author Filippo Fontanelli mat 422385
 Si dichiara che il contenuto di questo file e' in ogni sua parte opera
 originale dell' autore.  */

#ifndef UTILITYSERVER_H_
#define UTILITYSERVER_H_
#include "comsock.h"
#include "genHash.h"

/** Testa se e != NULL, in caso contrario stampa un messaggio di errore sullo stderr
 *	\param	e		varibile da testare
 *	\param	mess	testo del messaggio stampato
 *
 *  \retval 0 	 se e != NULL
 *  \retval 1	altrimenti
 */
 int if_null_char(char * e, char* mess);

/** Prepara le stringhe per la memorizzazione nel filelog, allocando memoria
 *	\param	client	il nome del client mittente
 *	\param	dest	il nome del client destinatario
 *	\param	message	testo del messaggio
 *
 *  \retval s 	 la stringa formattata
 *  \retval NULL in caso di errore(errno non settata)
 */
char *logtoqueue(char *client, char* dest, char* message);

/** Formatta i messaggi riempiendoli correttamente in base al loro tipo, allocando memoria
 *	\param	type	il tipo di messsaggio
 *	\param	testo	il testo del messaggio
 *	\param	msg		messaggio da formattare
 *
 *  \retval 0 	 messaggio correttamente formattato
 *  \retval 1 in caso di errore
 */
int s_riempiMessage(message_t *msg, char *testo, char type);

/** Controlla l'invio di un messaggio al server
 *  \param	socket  la socket dove spedire il messaggio
 *	\param	type	il tipo di messsaggio
 *	\param	testo	il testo del messaggio
 *	\param	msg		messaggio spedire
 *	\param	testo_error testo del messaggio di errore(ifdef DEBUG)
 *
 *  \retval 0 	 messaggio correttamente spedito
 *  \retval 1 	 in caso di errore
 */
int s_control_send_message(message_t* msg, char* testo_mess, char type,
		int socket);

/** Controlla la ricezione di un messaggio dal server
 *  \param	socket  la socket su cui posizionarsi in ascolto
 *	\param	msg		messaggio spedire
 *	\param	testo_error testo del messaggio di errore(ifdef DEBUG)
 *
 *  \retval 0 	 messaggio correttamente spedito
 *  \retval 1 	 in caso di errore
 */
int s_control_recive_message(message_t* msg, int socket);


#endif /* UTILITYSERVER_H_ */
