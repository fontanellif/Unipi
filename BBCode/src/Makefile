#################################################
#
# Makefile progetto lls 2011
# 
#################################################

# Compiler flags
CFLAGS = -Wall
# aggiungere -lgcov o -fprofile-arcs al linking

# Compilatore
CC = gcc

# Lista degli object files
OBJS = main.o Ansi.o Ascii.o def.h queue.o

# phony targets
.PHONY: bbfmt clean compila

bbfmt: $(OBJS)
	$(CC) $(CFLAGS) $(OBJS) -o bbfmt

main.o: main.c main.h

Ansi.o: Ansi.c Ansi.h

Ascii.o: Ascii.c Ascii.h

queue.o: queue.c queue.h

def.o: def.h

#compila

compila: bbfmt
	@echo "Compilazione riuscita!!\n"
#ripulisce
clean:
	@echo "\nComando Clear invocato, rimozione dei file .o e .~"	

	-rm -f *.o *.~
