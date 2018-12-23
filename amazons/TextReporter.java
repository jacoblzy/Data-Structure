package amazons;

/** A Reporter that uses the standard output for messaeges and standard
 *  error for error messages.
 *  @author P. N. Hilfinger
 */
class TextReporter implements Reporter {

    @Override
    public void reportError(String fmt, Object... args) {
        System.err.printf(fmt, args);
        System.err.println();
    }

    @Override
    public void reportNote(String fmt, Object... args) {
        System.out.printf("* " + fmt, args);
        System.out.println();
    }

    @Override
    public void reportMove(Move move) {
        System.out.printf("* %s%n", move);
    }
}
