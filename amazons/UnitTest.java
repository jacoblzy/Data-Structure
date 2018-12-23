package amazons;

import org.junit.Test;

import static amazons.Piece.*;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** The suite of all JUnit tests for the amazons package.
 *  @author Jacob Lin
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** Tests deep copy. */
    @Test
    public void testDeepCopy() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        Board c = new Board();
        c.copy(b);
        assertEquals(BLACK, c.get(3, 5));
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(EMPTY, c.get(9, 9));
        b.makeMove(Square.sq(3, 0), Square.sq(4, 0), Square.sq(3, 0));
        assertEquals(0, c.getMoveSize());
        assertEquals(1, b.getMoveSize());

    }

    /** Tests basic correctness of put and get on the initialized board. */
    @Test
    public void testBasicPutGet() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(BLACK, b.get(3, 5));
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(WHITE, b.get(9, 9));
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(EMPTY, b.get(3, 5));
    }

    /** Tests proper identification of legal/illegal queen moves. */
    @Test
    public void testIsQueenMove() {
        assertFalse(Square.sq("a4").isQueenMove(Square.sq(1, 5)));
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(2, 7)));
        assertFalse(Square.sq(0, 0).isQueenMove(Square.sq(5, 1)));
        assertTrue(Square.sq(1, 1).isQueenMove(Square.sq(9, 9)));
        assertTrue(Square.sq(2, 7).isQueenMove(Square.sq(8, 7)));
        assertTrue(Square.sq(3, 0).isQueenMove(Square.sq(3, 4)));
        assertTrue(Square.sq(7, 9).isQueenMove(Square.sq(0, 2)));
    }

    /** Tests toString for initial board state and a smiling board state. :) */
    @Test
    public void testToString() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        int count = 0;
        Iterator<Move> iter = b.legalMoves(WHITE);
        while (iter.hasNext()) {
            iter.next();
            count++;
        }
        assertEquals(count, 1942);
        assertEquals(SMILE, b.toString());
    }
    /** Tests of queen move. */
    @Test
    public void testQueenMove() {
        assertNull(Square.sq(1, 5).queenMove(0, 10));
        assertEquals(Square.sq(3, 3), Square.sq(1, 1).queenMove(1, 2));
        assertEquals(Square.sq(9, 9), Square.sq(3, 3).queenMove(1, 6));
        assertNull(Square.sq(3, 3).queenMove(1, 7));
    }

    /** Tests of direction in move. */
    @Test
    public void testDirection() {
        assertEquals(0, Square.sq(1, 5).direction(Square.sq(1, 9)));
        assertEquals(1, Square.sq(1, 5).direction(Square.sq(2, 6)));
        assertEquals(6, Square.sq(5, 7).direction(Square.sq(0, 7)));
        assertEquals(7, Square.sq(1, 5).direction(Square.sq(0, 6)));
    }
    /** Tests of isUnblockedMove. */
    @Test
    public void testIsUnblockedMove() {
        Board b = new Board();
        Square from = Square.sq(3, 0);
        Square toBlock = Square.sq(9, 6);
        Square noBlock = Square.sq(8, 5);
        assertFalse(b.isUnblockedMove(from, toBlock, Square.sq(5, 2)));
        assertTrue(b.isUnblockedMove(from, noBlock, Square.sq(5, 2)));
        b.put(SPEAR, Square.sq(5, 2));
        assertEquals(SPEAR, b.get(5, 2));
        assertFalse(b.isUnblockedMove(from, toBlock, null));
        assertFalse(b.isUnblockedMove(Square.sq(9, 0), Square.sq(9, 4), null));
        assertTrue(b.isUnblockedMove(Square.sq(9, 0), Square.sq(9, 2), null));
        b.put(WHITE, Square.sq(9, 1));
        b.put(WHITE, Square.sq(1, 9));
        assertFalse(b.isUnblockedMove(Square.sq(9, 0), Square.sq(9, 2), null));
        assertFalse(b.isUnblockedMove(Square.sq(3, 0), Square.sq(8, 5), null));
        b.put(SPEAR, Square.sq(7, 4));
        assertFalse(b.isUnblockedMove(from, toBlock, Square.sq(5, 2)));
        assertEquals(SPEAR, b.get(7, 4));
        assertFalse(b.isUnblockedMove(Square.sq(6, 3), noBlock, null));
    }

    @Test
    public void testUndo() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        b.makeMove(Square.sq(0, 3), Square.sq(3, 3), Square.sq(3, 6));
        assertNotEquals(INIT_BOARD_STATE, b.toString());
        b.makeMove(Square.sq(6, 9), Square.sq(9, 9), Square.sq(6, 9));
        b.makeMove(Square.sq(6, 0), Square.sq(7, 1), Square.sq(7, 0));
        b.undo();
        b.undo();
        b.undo();
        b.undo();
        assertEquals(INIT_BOARD_STATE, b.toString());
    }

    private void makeSmile(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }

    static final String INIT_BOARD_STATE =
            "   - - - B - - B - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   B - - - - - - - - B\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   W - - - - - - - - W\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - W - - W - - -\n";

    static final String SMILE =
            "   - - - - - - - - - -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - S - S - - S - S -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - W - - - - W - -\n"
                    + "   - - - W W W W - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n";

    /** Tests reachableFromIterator to make sure it returns all reachable
     *  Squares. This method may need to be changed based on
     *   your implementation. */
    @Test
    public void testReachableFrom() {
        Board b = new Board();
        buildBoard(b, REACHBBLEFROMTESTBOARD);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom =
                b.reachableFrom(Square.sq(5, 4), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertTrue(REACHABLEFORMSQS.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLEFORMSQS.size(), numSquares);
        assertEquals(REACHABLEFORMSQS.size(), squares.size());
        int numSquares2 = 0;
        Set<Square> squares2 = new HashSet<>();
        Iterator<Square> reachable2 = b.reachableFrom(Square.sq(0, 9),
                Square.sq(1, 9));
        while (reachable2.hasNext()) {
            Square s2 = reachable2.next();
            assertTrue(RETEST.contains(s2));
            numSquares2 += 1;
            squares2.add(s2);
        }
        assertEquals(RETEST.size(), numSquares2);
        assertEquals(RETEST.size(), squares2.size());

    }

    /** Tests legalMovesIterator to make sure it returns all legal Moves.
     *  This method needs to be finished and may need to be changed
     *  based on your implementation. */
    @Test
    public void testLegalMoves() {
        Board b = new Board();
        buildBoard(b, REACHBBLEFROMTESTBOARD);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(LEGALMOVETEST.contains(m));
            numMoves += 1;
            moves.add(m);
        }
        moves.toArray();
        assertEquals(LEGALMOVETEST.size(), numMoves);
        assertEquals(LEGALMOVETEST.size(), moves.size());

    }
    /** Test the efficiency of my legalMoveIterator. */
    @Test
    public void testRunTime() {
        Board c = new Board();
        int count = 0;
        Iterator<Move> iter = c.legalMoves(WHITE);
        while (iter.hasNext()) {
            iter.next();
            count++;
        }
        assertEquals(count, 2176);
    }
    @Test
    public void testCorner() {
        Board e = new Board();
        buildBoard(e, CORNERTEST);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = e.legalMoves(Piece.BLACK);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(CORNERTESTHASH.contains(m));
            numMoves += 1;
            moves.add(m);
        }
        moves.toArray();
        assertEquals(CORNERTESTHASH.size(), numMoves);
        assertEquals(CORNERTESTHASH.size(), moves.size());
    }


    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                b.put(piece, Square.sq(col, row));
            }
        }
    }

    static final Piece E = Piece.EMPTY;

    static final Piece W = Piece.WHITE;

    static final Piece B = Piece.BLACK;

    static final Piece S = Piece.SPEAR;

    static final Piece[][] REACHBBLEFROMTESTBOARD =
    {
            { W, S, E, S, E, E, E, S, E, S },
            { E, E, S, E, E, E, E, S, W, S },
            { B, S, S, E, E, E, E, S, E, S },
            { E, E, B, S, S, S, S, E, S, S },
            { E, E, E, S, E, E, E, E, B, E },
            { E, E, E, S, E, B, E, E, B, E },
            { E, E, E, S, S, S, B, S, B, E },
            { E, E, E, E, E, E, S, W, S, E },
            { E, E, E, E, E, E, S, S, S, S },
            { E, E, E, E, E, E, S, W, E, S },
    };

    static final Piece[][] CORNERTEST =
    {
            { W, S, E, S, E, E, E, S, E, B },
            { E, E, S, E, E, E, E, S, W, S },
            { E, S, S, E, E, E, E, S, E, S },
            { E, E, E, S, S, S, S, E, S, S },
            { E, E, E, S, E, E, E, E, E, E },
            { E, E, E, S, E, E, E, E, E, E },
            { E, E, E, S, S, S, E, S, E, E },
            { E, E, E, E, E, E, S, W, S, E },
            { W, W, W, E, E, E, S, S, S, S },
            { B, E, W, E, E, E, S, W, E, S },
    };

    static final Set<Square> REACHABLEFORMSQS =
            new HashSet<>(Arrays.asList(
                    Square.sq(5, 5),
                    Square.sq(4, 5),
                    Square.sq(4, 4),
                    Square.sq(6, 4),
                    Square.sq(7, 4),
                    Square.sq(6, 5),
                    Square.sq(7, 6),
                    Square.sq(8, 7)));

    static final Set<Square> RETEST =
            new HashSet<>(Arrays.asList(
                    Square.sq(0, 8),
                    Square.sq(1, 8),
                    Square.sq(1, 9),
                    Square.sq(2, 9)));

    static final Set<Move> LEGALMOVETEST =
            new HashSet<>(Arrays.asList(
                    Move.mv(Square.sq(0, 9), Square.sq(0, 8),
                            Square.sq(0, 9)),
                    Move.mv(Square.sq(0, 9), Square.sq(0, 8),
                            Square.sq(1, 8)),
                    Move.mv(Square.sq(0, 9), Square.sq(1, 8),
                            Square.sq(0, 9)),
                    Move.mv(Square.sq(0, 9), Square.sq(1, 8),
                            Square.sq(0, 8)),
                    Move.mv(Square.sq(0, 9), Square.sq(1, 8),
                            Square.sq(2, 9)),
                    Move.mv(Square.sq(8, 8), Square.sq(8, 9),
                            Square.sq(8, 8)),
                    Move.mv(Square.sq(8, 8), Square.sq(8, 9),
                            Square.sq(8, 7)),
                    Move.mv(Square.sq(8, 8), Square.sq(8, 7),
                            Square.sq(8, 8)),
                    Move.mv(Square.sq(8, 8), Square.sq(8, 7),
                            Square.sq(8, 9)),
                    Move.mv(Square.sq(8, 8), Square.sq(8, 7),
                            Square.sq(7, 6)),
                    Move.mv(Square.sq(7, 0), Square.sq(8, 0),
                            Square.sq(7, 0)),
                    Move.mv(Square.sq(8, 8), Square.sq(8, 7),
                            Square.sq(6, 5))));

    static final Set<Move> CORNERTESTHASH =
            new HashSet<>(Arrays.asList(
                    Move.mv(Square.sq(0, 0), Square.sq(1, 0),
                            Square.sq(0, 0)),
                    Move.mv(Square.sq(9, 9), Square.sq(8, 9),
                            Square.sq(9, 9))));


}

