//
//  main.h
//  All
//
//  Created by Filippo Fontanelli on 02/04/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <getopt.h>
#include <wchar.h>
#include <locale.h>
#include "queue.h"
#include "def.h"
#include "Ansi.h"
#include "Ascii.h"

#ifndef MAIN_H
#define MAIN_H

/*Definizione dei puntatori a funzione relative alle principali funzioni
 *del comando
 */
typedef int (*checkModeCallback)(token *t, queue *q, char *pch);

typedef int (*printModeCallback)(queue *q, int *endline);

typedef int (*readStdin)(char * app, int colum);


int CheckAscii(token *t,queue *q, char *pch);

int PrintAscii(queue *q, int *endline);

int CheckAnsi(token *t, queue *q, char *pch);

int PrintAnsi(queue *q, int *endline);

int ReadStdin7(char * app, int colum);

int ReadStdinDefault(char * app, int colum);

int CheckDefault(token *t, queue *q, char *pch);

int PrintDefault(queue *q, int *endline);

readStdin Read[R_MODE_MAX] = 
{
    ReadStdinDefault,
    ReadStdin7
};

checkModeCallback CheckFunc[O_MODE_MAX] =
{
	CheckAscii,
    CheckAnsi,
    CheckDefault
};

printModeCallback PrintFun[O_MODE_MAX] =
{
	PrintAscii,
    PrintAnsi,
    PrintDefault
};


/** Determina le opzioni del comando
 *  \param  argc	argc del main
 *  \param  argv	argv del main
 *  \param  option	array delle opzioni
 *
 *  \retval 0		successo
 *  \retval ERROR	in caso di errore
 */
int check_option_cmd_line(int argc, char** argv,int *option);

/** Giustifica il testo della coda
 *  \param  q       coda
 *
 *  \retval 0		successo
 *  \retval ERROR	in caso di errore
 */
int giustifica(queue *q);

#endif