package galaxy;

import java.util.Scanner;
import java.util.NoSuchElementException;

/** A type of InputSource that receives commands from a Scanner. This kind
 *  of source is intended for testing.
 *  @author P. N. Hilfinger
 */
class TestSource implements CommandSource, PuzzleSource {

    /** Provides commands and puzzles from SOURCE.  */
    TestSource(Scanner source) {
        source.useDelimiter("[ \t\n\r(,)]+");
        _source = source;
    }

    /** Returns a command string read from my source. At EOF, returns QUIT.
     *  Allows comment lines starting with "#", which are discarded. */
    @Override
    public String getCommand() {
        while (_source.hasNext()) {
            String line = _source.nextLine().trim().toUpperCase();
            if (!line.startsWith("#")) {
                return line;
            }
        }
        return "QUIT";
    }

    /** Initialize MODEL to a puzzle.  Throws IllegalStateException if there is
     *  no puzzle available, or it does not fit in MODEL. */
    @Override
    public void getPuzzle(Model model) {
        while (_source.hasNext("#")) {
            _source.nextLine();
        }
        if (!_source.hasNext("B\\[")) {
            throw new IllegalStateException("no puzzle");
        }
        try {
            model.clear();
            _source.next();
            while (_source.hasNextInt()) {
                model.placeCenter(_source.nextInt(), _source.nextInt());
            }
            _source.next("\\]");
        } catch (IllegalArgumentException | NoSuchElementException excp) {
            throw new IllegalStateException("bad test input");
        }
    }

    @Override
    public void setSeed(long seed) {
    }

    /** Input source. */
    private Scanner _source;
}
