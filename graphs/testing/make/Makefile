.PHONY: check clean

SHELL = bash

PYTHON = python3

TESTER := $(PYTHON) tester.py

TESTER_FLAGS =

CPATH = '../..:$(CLASSPATH):;..\..;$(CLASSPATH)'

check:
	@echo "Testing application make..."
	@CLASSPATH=$(CPATH) $(TESTER) $(TESTER_FLAGS) *.in

clean:
	$(RM) -r _* *~
