package enigma;

import static enigma.EnigmaException.*;

/** An Alphabet consisting of the Unicode characters in a certain range in
 *  order.
 *  @author P. N. Hilfinger
 */
class CharacterRange extends Alphabet {

    /** An alphabet consisting of all characters between FIRST and LAST,
     *  inclusive. */
    CharacterRange(char first, char last) {
        _first = Character.toUpperCase(first);
        _last = Character.toUpperCase(last);
        _chararray = null;
        if (_first > _last) {
            throw error("empty range of characters");
        }
    }
    /** Alternative to create an alphabet if reads a NOBAR String. */
    CharacterRange(String nobar) {
        _mybet = nobar;
        _chararray = nobar.toCharArray();
    }

    @Override
    int size() {
        if (_chararray == null) {
            return _last - _first + 1;
        } else {
            return _chararray.length;
        }

    }

    @Override
    boolean contains(char ch) {
        if (_chararray == null) {
            return ch >= _first && ch <= _last;
        } else {
            return _mybet.contains(String.valueOf(ch));
        }
    }

    @Override
    char toChar(int index) {
        if (_chararray == null) {
            if (!contains((char) (_first + index))) {
                throw error("character index out of range");
            }
            return (char) (_first + index);
        } else {
            if (index >= _mybet.length()) {
                throw error("character index out of range2");
            }
            return _chararray[index];
        }
    }

    @Override
    int toInt(char ch) {
        if (_chararray == null) {
            if (!contains(ch)) {
                throw error("character out of range");
            }
            return ch - _first;
        } else {
            if (!contains(ch)) {
                throw error("character out of range2");
            }
            return _mybet.indexOf(ch);
        }
    }

    /** Range of characters in this Alphabet. */
    private char _first, _last;
    /** Chararray to store the random Alphabet. */
    private char[] _chararray;
    /** String to store my original Alphabet, if applies. */
    private String _mybet;

}
