package enigma;

import java.util.HashMap;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Jacob Lin
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        if (_cycles.length() != 0) {
            if (!examCycle(_cycles)) {
                throw error("bad cycle input");
            }
            spiltcycles = cycles.split("[)]");
            for (int i = 0; i < spiltcycles.length; i++) {
                spiltcycles[i] = spiltcycles[i].
                        replaceAll("[(]", "").trim();
            }
        }
    }
    /** Return true if MYCYCLE is intact. */
    boolean examCycle(String mycycle) {
        HashMap<Character, Integer> myvocab = new HashMap<>();
        myvocab.put("(".charAt(0), 0);
        myvocab.put(")".charAt(0), 0);
        for (int i = 0; i < mycycle.length(); i++) {
            char exam = mycycle.charAt(i);
            if (myvocab.containsKey(exam)) {
                myvocab.put(exam, myvocab.get(exam) + 1);
            }
        }
        return myvocab.get("(".charAt(0)).equals(myvocab.get(")".charAt(0)));
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles = cycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        p = wrap(p);
        return _alphabet.toInt(permute(_alphabet.toChar(p)));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        c = wrap(c);
        return _alphabet.toInt(invert(_alphabet.toChar(c)));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (_cycles.length() == 0) {
            return p;
        }
        for (String eachCycle : spiltcycles) {
            if (eachCycle.indexOf(p) != -1) {
                int a = eachCycle.indexOf(p);
                if (eachCycle.endsWith(String.valueOf(p))) {
                    return eachCycle.charAt(0);
                } else {
                    return eachCycle.charAt(a + 1);
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (_cycles.length() == 0) {
            return c;
        }
        for (String eachCycle : spiltcycles) {
            if (eachCycle.indexOf(c) != -1) {
                int a = eachCycle.indexOf(c);
                int len = eachCycle.length();
                if (eachCycle.startsWith(String.valueOf(c))) {
                    return eachCycle.charAt(len - 1);
                } else {
                    return eachCycle.charAt(a - 1);
                }
            }
        }
        return c;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        if (_cycles.length() == 0) {
            return false;
        }
        for (String ele : spiltcycles) {
            if (ele.length() == 1) {
                return false;
            }
        }
        for (int i = 0; i < _alphabet.size(); i++) {
            if (!_alphabet.contains(_alphabet.toChar(i))) {
                continue;
            }
            if (_cycles.indexOf(_alphabet.toChar(i)) == -1) {
                return false;
            }
        }
        return true;
    }
    /** Return the cycles of the permutation. */
    String getCycles() {
        return _cycles;
    }
    /** Return the spiltcycles of the permutation. */
    String[] getSpiltcycles() {
        return spiltcycles;
    }


    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** Cycle of this permutation. */
    private String _cycles;
    /** Spiltcycles of this permutation. */
    private String[] spiltcycles;

}
