package amazons;

import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;
import java.util.Collections;

import static amazons.Piece.*;


/** The state of an Amazons Game.
 *  @author Jacob Lin
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 10;

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        this._turn = model._turn;
        this._winner = model._winner;
        this.array2D = new Piece[10][10];
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                this.array2D[row][col] = model.array2D[row][col];
            }
        }
        this.moveNum = model.moveNum;
        this.allMyMoves = new Stack<>();
        this.allMyMoves.addAll(model.allMyMoves);
    }

    /** Clears the board to the initial position. */
    void init() {
        array2D = new Piece[10][10];
        _turn = WHITE;
        _winner = null;
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                put(EMPTY, row, col);
            }
        }
        put(WHITE, 3, 0);
        put(WHITE, 6, 0);
        put(WHITE, 0, 3);
        put(WHITE, 9, 3);
        put(BLACK, 0, 6);
        put(BLACK, 9, 6);
        put(BLACK, 3, 9);
        put(BLACK, 6, 9);
        moveNum = 0;
        allMyMoves = new Stack<>();
    }

    /** Return the Piece whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the number of moves (that have not been undone) for this
     *  board. */
    int numMoves() {
        return moveNum;
    }

    /** Return the winner in the current position, or null if the game is
     *  not yet finished. */
    Piece winner() {
        return _winner;
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        return array2D[row][col];
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        _winner = null;
        array2D[s.row()][s.col()] = p;
    }

    /** Set square (COL, ROW) to P. */
    final void put(Piece p, int col, int row) {
        _winner = null;
        array2D[row][col] = p;
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /** Return true iff FROM - TO is an unblocked queen move on the current
     *  board, ignoring the contents of ASEMPTY, if it is encountered.
     *  For this to be true, FROM-TO must be a queen move and the
     *  squares along it, other than FROM and ASEMPTY, must be
     *  empty. ASEMPTY may be null, in which case it has no effect. */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        if (!from.isQueenMove(to)) {
            return false;
        }
        Square temp = from;
        while (temp != to && temp != null) {
            temp = temp.queenMove(from.direction(to), 1);
            if (temp != null) {
                Piece atPiece = get(temp);
                if (atPiece != EMPTY && temp != asEmpty) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return _winner == null && get(from) == _turn;
    }

    /** Return true iff FROM-TO is a valid first part of move, ignoring
     *  spear throwing. */
    boolean isLegal(Square from, Square to) {
        return isLegal(from) && isUnblockedMove(from, to, null);
    }

    /** Return true iff FROM-TO(SPEAR) is a legal move in the current
     *  position. */
    boolean isLegal(Square from, Square to, Square spear) {
        return isLegal(from, to) && isUnblockedMove(to, spear, from);
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to(), move.spear());
    }

    /** Move FROM-TO(SPEAR), assuming this is a legal move. */
    void makeMove(Square from, Square to, Square spear) {
        assert isLegal(from, to, spear);
        Piece myturn = get(from);
        put(EMPTY, from);
        put(myturn, to);
        put(SPEAR, spear);
        moveNum++;
        Move mymove = Move.mv(from, to, spear);
        allMyMoves.push(mymove);
        if (myturn == BLACK) {
            _turn = WHITE;
        } else if (myturn == WHITE) {
            _turn = BLACK;
        }
        Iterator<Move> iter = legalMoves(_turn);
        if (!iter.hasNext()) {
            _winner = myturn;
        }
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to(), move.spear());
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (allMyMoves.empty()) {
            return;
        }
        Move myLastMove = allMyMoves.pop();
        Piece lastTurn = get(myLastMove.to());
        put(EMPTY, myLastMove.spear());
        put(EMPTY, myLastMove.to());
        put(lastTurn, myLastMove.from());
        _turn = lastTurn;
        moveNum--;
        _winner = null;
    }

    /** Return an Iterator over the Squares that are reachable by an
     *  unblocked queen move from FROM. Does not pay attention to what
     *  piece (if any) is on FROM, nor to whether the game is finished.
     *  Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     *  feature is useful when looking for Moves, because after moving a
     *  piece, one wants to treat the Square it came from as empty for
     *  purposes of spear throwing.) */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /** Return an Iterator over all legal moves on the current board. */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /** Return an Iterator over all legal moves on the current board for
     *  SIDE (regardless of whose turn it is). */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /** An iterator used by reachableFrom. */
    private class ReachableFromIterator implements Iterator<Square> {

        /** Iterator of all squares reachable by queen move from FROM,
         *  treating ASEMPTY as empty. */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = -1;
            _steps = 0;
            _asEmpty = asEmpty;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _dir < 8;
        }

        @Override
        public Square next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Square thisone = _from.queenMove(_dir, _steps);
            toNext();
            return thisone;
        }

        /** Advance _dir and _steps, so that the next valid Square is
         *  _steps steps in direction _dir from _from. */
        private void toNext() {
            if (_dir == 8) {
                return;
            }
            _steps++;
            Square nextSq = _from.queenMove(_dir, _steps);
            if (nextSq == null || !isUnblockedMove(_from, nextSq, _asEmpty)) {
                _dir++;
                _steps = 0;
                toNext();
            }
        }

        /** Starting square. */
        private Square _from;
        /** Current direction. */
        private int _dir;
        /** Current distance. */
        private int _steps;
        /** Square treated as empty. */
        private Square _asEmpty;
    }

    /** An iterator used by legalMoves. */
    private class LegalMoveIterator implements Iterator<Move> {

        /** All legal moves for SIDE (WHITE or BLACK). */
        LegalMoveIterator(Piece side) {
            _startingSquares = Square.iterator();
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _start != null;
        }


        @Override
        public Move next() {
            Move toreturn = Move.mv(_start, _nextSquare, _spearThrows.next());
            if (!_spearThrows.hasNext()) {
                toNext();
            }
            return toreturn;
        }

        /** Advance so that the next valid Move is
         *  _start-_nextSquare(sp), where sp is the next value of
         *  _spearThrows. */
        private void toNext() {
            if (!_pieceMoves.hasNext()) {
                _start = null;
                while (_startingSquares.hasNext()) {
                    Square temp = _startingSquares.next();
                    Iterator<Square> surrounded = reachableFrom(temp, null);
                    if (get(temp) == _fromPiece && surrounded.hasNext()) {
                        _start = temp;
                        break;
                    }
                }
                if (_start == null) {
                    return;
                }
                _pieceMoves = reachableFrom(_start, null);
            }
            _nextSquare = _pieceMoves.next();
            _spearThrows = reachableFrom(_nextSquare, _start);
        }

        /** Color of side whose moves we are iterating. */
        private Piece _fromPiece;
        /** Current starting square. */
        private Square _start;
        /** Remaining starting squares to consider. */
        private Iterator<Square> _startingSquares;
        /** Current piece's new position. */
        private Square _nextSquare;
        /** Remaining moves from _start to consider. */
        private Iterator<Square> _pieceMoves;
        /** Remaining spear throws from _piece to consider. */
        private Iterator<Square> _spearThrows;
    }

    @Override
    public String toString() {
        String result = "";
        for (int row = 9; row >= 0; row--) {
            result = result + "  ";
            for (int col = 0; col < 10; col++) {
                result += " " + array2D[row][col].toString();
            }
            result = result + "\n";
        }
        return result;
    }
    /** return the size of all my moves. */
    public int getMoveSize() {
        return allMyMoves.size();
    }

    /** An empty iterator for initialization. */
    private static final Iterator<Square> NO_SQUARES =
        Collections.emptyIterator();

    /** Piece whose turn it is (BLACK or WHITE). */
    private Piece _turn;
    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;
    /** A 2D array to store the pieces. */
    private Piece[][] array2D;
    /** Keep track of my number of moves. */
    private int moveNum;
    /** An ArrayList to store all my moves. */
    private Stack<Move> allMyMoves;
}
