#!/usr/bin/env python3
# -*-Python-*-

import testing
import os, sys, re
import io
import getopt
from os.path import join, exists

PROGRAM = "java -ea trip.Main"

def unwindow(text):
    """Remove bogus carriage returns from TEXT."""
    return re.sub(r'\r', '', text)

class Proj3_Trip_Tester(testing.Tester):
    """A tester for the trip application.  Each test ID.in represents the test
          java -ea trip.Main -m ID.map <contents of ID.in>
    The resulting standard output is compared to ID.out, after filtering out
    leading and trailing whitespace and blank lines."""

    def output_filter(self, id, text):
        """Filter out leading and trailing whitespace and blank lines."""
        return re.sub(r'(?m)^[ \t]*(\r?\n)?|[ \t]+$', '', unwindow(text))

    def command_args(self, testid):
        base = join(self.base_dir(testid), self.base_id(testid))
        with open(testid) as inp:
            targets = inp.read().strip()
        return "-m {base}.map {targets}".format(base=base, targets=targets)

    def input_files(self, id):
        name = self.base_id(id)
        result = ()
        for ext in ".in", ".map":
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
        else:
            super().output_compare(testid)


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

tester = Proj3_Trip_Tester(tested_program=PROGRAM, report_limit=show,
                           time_limit=15)

if tester.test_all(args):
    sys.exit(0)
else:
    sys.exit(1)

