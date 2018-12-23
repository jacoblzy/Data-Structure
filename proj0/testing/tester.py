#!/usr/bin/env python3
# -*-Python-*-

import testing
import os, sys, re
import io
import getopt
from os.path import join

PROGRAM = "java -ea galaxy.Main --testing --no-display"

os.environ.pop('DISPLAY', None)

class Proj0_Tester(testing.Tester):
    def output_filter(self, id, text):
        text = re.sub(r'#.*\r?\n','',text)
        return text

show=None
try:
    opts, args = getopt.getopt(sys.argv[1:], '', ['show='])
    for opt, val in opts:
        if opt == '--show':
            show = int(val)
        else:
            assert False
except:
    print("Usage: python3 tester.py [--show=N] TEST.in...",
          file=sys.stderr)
    sys.exit(1)

tester = Proj0_Tester(tested_program=PROGRAM, report_limit=show,
                      report_char_limit=10000)

sys.exit(0 if tester.test_all(args) else 1)

