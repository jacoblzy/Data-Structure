package galaxy;

import ucb.gui2.Pad;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.BasicStroke;
import java.awt.event.MouseEvent;

import static galaxy.Place.pl;

/** A widget that displays a Galaxy puzzle.
 *  @author P. N. Hilfinger
 */
class BoardWidget extends Pad {

    /* Parameters controlling sizes, speeds, colors, and fonts. */

    /** Colors of empty squares and grid lines. */
    static final Color
        BACKGROUND_COLOR = Color.lightGray,
        GALAXY_SQUARE_COLOR = Color.white,
        CENTER_COLOR = Color.white,
        GRID_LINE_COLOR = Color.black;

    /** Bar width separating tiles and length of tile's side
     *  (pixels). */
    static final int
        GRID_LINE_WIDTH = 1,
        BOUNDARY_WIDTH = 4,
        CENTER_RADIUS = 7,
        OFFSET = 2,
        CELL_SIDE = 29;

    /** Separation between cells centers and half separation between
     *  centers. */
    static final int
        CELL_SEP = CELL_SIDE + GRID_LINE_WIDTH,
        HALF_SEP = CELL_SEP / 2;

    /** Strokes for ordinary grid lines and those that are parts of
     *  boundaries. */
    static final BasicStroke
        GRIDLINE_STROKE = new BasicStroke(GRID_LINE_WIDTH),
        BOUNDARY_STROKE = new BasicStroke(BOUNDARY_WIDTH);

    /** Color for overlay text on board. */
    static final Color OVERLAY_COLOR = new Color(200, 0, 0, 64);

    /** Font for overlay text on board. */
    static final Font OVERLAY_FONT = new Font("SansSerif", Font.BOLD, 32);

    /** A graphical representation of a Galaxy board that sends commands
     *  derived from mouse clicks to COMMANDS. */
    BoardWidget(ArrayBlockingQueue<String> commands) {
        _commands = commands;
        _marked = new boolean[3][3];
        setMouseHandler("click", this::mouseClicked);
        setSize(3, 3);
    }

    /** Set the size of the board to COLS x ROWS. */
    public void setSize(int cols, int rows) {
        synchronized (me) {
            _rows = rows; _cols = cols;
            _boardWidth = cols * CELL_SEP + BOUNDARY_WIDTH;
            _boardHeight = rows * CELL_SEP + BOUNDARY_WIDTH;
            _marked = new boolean[cols][rows];
            _centers.clear();
            _boundaries.clear();
            setPreferredSize(_boardWidth, _boardHeight);
        }
        repaint();
    }

    /** Indicate that "SOLVED" label should be displayed. */
    synchronized void markEnd() {
        _end = true;
        repaint();
    }

    /** Draw the grid lines on G. */
    private void drawGrid(Graphics2D g) {
        g.setColor(GRID_LINE_COLOR);
        g.setStroke(GRIDLINE_STROKE);
        for (int k = 0; k <= 2 * _cols; k += 2) {
            g.drawLine(cx(k), cy(0), cx(k), cy(2 * _rows));
        }
        for (int k = 0; k <= 2 * _rows; k += 2) {
            g.drawLine(cx(0), cy(k), cx(2 * _cols), cy(k));
        }
    }

    /** Paint all marked cells on G. */
    private void drawMarkedCells(Graphics2D g) {
        g.setColor(GALAXY_SQUARE_COLOR);
        for (int x = 1; x < 2 * _cols; x += 2) {
            for (int y = 1; y < 2 * _rows; y += 2) {
                if (_marked[x / 2][y / 2]) {
                    g.fillRect(GRID_LINE_WIDTH + cx(x) - HALF_SEP,
                               GRID_LINE_WIDTH + cy(y) - HALF_SEP,
                               CELL_SIDE, CELL_SIDE);
                }
            }
        }
    }

    /** Draw all boundaries on G. */
    private void drawBoundaries(Graphics2D g) {
        g.setColor(GRID_LINE_COLOR);
        g.setStroke(BOUNDARY_STROKE);
        for (Place segm : _boundaries) {
            int xm = cx(segm.x),
                ym = cy(segm.y);

            if (segm.x % 2 == 0) {
                g.drawLine(xm, ym - HALF_SEP, xm, ym + HALF_SEP);
            } else {
                g.drawLine(xm - HALF_SEP, ym, xm + HALF_SEP, ym);
            }
        }
    }

