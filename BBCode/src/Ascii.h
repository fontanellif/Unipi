//
//  Ascii.h
//  All
//
//  Created by Filippo Fontanelli on 02/04/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#include "queue.h"
#include "def.h"

#ifdef _ASCII_H
#define _ASCII_H

/** Determina la formattazione in base alle necessita' della formattazione ascii
 *  \param  t	token da analizzare
 *  \param  q	coda del token
 *  \param  pch	testo da analizzare
 *
 *  \retval 0       successo
 *  \retval ERROR	in caso di errore
 */
extern int CheckAscii(token *t,queue *q, char *pch);

/** Determina la formattazione in base alle necessita' della formattazione ansi
 *  \param  q       coda del token
 *  \param  endline numero di caratteri gia' stampati
 *
 *  \retval n       numero di caratteri stampati
 */
extern int PrintAscii(queue *q, int *endline);

/** Setta la formattazione nel token
 *  \param  t	token da settare
 *
 *  \retval c       caratteri da aggiungere al token
 *  \retval NULL	in caso di errore
 */
int SetAscii(token * t,queue *q,char * app);

/** Determina la formattazione
 *  \param  t	token da analizzare
 *  \param  pch	testo da analizzare
 *
 *  \retval EXIT_SUCCESS	successo
 *  \retval EXIT_FAILURE	in caso di errore
 */
static int Check(token *t, char *pch);

/** Determina la presenza di un'emoticon
 *  \param  t	token da analizzare
 *  \param  p	testo da analizzare
 *
 *  \retval EXIT_SUCCESS	successo
 *  \retval EXIT_FAILURE	in caso di errore
 */
static int check_smile(token *t,char* p);

#endif