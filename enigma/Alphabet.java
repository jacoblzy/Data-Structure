package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author P. N. Hilfinger
 */
abstract class Alphabet {

    /** Returns the size of the alphabet. */
    abstract int size();

    /** Returns true if preprocess(CH) is in this alphabet. */
    abstract boolean contains(char ch);

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    abstract char toChar(int index);

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    abstract int toInt(char ch);

}
