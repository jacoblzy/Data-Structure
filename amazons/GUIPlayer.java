package amazons;

/** A Player that takes input from a GUI.
 *  @author Jacob Lin
 */
class GUIPlayer extends Player implements Reporter {

    /** A new GUIPlayer that takes moves and commands from GUI.  */
    GUIPlayer(GUI gui) {
        this(null, null, gui);
    }

    /** A new GUIPlayer playing PIECE under control of CONTROLLER, taking
     *  moves and commands from GUI. */
    GUIPlayer(Piece piece, Controller controller, GUI gui) {
        super(piece, controller);
        _gui = gui;
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new GUIPlayer(piece, controller, _gui);
    }

    @Override
    String myMove() {
        return _gui.readCommand();
    }

    @Override
    public void reportError(String fmt, Object... args) {
        _gui.reportError(fmt, args);
    }

    @Override
    public void reportNote(String fmt, Object... args) {
        _gui.reportNote(fmt, args);
    }

    @Override
    public void reportMove(Move unused) {
    }

    /** The GUI I use for input. */
    private GUI _gui;
}
