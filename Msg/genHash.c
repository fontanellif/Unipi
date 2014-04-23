/*
 * genHash.c
 *
 *  Created on: 19/mar/2010
 *      Author: Filippo Fontanelli
 *
 *  Si dichiara che il contenuto di questo file e' in ogni sua parte opera originale dell' autore.
 */

#include "genHash.h"
#include <stdlib.h>
#include <errno.h>
#include <stdio.h>
#include <string.h>
#include <math.h>


#define DEBUG



/** FUNZIONI DA IMPLEMENTARE */

/** crea una tabella hash allocata dinamicamente
 \param size ampiezza della tabella
 \param compare funzione usata per confrontare due chiavi all'interno della tabella
 \param copyk funzione per copiare una chiave
 \param copyp funzione per copiare un payload
 \param hashfunction funzione hash (chiave,size della tabella)

 \retval NULL in caso di errori con \c errno impostata opportunamente
 \retval p (p!=NULL) puntatore alla nuova tabella allocata
 */
hashTable_t * new_hashTable(unsigned int size, int(* compare)(void *, void *),
		void* (* copyk)(void *), void* (*copyp)(void*),
		unsigned int(*hashfunction)(void*, unsigned int)) {

	hashTable_t *h;

	if (!compare || !copyk || !copyp || size < 0) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n",
				"if (!compare || !copyk || !copyp || size < 0)", "GenHash->new_hashTable");
#endif
		errno = EINVAL;
		return NULL;
	}

	if (!(h = (hashTable_t *) malloc(sizeof(hashTable_t)))) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n",
				"if (!(h = (hashTable_t *) malloc(sizeof(hashTable_t))))", "GenHash->new_hashTable");
#endif
		errno = ENOMEM;
		return NULL;
	}

	if (!(h->table = (list_t **) calloc(size, sizeof(list_t*)))) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n",
				"if (!(	h->table = (list_t **) malloc(sizeof(list_t*) * size)))", "GenHash->new_hashTable");
#endif
		errno = ENOMEM;
		free(h);
		return NULL;
	}

	memset(h->table, 0, size * sizeof(list_t*));

	h->size = size;
	h->compare = compare;
	h->copyk = copyk;
	h->copyp = copyp;
	h->hash = hashfunction;

	return h;

}

/** funzione hash per key di tipo int
 \param key valore chiave
 \param size ampiezza della hash table

 \retval index posizione nella tabella
 */
unsigned int hash_int(void * key, unsigned int size) {

	return ((*(int *) key) % size);

}

/** funzione hash per key di tipo string
 \param key valore chiave
 \param size ampiezza della hash table

 \retval index posizione nella tabella
 */
unsigned int hash_string(void * key, unsigned int size) {
	unsigned int hashval;
	char *str = (char *) key;

	hashval = 0;

	for (; *str != '\0'; str++)
		hashval = *str + (hashval << 5) - hashval;

	return (unsigned int) hashval % size;

}

/** distrugge una tabella hash deallocando tutta la memoria occupata
 \param pt puntatore al puntatore della tabella da distruggere

 nota: mette a NULL il puntatore \c *pt
 */
void free_hashTable(hashTable_t ** pt) {
	list_t **app;
	int i;

	if ((*pt) == NULL) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n", "if ((*pt) == NULL)",
				"GenHash->free_hashTable");
#endif
		errno = EINVAL;
		return;
	}

	app = (*pt)->table;

	for (i = 0; i < (*pt)->size; i++) {
		free_List(&app[i]);
	}

	free((*pt)->table);
	free(*pt);

	(*pt) = NULL;

}

/** inserisce una nuova coppia (key, payload) nella hash table (se non c'e' gia')

 \param t la tabella cui aggiungere
 \param key la chiave
 \param payload l'informazione

 \retval -1 se si sono verificati errori (errno settato opportunamente)
 \retval 0 se l'inserimento \`e andato a buon fine

 SP ricordarsi di controllare se (in caso di errore) si lascia la situazione consistente o si fa casino nella lista ....
 */
int add_hashElement(hashTable_t * t, void * key, void* payload) {

	unsigned int hashval;

	if ((t == NULL || !key || !payload)) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n",
				"if ((t == NULL || !key || !payload))",
				"GenHash->add_hashElement");
