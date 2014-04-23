/*
 * genList.c
 *
 *  Created on: 18/mar/2010
 *      Author: Filippo Fontanelli
 *
 *  Si dichiara che il contenuto di questo file e' in ogni sua parte opera originale dell' autore.
 */

#include "genList.h"
#include <stdlib.h>
#include <errno.h>
#include <stdio.h>

/*
 *#define DEBUGREMOVE
 *#define DEBUG
 */


/** cerca una key nella lista
 \param t elemento corrente della lista
 \param key chiave da ricercare
 \param compare funzione usata per confrontare due chiavi

 \retval NULL se la chiave non è stata trovata o in caso di errori con \c errno impostata opportunamente
 \retval p  puntatore all'elemento con chiave = key
 */
elem_t * find_Element(elem_t * t, void * key, int(*compare)(void*, void*)) {
	if (t == NULL || !key || !compare) {
		errno = EINVAL;
		return NULL;
	}
	if (compare(key, t->key) == 0) {
#ifdef DEBUG
		printf("key : %s -- t->key :%s\n", (char *) key, (char *) t->key);
#endif
		return t;
	}
	return find_Element(t->next, key, compare);
}


/** crea una lista generica
 \param compare funzione usata per confrontare due chiavi
 \param copyk funzione usata per copiare una chiave
 \param copyp funzione usata per copiare un payload

 \retval NULL in caso di errori con \c errno impostata opportunamente
 \retval p (p!=NULL) puntatore alla nuova lista
 */
list_t * new_List(int(* compare)(void *, void *), void* (* copyk)(void *),
		void* (*copyp)(void*)) {

	list_t* app;

	if (!compare || !copyk || !copyp) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n",
				"if (!compare || !copyk || !copyp)", "GenList->NewList");
#endif
		errno = EINVAL;
		return NULL;
	}

	if (!(app = malloc(sizeof(list_t)))) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n",
				"if (!(app = malloc(sizeof(list_t))))", "GenList->NewList");
#endif
		errno = ENOMEM;
		return NULL;
	}

	app->compare = compare;
	app->copyk = copyk;
	app->copyp = copyp;
	app->head = NULL;

	return app;
}

/** distrugge una lista deallocando tutta la memoria occupata
 \param pt puntatore al puntatore della lista da distruggere

 nota: mette a NULL il puntatore \c *t
 */
void free_List(list_t ** pt) {
	elem_t* app;

	if ((*pt) == NULL) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n", "if ((*pt) == NULL)",
				"GenList->free_List");
#endif
		errno = EINVAL;
		return;
	}
	app = (*pt)->head;

	while (app != NULL) {
		(*pt)->head = (*pt)->head->next;
		if (app->key && app->payload) {
			free(app->key);
			free(app->payload);
		}
		free(app);
		app = (*pt)->head;
	}
	free(*pt);
	(*pt) = NULL;
}

/** inserisce una nuova coppia (key, payload) in testa alla lista,
 sia key che payload devono essere copiate nel nuovo elemento della lista.
 Nella lista \b non sono permesse chiavi replicate

 \param t puntatore alla lista
 \param key la chiave
 \param payload l'informazione

 \retval -1 se si sono verificati errori (errno settato opportunamente)
 \retval 0 se l'inserimento \`e andato a buon fine

 */

int add_ListElement(list_t * t, void * key, void* payload) {
	elem_t* news;

	if ((t == NULL || !key || !payload)) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n",
				"if ((t == NULL || !key || !payload))",
				"GenList->add_ListElement");
#endif
		errno = EINVAL;
		return -1;
	}
	if (find_ListElement(t, key) != NULL) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n",
				"if (find_ListElement(t, key) != NULL)",
				"GenList->add_ListElement");
#endif
		errno = EINVAL;
		return -1;
	}
	if (!(news = malloc(sizeof(elem_t)))) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n",
				"if (!(news = malloc(sizeof(elem_t))))", "GenList->add_ListElement");
#endif
		errno = ENOMEM;
		return -1;
	}
	if (!(news->key = t->copyk(key)) || !(news->payload = t->copyp(payload))) {
#ifdef DEBUG
		printf(
				"Error Tipe: %s Where: %s\n",
				"if ((news->key = t->copyk(key) == NULL) || (news->payload = t->copyp(payload) == NULL))",
				"GenList->add_ListElement");
#endif
		errno = EINVAL;
		free(news);
		return -1;
	}
	news->next = t->head;

	t->head = news;

	return 0;
}

/** elimina l'elemento di chiave (key) deallocando la memoria

 \param t puntatore alla lista
 \param key la chiave


 \retval -1 se si sono verificati errori (errno settato opportunamente)
 \retval 0 se l'esecuzione e' stata corretta

 */
int remove_ListElement(list_t * t, void * key) {

	elem_t* head_one, *previous;

	if (t == NULL || !key) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n", "if (t == NULL || !key)",
				"GenList->add_ListElement");
#endif
		errno = EINVAL;
		return -1;
	}

	head_one = t->head;
	previous = NULL;

	for (; head_one != NULL && (t->compare(head_one->key, key) != 0); head_one
			= head_one->next)
		previous = head_one;

	if (!head_one)
		return -1;

	if (!t->compare(head_one->key, key)) {

		if (head_one == t->head)
			t->head = t->head->next;
		else
			previous->next = head_one->next;

		free(head_one->key);
		free(head_one->payload);
		free(head_one);

	}
	return 0;
}

/** cerca l'elemento di chiave (key)

 \param t puntatore alla lista
 \param key la chiave


 \retval NULL se l'elemento non c'e'
 \retval p puntatore all'elemento trovato (puntatore interno alla lista non alloca memoria)

 */
elem_t * find_ListElement(list_t * t, void * key) {
	if (t == NULL || !key) {
#ifdef DEBUG
		printf("Error Tipe: %s Where: %s\n", "if (t == NULL || !key)",
				"GenList->find_ListElement");
#endif
		return NULL;
	}
	return find_Element(t->head, key, t->compare);
}


void free_elem_t(elem_t* e){
	if ( e != NULL){
	free(e->key);
			free(e->payload);
			free(e);
	}
}
