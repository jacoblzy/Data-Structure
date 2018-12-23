package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.NoSuchElementException;

import static enigma.EnigmaException.error;

/** Enigma simulator.
 *  @author Jacob Lin
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }


    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine myMachine = readConfig();
        int atleastset = 0;
        while (_input.hasNextLine()) {
            String unknown = _input.nextLine();
            if (unknown.startsWith("*")) {
                setUp(myMachine, unknown);
                atleastset++;
            } else if (unknown.length() != 0
                    && unknown.trim().equals("")) {
                continue;
            } else {
                if (atleastset == 0) {
                    throw error("no setting line");
                }
                String myresult = myMachine.convert(unknown.toUpperCase());
                printMessageLine(myresult);
            }

        }

    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String firstLine = _config.nextLine();
            if (firstLine.contains(String.valueOf("-"))) {
                char firstchar = firstLine.charAt(0);
                char lastchar = firstLine.charAt(firstLine.length() - 1);
                _alphabet = new CharacterRange(firstchar, lastchar);
            } else {
                _alphabet = new CharacterRange(firstLine);
            }
            int numRotors = _config.nextInt();
            int numPawls = _config.nextInt();
            ArrayList<Rotor> myRotors = new ArrayList<>();
            while (_config.hasNextLine()) {
                String what = _config.nextLine();
                if (what.trim().equals("")) {
                    continue;
                }
                Rotor thisRotor = readRotor(what);
                myRotors.add(thisRotor);
            }
            return new Machine
            (_alphabet, numRotors, numPawls, myRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }
    /** Return a rotor, reading its description THISLINE from _config. */
    private Rotor readRotor(String thisline) {
        try {
            while (thisline.equals("")) {
                thisline = _config.nextLine();
            }
            String[] thisarray = thisline.trim().split(" ", 3);
            Permutation myperm = new Permutation(thisarray[2], _alphabet);
            String myname = thisarray[0];
            if (thisarray[1].trim().startsWith("M")) {
                String mynotch = thisarray[1].substring(1);
                return new MovingRotor(myname, myperm, mynotch);
            } else if (thisarray[1].trim().startsWith("N")) {
                return new FixedRotor(myname, myperm);
            } else if (thisarray[1].trim().startsWith("R")) {
                while (!myperm.derangement()) {
                    String anothercycle = _config.nextLine().trim();
                    thisarray[2] = thisarray[2] + anothercycle;
                    myperm = new Permutation(thisarray[2], _alphabet);
                }
                return new Reflector(myname, myperm);
            } else {
                throw error("no such a rotor");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        if (settings.trim().indexOf("*") != 0) {
            throw error("setting must start with *");
        }
        String[] mysettingarray =
                settings.trim().substring(1).trim().split(" ");
        String[] mynames = new String[M.numRotors()];
        System.arraycopy(mysettingarray, 0, mynames, 0, M.numRotors());
        HashSet<String> toexam = new HashSet<>();
        for (int i = 0; i < mynames.length; i++) {
            String eachname = mynames[i];
            toexam.add(eachname);
            ArrayList<String> names = M.getallnames();
            if (!names.contains(eachname)) {
                throw error("BAD ROTOR NAME!");
            }
        }
        if (mynames.length != toexam.size()) {
            throw error("duplicate rotors");
        }
        String mysetting = mysettingarray[M.numRotors()];
        if (mysetting.trim().length() != M.numRotors() - 1) {
            throw error("bad rotor input(name)");
        }
        M.insertRotors(mynames);
        M.setRotors(mysetting);
        if (settings.contains("(")) {
            String plugperm = settings.substring(settings.indexOf("("));
            M.setPlugboard(new Permutation(plugperm, _alphabet));
        } else {
            M.setPlugboard(new Permutation("", _alphabet));
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        while (msg.length() >= 5) {
            _output.print(msg.substring(0, 5));
            msg = msg.substring(5);
            if (msg.length() > 0) {
                _output.print(" ");
            }
        }
        _output.println(msg);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