#endif
		errno = EINVAL;
		return -1;
	}

	if (find_hashElement(t, key) != NULL) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n",
				"if (find_hashElement(t, key) != NULL)",
				"GenHash->add_hashElement");
#endif
		errno = EINVAL;
		return -1;
	}

	hashval = t->hash(key, t->size);
/*
 * Se è il primo elemento che inserisco in t->table[hashval], provvedo a creare la lista ad esso associata.
 */
	if (t->table[hashval] == NULL) {
		t->table[hashval] = new_List(t->compare, t->copyk, t->copyp);
	}

	if (add_ListElement(t->table[hashval], key, payload) == 1) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n",
				"if(add_ListElement(t->table[hashval],key,payload) == 1)",
				"GenHash->add_hashElement");
#endif
		errno = ENOMEM;
		return -1;
	}
	return 0;
}

/** cerca una chiave nella tabella e restituisce il payload per quella chiave
 \param t la tabella in cui aggiungere
 \param key la chiave da cercare

 \retval NULL in caso di errore (errno != 0)
 \retval p puntatore a una \b copia del payload (alloca memoria)
 */
void * find_hashElement(hashTable_t * t, void * key) {
	unsigned hashval;
	elem_t * p;
	void* payload;

	if ((t == NULL || !key)) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n",
				"if ((t == NULL || !key))",
				"GenHash->find_hashElement");
#endif
		errno = EINVAL;
		return NULL;
	}

	hashval = t->hash(key, t->size);

	if ((p = find_ListElement(t->table[hashval], key)) != NULL) {
		payload = t->table[hashval]->copyp(p->payload);
		return payload;
	} else {
		free(p);
		return NULL;
	}
}

/** elimina l'elemento di chiave (key) deallocando la memoria

 \param t puntatore alla lista
 \param key la chiave


 \retval -1 se si sono verificati errori (errno settato opportunamente)
 \retval 0 se l'esecuzione e' stata corretta

 */
int remove_hashElement(hashTable_t * t, void * key) {
	unsigned hashval;

	if ((t == NULL || !key)) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n",
				"if ((t == NULL || !key))",
				"GenHash->find_hashElement");
#endif
		errno = EINVAL;
		return -1;
	}

	hashval = t->hash(key, t->size);

	return remove_ListElement(t->table[hashval], key);

}

/** funzione di confronto per lista di interi 
 \param a puntatore intero da confrontare
 \param b puntatore intero da confrontare
 
 \retval 0 se sono uguali
 \retval p (p!=0) altrimenti
 */
int compare_int_hash(void *a, void *b) {
    int *_a, *_b;
    _a = (int *) a;
    _b = (int *) b;
    return ((*_a) - (*_b));
}

/** funzione di confronto per lista di stringhe 
 \param a puntatore prima stringa da confrontare
 \param b puntatore seconda stringa da confrontare
 
 \retval 0 se sono uguali
 \retval p (p!=0) altrimenti
 */
int compare_string_hash(void *a, void *b) {
    char *_a, *_b;
    _a = (char *) a;
    _b = (char *) b;
    return strcmp(_a,_b);
}


/** funzione di copia di un intero 
 \param a puntatore intero da copiare
 
 \retval NULL se si sono verificati errori
 \retval p puntatore al nuovo intero allocato (alloca memoria)
 */
void * copy_int_hash(void *a) {
	int * _a;
	
	if ( ( _a = malloc(sizeof(int) ) ) == NULL ) return NULL;
	
	*_a = * (int * ) a;
	
	return (void *) _a;
}

/** funzione di copia di una stringa 
 \param a puntatore stringa da copiare
 
 \retval NULL se si sono verificati errori
 \retval p puntatore alla stringa allocata (alloca memoria)
 */
void * copy_string_hash(void * a) {
	char * _a;
	
	if ( ( _a = strdup(( char * ) a ) ) == NULL ) return NULL;
	
	return (void *) _a;
}


void * copy_tid_hash(void *a){
	return a;
}

elem_t * point_find_hashElement( hashTable_t * t,void * key ){
	unsigned hashval;
	hashval = t->hash(key, t->size);
	return find_ListElement(t->table[hashval], key);
}


