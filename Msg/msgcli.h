/** \file msgcli.h       \author Filippo Fontanelli mat 422385     Si dichiara che il contenuto di questo file e' in ogni sua parte opera     originale dell' autore.  */#ifndef MSGCLI_H_#define MSGCLI_H_#include "comsock.h"#include "utilityclient.h"#include <pthread.h>#include <signal.h>#include <mcheck.h>/** Elimina le strutture dati allocate *	\param	valueexit valore di ritorno della funzione * */void free_all(int valueexit);/** handler_signal, funzione che si occupa della gestione dei segnali. * */void *handler_signal();/** Funzione che si occupa della ricezione dei messaggi dal server * */void *receiver();/** Funzione che si occupa della spedizone dei messaggi dal server * dopo averli letti dallo stdin * */void *sender();#endif /* MSGCLI_H_ */