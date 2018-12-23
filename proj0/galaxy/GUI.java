package galaxy;

import ucb.gui2.TopLevel;
import ucb.gui2.LayoutSpec;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.concurrent.ArrayBlockingQueue;

/** The GUI controller for a Galaxy board and buttons.
 *  @author P. N. Hilfinger
 */
class GUI extends TopLevel implements View {

    /** Minimum size of board in pixels. */
    private static final int MIN_SIZE = 500;

    /** A new window with given TITLE providing a view of MODEL. */
    GUI(String title, Model model) {
        super(title, true);
        addMenuButton("Game->New", this::newGame);
        addMenuButton("Game->Size", this::newSize);
        addMenuButton("Game->Seed", this::newSeed);
        addMenuButton("Game->Quit", this::quit);

        _cols = model.cols();
        _rows = model.rows();

        _widget = new BoardWidget(_pendingCommands);
        _widget.setSize(model.cols(), model.rows());
        add(_widget,
            new LayoutSpec("y", 0,
                           "height", "REMAINDER",
                           "width", "REMAINDER"));

    }

    /** Response to "Quit" button click. */
    private void quit(String dummy) {
        _pendingCommands.offer("QUIT");
    }

    /** Response to "New Game" button click. */
    private void newGame(String dummy) {
        _pendingCommands.offer("NEW");
    }

    /** Pattern describing the 'size' command's arguments. */
    private static final Pattern SIZE_PATN =
        Pattern.compile("\\s*(\\d{1,2})\\s*x\\s*(\\d{1,2})\\s*$");

    /** Pattern describing the 'seed' command's arguments. */
    private static final Pattern SEED_PATN =
        Pattern.compile("\\s*(-?\\d{1,18})\\s*$");

    /** Response to "Size" button click. */
    private void newSize(String dummy) {
        String response =
            getTextInput("Enter new size (<cols>x<rows>).",
                         "New size",  "plain",
                         String.format("%dx%d", _cols, _rows));
        if (response != null) {
            Matcher mat = SIZE_PATN.matcher(response);
            if (mat.matches()) {
                int cols = Integer.parseInt(mat.group(1)),
                    rows = Integer.parseInt(mat.group(2));
                if (cols >= 3 && rows >= 3) {
                    _pendingCommands.offer(String.format("SIZE %d %d",
                                                         cols, rows));
                }
            } else {
                showMessage("Bad board size chosen.", "Error", "error");
            }
        }
    }

    /** Response to "Size" button click. */
    private void newSeed(String dummy) {
        String response =
            getTextInput("Enter new random seed.",
                         "New seed",  "plain", "");
        if (response != null) {
            Matcher mat = SEED_PATN.matcher(response);
            if (mat.matches()) {
                _pendingCommands.offer(String.format("SEED %s", mat.group(1)));
            } else {
                showMessage("Enter an integral seed value.", "Error", "error");
            }
        }
    }

    /** Return the next command from our widget, waiting for it as necessary.
     *  Ordinary clicks on edges reported as "EDGE" commands.
     *  Menu-button clicks result in the messages "QUIT", "NEW", or
     *  "SIZE". */
    String readCommand() {
        try {
            return _pendingCommands.take();
        } catch (InterruptedException excp) {
            throw new Error("unexpected interrupt");
        }
    }

    @Override
    public void update(Model model) {
        if (model.rows() != _rows || model.cols() != _cols) {
            _cols = model.cols();
            _rows = model.rows();
            _widget.setSize(_cols, _rows);
            display(true);
        }
        _widget.update(model);
    }

    /** The board widget. */
    private BoardWidget _widget;
    /** The current size of the model. */
    private int _cols, _rows;

    /** Queue of pending key presses. */
    private ArrayBlockingQueue<String> _pendingCommands =
        new ArrayBlockingQueue<>(5);

}
