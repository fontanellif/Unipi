/** \file utilityclient.c
 \author Filippo Fontanelli mat 422385
 Si dichiara che il contenuto di questo file e' in ogni sua parte opera
 originale dell' autore.  */



#include "utilityclient.h"
/*#define DEBUG*/

message_t *c_riempiMessage(message_t *msg, char *testo, char type) {
	int lusername, lbuffer = 0;

	if (msg == NULL)
		return NULL;

	switch (type) {
	case MSG_BCAST:
	case MSG_ERROR:
	case MSG_CONNECT: {
		msg->type = type;
		if (testo != NULL) {
			if (strlen(testo) != 0) {

				msg->length = strlen(testo) + 1;
				msg->buffer = calloc(sizeof(char), msg->length);
				strcpy(msg->buffer, testo);
				msg->buffer[msg->length - 1] = '\0';
			} else {
				msg->buffer = NULL;
			}
		} else {
			msg->buffer = NULL;
			msg->length = 0;
		}

		break;

	}
	case MSG_OK:
	case MSG_EXIT:
	case MSG_LIST: {
		msg->type = type;
		msg->buffer = NULL;
		msg->length = 0;
		break;
	}
	case MSG_TO_ONE: {
		msg->type = MSG_TO_ONE;
		lusername = strcspn(testo, " ");
		lbuffer = strlen(testo) - 3;
		testo[strlen(testo)] = '\0';
		msg->length = lbuffer;
		msg->buffer = calloc(sizeof(char), msg->length);
		memcpy(msg->buffer, testo + 4, lbuffer - 1);
		break;
	}
	default: {
		fprintf(stderr,
				"ERROR : comsock : riempiMessage, tipo : %c inaspettato.\n",
				type);
		return NULL;
		break;
	}
	}
#ifdef DEBUG
	printf(
			"OK : comsock : riempiMessage(message_t *msg, %s, char type) terminata\n",
			msg->buffer);
#endif
	return msg;

}

int c_control_send_message(message_t* msg, char* testo_mess, char type,
		int socket, char* testo_error) {
	if (msg != NULL)
		msg = c_riempiMessage(msg, testo_mess, type);
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
#ifdef DEBUG
	printf(
			"OK : COMSOCK : control_send_message : messaggio spedito correttamente, type : %c, testo %s.\n",
			msg->type, msg->buffer);
#endif
	return 0;
}

int c_control_recive_message(message_t* msg, int socket, char* testo_error) {

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
#ifdef DEBUG
	printf(
			"OK : COMSOCK : control_recive_message : messaggio ricevuto correttamente, type : %c, testo %s.\n",
			msg->type, msg->buffer);
#endif
	return 0;
}

