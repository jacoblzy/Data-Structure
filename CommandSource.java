package galaxy;

/** Describes a source of input commands.  The possible text commands are as
 *  follows (parts of a command are separated by whitespace):
 *    - SIZE w h: replace the current board with one that is w cells wide and
 *                h cells high, with no centers or barriers.  Then start a new
 *                puzzle.  Requires that w, h >= 3 and that they are properly
 *                formed numerals.
 *    - NEW:      Start a new puzzle.
 *    - EDGE x, y: toggle an edge at coordinates (x, y).  Assumes that
 *                (x, y) is a valid edge designation.
 *    - SEED s: set a new random seed.
 *    - QUIT: exit the program.
 *  @author P. N. Hilfinger
 */
interface CommandSource {

    /** Returns one command string, trimmed of preceding and following
     *  whitespace and converted to upper case. */
    String getCommand();

}
