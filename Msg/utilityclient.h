/** \file utilityclient.h
 \author Filippo Fontanelli mat 422385
 Si dichiara che il contenuto di questo file e' in ogni sua parte opera
 originale dell' autore.  */
#ifndef UTILITYCLIENT_H_
#define UTILITYCLIENT_H_
#include "comsock.h"

/** Formatta i messaggi riempiendoli correttamente in base al loro tipo, allocando memoria
 *	\param	type	il tipo di messsaggio
 *	\param	testo	il testo del messaggio
 *	\param	msg		messaggio da formattare
 *
 *  \retval 0 	 messaggio correttamente formattato
 *  \retval 1 	 in caso di errore
 */
message_t *c_riempiMessage(message_t *msg, char *testo, char type);

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
int c_control_send_message(message_t* msg, char* testo_mess, char type,
		int socket, char* testo_error);

/** Controlla la ricezione di un messaggio dal server
 *  \param	socket  la socket su cui posizionarsi in ascolto
 *	\param	msg		messaggio spedire
 *	\param	testo_error testo del messaggio di errore(ifdef DEBUG)
 *
 *  \retval 0 	 messaggio correttamente spedito
 *  \retval 1 	 in caso di errore
 */
int c_control_recive_message(message_t* msg, int socket, char* testo_error);

#endif /* UTILITYCLIENT_H_ */
