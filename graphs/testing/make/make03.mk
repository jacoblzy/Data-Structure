foo.o: foo.c foo.h
        gcc -g -c foo.o foo.c

foo: foo.o
	gcc -o foo foo.o

foo.c: foo.y
	yacc -o foo.c foo.y
