//
//  def.h
//  All
//
//  Created by Filippo Fontanelli on 02/04/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//


#ifndef _DEF_H
#define _DEF_H

/*Define relative all'implementazione della coda*/
#define EMPTY 0
#define FULL 100
#define INIT_QUEUE(q,w) if((init(q,w) == ERROR)) { oops("error: init(queue *q, int* want_opt) "); }
#define INIT_TOKEN(t) if((init_token(t) == ERROR)) { oops("error: init_token(token *t) "); }
#define OPT_NUM 4

/*Define relative al token*/
#define DEFAULT_NUM_COLUM 78
#define LINK 50

/*Define relative all'allocazione di memoria*/
#define oops(s) { perror((s)); exit(EXIT_FAILURE); }
#define MALLOC(s,t) if(((s) = malloc(t)) == NULL) { oops("error: malloc() "); }

/*Define dei vari tipi di errori*/
#define ERROR_COLUM -2
#define ERROR_EOF -10
#define ERROR -1

#define O_COLUM 2
#define O_7 3

typedef enum {
    R_DEFAULT,
    R_7,
    R_MODE_MAX
}ReadMode;

typedef enum{
	O_ASCI,
    O_ANSI,
    O_DEFAULT,
	O_MODE_MAX
} OptMode;

enum {
    FS_B,
    FS_I,
    FS_U,
    FS_URL,
    FS_IMG,
    FS_QUOTE,
    FS_CODE,
    F_P,
    FE_B,
    FE_I,
    FE_U,
    FE_URL,
    FE_IMG,
    FE_QUOTE,
    FE_CODE,
    F_SAD,
    F_SMILE,
    NUM_FORMATTAZIONI
}Formato;

#define FORMATTAZIONE 0
#define STAMPA_F 1

#define S_CODE  "code"
#define S_B "b"
#define S_I "i"
#define S_U "u"
#define S_URL "url="
#define S_IMG "img="
#define S_QUOTE "quote"
#define S_CODE "code"
#define S_P "p"
#define E_CODE "/code"
#define E_B "/b"
#define E_I "/i"
#define E_U "/u"
#define E_URL "/url"
#define E_IMG "/img"
#define E_QUOTE "/quote"
#define E_CODE "/code"
#define S_SMILE "smile"
#define S_SAD "sad"

/*ripristino scrittura standard*/
#define ESCapeStart  "\033["
#define ESCapeEnd  "m"
#define ESCapeF  ";"

#define RIPRISTINA   "0"
#define RIPRISTINAEND   "\033[0m"

/*stili*/
#define GRASSETTO     "1"
#define SOTTOLINEATO  "4"
#define CORSIVO       "3"

/*colori*/
#define NERO    "30"
#define ROSSO   "31"
#define VERDE   "32"
#define GIALLO  "33"
#define BLU     "34"
#define MAGENTA "35"
#define CIANO   "36"
#define BIANCO  "37"

#endif