    /** Draw all galactic centers on G. */
    private void drawCenters(Graphics2D g) {
        g.setColor(CENTER_COLOR);
        g.setStroke(GRIDLINE_STROKE);
        for (Place center : _centers) {
            g.fillOval(cx(center.x) - CENTER_RADIUS,
                       cy(center.y) - CENTER_RADIUS,
                       2 * CENTER_RADIUS, 2 * CENTER_RADIUS);
        }

        g.setColor(GRID_LINE_COLOR);
        for (Place center : _centers) {
            g.drawOval(cx(center.x) - CENTER_RADIUS,
                       cy(center.y) - CENTER_RADIUS,
                       2 * CENTER_RADIUS, 2 * CENTER_RADIUS);
        }
    }

    /** Indicate that puzzle is solved on G. */
    private void markSolved(Graphics2D g) {
        g.setFont(OVERLAY_FONT);
        FontMetrics metrics = g.getFontMetrics();
        g.setColor(OVERLAY_COLOR);
        g.drawString("SOLVED",
                     (OFFSET + _boardWidth
                      - metrics.stringWidth("SOLVED")) / 2,
                     OFFSET
                     + (2 * _boardHeight + metrics.getMaxAscent()) / 4);
    }


    @Override
    public synchronized void paintComponent(Graphics2D g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, _boardWidth, _boardHeight);

        drawGrid(g);
        drawMarkedCells(g);
        drawBoundaries(g);
        drawCenters(g);

        if (_end) {
            markSolved(g);
        }
    }

    /** Handle mouse click event E, creating an EDGE command if indicated. */
    private synchronized void mouseClicked(String unused, MouseEvent e) {
        int xpos = e.getX(), ypos = e.getY();
        int x = Math.round((float) (xpos - OFFSET) / HALF_SEP),
            y = 2 * _rows - Math.round((float) (ypos - OFFSET) / HALF_SEP);
        if (x % 2 != y % 2
            && x > 0 && y > 0 && x < 2 * _cols && y < 2 * _rows) {
            _commands.offer(String.format("EDGE %d %d", x, y));
        }
    }

    /** Revise the displayed board according to MODEL. */
    synchronized void update(Model model) {
        _centers.clear();
        _centers.addAll(model.centers());
        _marked = new boolean[_cols][_rows];
        for (int x = 0; x < _cols; x += 1) {
            for (int y = 0; y < _rows; y += 1) {
                _marked[x][y] = (model.mark(2 * x + 1, 2 * y + 1) > 0);
            }
        }

        _boundaries.clear();
        for (int x = 0; x < model.xlim(); x += 1) {
            for (int y = 0; y < model.ylim(); y += 1) {
                if (model.isBoundary(x, y)) {
                    _boundaries.add(pl(x, y));
                }
            }
        }

        _end = model.solved();

        repaint();
    }

    /** Return pixel coordinates of vertical board coordinate Y relative
     *  to window. */
    private int cy(int y) {
        return OFFSET + (2 * _rows - y) * HALF_SEP;
    }

    /** Return pixel coordinates of horizontal board coordinate X relative
     *  to window. */
    private int cx(int x) {
        return OFFSET + x * HALF_SEP;
    }

    /** The list of galactic centers currently displayed. */
    private final ArrayList<Place> _centers = new ArrayList<>();
    /** The list of boundaries currently displayed. */
    private final ArrayList<Place> _boundaries = new ArrayList<>();
    /** Marked cells: _marked[COLUMN][ROW] true iff cell in column COLUMN and
     *  row ROW is part of a complete galaxy. */
    private boolean[][] _marked;

    /** Number of rows and of columns. */
    private int _rows, _cols;

    /** Queue on which to post commands (from mouse clicks). */
    private ArrayBlockingQueue<String> _commands;

    /** Length (in pixels) of the side of the board. */
    private int _boardWidth, _boardHeight;
    /** True iff "GAME OVER" message is being displayed. */
    private boolean _end;
}
