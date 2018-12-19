# This a Makefile, an input file for the GNU 'make' program.  For you 
# command-line and Emacs enthusiasts, this makes it possible to build
# this program with a single command:
#     make 
# You can also clean up junk files and .class files with
#     make clean
# To run style61b (our style enforcer) over your source files, type
#     make style
# Finally, you can run any tests you'd care to with
#     make check

SHELL = bash

STYLEPROG = style61b

PYTHON = python3

PACKAGE = galaxy

# A non-standard classpath that works on Linux, Mac, and Windows.
# To Unix-like systems (Linux and Mac) it has the form
#     <valid classpath>:<garbage classpath (ignored)>
# while to Windows systems it looks like
#     <garbage classpath (ignored)>;<valid classpath>
CPATH = "..:$(CLASSPATH):;..;$(CLASSPATH)"

# Flags to pass to Java compilations (include debugging info and report
# "unsafe" operations.)
JFLAGS = -g -Xlint:unchecked -cp $(CPATH) -d .. -encoding utf8

CLASSDEST = ..

REMOTEBIN = cs61b@torus.cs.berkeley.edu:bin

# All .java files to be compiled.
SRCS = $(wildcard *.java)

CLASSES = $(SRCS:.java=.class)

# Test directories
TESTS = tests

# Tell make that these are not really files.
.PHONY: clean default compile force style  \
	check unit integration jar dist

%.class: %.java
	javac $(JFLAGS) -d "$(CLASSDEST)" $^ || { $(RM) $@; false; }

# By default, make sure all classes are present and check if any sources have
# changed since the last build.
default: compile

compile: Main.class

style:
	$(STYLEPROG) $(SRCS) 

Main.class: $(SRCS)
	javac $(JFLAGS) -d "$(CLASSDEST)" $(SRCS) || { $(RM) $@; false; }

force:
	javac $(JFLAGS) -d "$(CLASSDEST)" $(SRCS)

# Run Tests.
check: 
	$(MAKE) unit
	$(MAKE) -C .. PYTHON=$(PYTHON) check

# Run unit tests in this directory
unit: compile
	cd ..; java -ea galaxy.UnitTests

# Find and remove all *~ and *.class files.
clean:
	$(RM) *.class *~

jar:
	$(MAKE) -C staff-galaxy

dist: jar
	rsync -a ../bin/ $(REMOTEBIN)
