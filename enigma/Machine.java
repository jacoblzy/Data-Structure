package enigma;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static enigma.EnigmaException.error;

/** Class that represents a complete enigma machine.
 *  @author Jacob Lin
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allrotors = allRotors;
        _listofrotor = new ArrayList<Rotor>(allRotors);
        _plugboard = new Permutation("", alpha);
        tobeassigned = new Rotor[numRotors];
        if (_numRotors <= 1 || _numRotors <= pawls) {
            throw error("wrong configuration");
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (tobeassigned.length != rotors.length) {
            throw error("bad insertion");
        }
        int limitation = numPawls();
        for (int i = 0; i < rotors.length; i++) {
            for (Rotor r : _listofrotor) {
                if (r.name().toUpperCase().equals(rotors[i])) {
                    tobeassigned[i] = r;
                }
            }
        }
        if (!tobeassigned[0].reflecting()) {
            throw error("the first rotor must reflect");
        }
        for (Rotor everyrotor : tobeassigned) {
            if (everyrotor.rotates()) {
                limitation--;
                if (limitation < 0) {
                    throw error("too much moving rotors!");
                }
            }
        }
    }

    /** Return a list of all available rotors. */
    ArrayList<String> getallnames() {
        if (_listofrotor.size() == 0) {
            return null;
        }
        ArrayList<String> allavnames = new ArrayList<>();
        for (Rotor eachrotor : _listofrotor) {
            allavnames.add(eachrotor.name().toUpperCase());
        }
        return allavnames;
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw error("setting length incorrect");
        }
        if (!setting.toUpperCase().equals(setting)) {
            throw error("setting must be uppercase");
        }
        char[] settingChar = setting.toCharArray();
        for (int i = 0; i < settingChar.length; i++) {
            if (!_alphabet.contains(settingChar[i])) {
                throw error("setting out of char range");
            }
            tobeassigned[i + 1].set(settingChar[i]);
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceMachine(tobeassigned);
        int afterThisStep = _plugboard.permute(c);
        for (int i = tobeassigned.length - 1; i > 0; i--) {
            afterThisStep = tobeassigned[i].convertForward(afterThisStep);
        }
        afterThisStep = tobeassigned[0].convertForward(afterThisStep);
        for (int i = 1; i < tobeassigned.length; i++) {
            afterThisStep = tobeassigned[i].convertBackward(afterThisStep);
        }
        return _plugboard.invert(afterThisStep);
    }
    /** Helper method takeing LISTROTOR to deal with the stepping mechanism. */
    void advanceMachine(Rotor[] listrotor) {
        HashSet<Rotor> tobeadvanced = new HashSet<>();
        for (int i = 0; i < listrotor.length - 1; i++) {
            if (!listrotor[i].rotates()) {
                continue;
            }
            if (listrotor[i + 1].atNotch()) {
                tobeadvanced.add(listrotor[i]);
                tobeadvanced.add(listrotor[i + 1]);
            }
        }
        tobeadvanced.add(listrotor[listrotor.length - 1]);
        for (Rotor eachRotor: tobeadvanced) {
            eachRotor.advance();
        }
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String[] vocab = msg.trim().split(" ");
        String myresult = "";
        for (String eachword : vocab) {
            char[] tobetrans = eachword.toUpperCase().toCharArray();
            for (int i = 0; i < tobetrans.length; i++) {
                tobetrans[i] = _alphabet.toChar
                        (convert(_alphabet.toInt(tobetrans[i])));
            }
            myresult = myresult + new String(tobetrans);
        }
        return myresult.trim();
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** Number of all available rotors. */
    private int _numRotors;
    /** Number of all moving rotors. */
    private int _pawls;
    /** The permutation of the plugboard, default blank. */
    private Permutation _plugboard;
    /** Return a list of all available rotors. */
    private ArrayList<Rotor> _listofrotor;
    /** Return the rotors to be inserted according to the input file. */
    private Rotor[] tobeassigned;
    /** Return all rotors available. */
    private Collection<Rotor> _allrotors;
}
