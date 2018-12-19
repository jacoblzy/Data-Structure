package galaxy;

/** The input/output and GUI controller for play of a Galaxy puzzle.
 *  @author P. N. Hilfinger. */
public class Controller {

    /** Controller for a game represented by MODEL, using COMMANDS as the
     *  the source of commands, and PUZZLES to supply puzzles.  If LOGGING,
     *  prints commands received on standard output.  If TESTING, prints
     *  the board when possibly changed.  If VIEW is non-null, update it
     *  at appropriate points when the model changes. */
    public Controller(Model model, View view,
                      CommandSource commands, PuzzleSource puzzles,
                      boolean logging, boolean testing) {
        _model = model;
        _view = view;
        _commands = commands;
        _puzzles = puzzles;
        _logging = logging;
        _testing = testing;
        _solving = true;
    }

    /** Return true iff we have not received a Quit command. */
    boolean solving() {
        return _solving;
    }

    /** Clear the board and solve one puzzle, until receiving a quit or
     *  new-game request.  Update the viewer with each added tile or
     *  change in the board from tilting. */
    void solvePuzzle() {
        _model.clear();
        _puzzles.getPuzzle(_model);
        _model.markGalaxies(1);
        logPuzzle();
        logBoard();
        while (_solving) {
            if (_view != null) {
                _view.update(_model);
            }

            String cmnd = _commands.getCommand();
            if (_logging) {
                System.out.println(cmnd);
            }
            String[] parts = cmnd.split("\\s+");
            switch (parts[0]) {
            case "QUIT": case "Q":
                _solving = false;
                return;
            case "NEW": case "N":
                return;
            case "SIZE": case "SI": {
                int w = Integer.parseInt(parts[1]),
                    h = Integer.parseInt(parts[2]);
                assert w >= 3 && h >= 3;
                _model.init(w, h);
                return;
            }
            case "SEED": case "SE": {
                _puzzles.setSeed(Long.parseLong(parts[1]));
                return;
            }
            case "EDGE": {
                int w, h;
                w = Integer.parseInt(parts[1]);
                h = Integer.parseInt(parts[2]);
                _model.toggleBoundary(w, h);
                _model.markGalaxies(1);
                logBoard();
                break;
            }
            default:
                break;
            }
        }
    }

    /** If testing, print the contents of the board. */
    private void logBoard() {
        if (_testing) {
            System.out.printf("B[ %dx%d%s%n%s]%n", _model.cols(), _model.rows(),
                              _model.solved() ? " (SOLVED)" : "", _model);
        }
    }

    /** If logging, print the positions of the centers. */
    private void logPuzzle() {
        if (_logging) {
            System.out.printf("B%s%n", _model.centers());
        }
    }

    /** The board. */
    private Model _model;

    /** Our view of _model. */
    private View _view;

    /** Input source from standard input. */
    private CommandSource _commands;

    /** Input source from standard input. */
    private PuzzleSource _puzzles;

    /** True while user is still working on a puzzle. */
    private boolean _solving;

    /** True iff we are logging commands on standard output. */
    private boolean _logging;

    /** True iff we are testing the program and printing board contents. */
    private boolean _testing;

}
