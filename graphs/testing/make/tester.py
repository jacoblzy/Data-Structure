#!/usr/bin/env python3
# -*-Python-*-

import testing
import os, sys, re
import io
import getopt
from os.path import join, exists

PROGRAM = "java -ea make.Main"

def unwindow(text):
    """Remove bogus carriage returns from TEXT."""
    return re.sub(r'\r', '', text)

class Proj3_Make_Tester(testing.Tester):
    """A tester for the make application.  Each test ID.in represents the test
          java -ea make.Maih -f ID.mk -D ID.dir `cat ID.in`
    where ID.in contains the targets to be built for this test.  
    The resulting standard output is compared to ID.std, after filtering out
    leading and trailing whitespace and blank lines."""

    def to_lines(self, text):
        """Return TEXT broken into lines."""
        return re.split(r'\r?\n', text.rstrip())

    def command_args(self, testid):
        base = join(self.base_dir(testid), self.base_id(testid))
        with open(testid) as inp:
            targets = inp.read().strip()
        return "-f {base}.mk -D {base}.dir {targets}"\
               .format(base=base, targets=targets)

    def input_files(self, id):
        name = self.base_id(id)
        result = ()
        for ext in ".mk", ".dir", ".in":
            f = name + ext
            fullname = join(self.base_dir(id), f)
            if exists(fullname):
                result += ((f, fullname, None),)
        return result

    def error_filter(self, id, text):
        """Convert error messages to the phrase "<SOME ERROR MESSAGE>"
        in TEXT."""
        return re.sub(r'(?im)^.*\berror\b.*', '<SOME ERROR MESSAGE>',
                      unwindow(text))

    def output_compare(self, testid):
        """Sets .reason to True iff either:
            a. The RC is 0, there is no .err file for this test, and the
               filtered .stdout contents are equal to the filtered standard
               file; or
            b. The RC is non-zero, there is a .err file for this test, and
               the filtered error output equals the filtered standard error
               output.
        and otherwise sets .reason to error message."""

        self.reason = True
        if self.standard_error_file(testid):
            if self.rc == 0:
                self.reason = "Program did not detect error---exit code 0."
            else:
                std_err_output = \
                      testing.contents(self.standard_error_file(testid))
                if self.stderr is None or \
                   self.error_filter(testid, self.stderr) != std_err_output:
                    self.reason = "Missing error message or extra error output"
        elif self.rc != 0:
            self.reason = "Execution error---exit code not 0."
        else:
            std = self.to_lines(testing.contents(self.standard_output_file
                                                 (testid)))
            test_out = self.to_lines(self.stdout)
            if std != test_out:
                self.reason = "Wrong sequence of rebuilding commands issued"

show=None
try:
    opts, args = getopt.getopt(sys.argv[1:], '', ['show='])
    for opt, val in opts:
        if opt == '--show':
            show = int(val)
        else:
            assert False
except:
    print("Usage: python3 trip-tester.py [--show=N] TEST...",
          file=sys.stderr)
    sys.exit(1)

tester = Proj3_Make_Tester(tested_program=PROGRAM, report_limit=show)

if tester.test_all(args):
    sys.exit(0)
else:
    sys.exit(1)

