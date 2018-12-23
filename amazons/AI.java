package amazons;

import java.util.Iterator;

import static java.lang.Math.*;

import static amazons.Piece.*;
/** A Player that automatically generates moves.
 *  @author Jacob Lin
 */
class AI extends Player {

    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        if (depth == 0 || board.winner() == WHITE || board.winner() == BLACK) {
            return staticScore(board);
        }
        if (sense == 1) {
            Iterator<Move> iter = board.legalMoves(board.turn());
            int bestVal = Integer.MIN_VALUE;
            while (iter.hasNext()) {
                Move thisMove = iter.next();
                board.makeMove(thisMove);
                int response = findMove(board, depth - 1,
                        false, -sense, alpha, beta);
                board.undo();
                if (response > bestVal) {
                    if (saveMove) {
                        _lastFoundMove = thisMove;
                    }
                    bestVal = response;
                    alpha = max(alpha, response);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return bestVal;
        } else {
            Iterator<Move> iter2 = board.legalMoves(board.turn());
            int bestVal = Integer.MAX_VALUE;
            while (iter2.hasNext()) {
                Move thisMove = iter2.next();
                board.makeMove(thisMove);
                int response = findMove(board, depth - 1,
                        false, -sense, alpha, beta);
                board.undo();
                if (response < bestVal) {
                    if (saveMove) {
                        _lastFoundMove = thisMove;
                    }
                    bestVal = response;
                    beta = min(beta, response);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return bestVal;

        }
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private int maxDepth(Board board) {
        final int N = board.numMoves();
        final int firstbound = 20;
        final int secondbound = 30;
        final int thirdbound = 60;
        final int forthbound = 70;
        if (N <= firstbound) {
            return 1;
        } else if (N <= secondbound) {
            return 2;
        } else if (N <= thirdbound) {
            return 3;
        } else if (N <= forthbound) {
            return 4;
        } else {
            return 5;
        }
    }


    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        Piece winner = board.winner();
        final int N = 100000;
        if (winner == BLACK) {
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            return WINNING_VALUE;
        }
        Iterator<Move> toCount = board.legalMoves(board.turn());
        int i = 0;
        while (toCount.hasNext()) {
            toCount.next();
            i++;
        }
        if (i == 0) {
            return -N;
        }
        Iterator<Move> opCount = board.legalMoves(myOpponent(board.turn()));
        if (!opCount.hasNext()) {
            return N;
        }
        return i;
    }

    /** Return the opponent of MYTURN. */
    private Piece myOpponent(Piece myturn) {
        if (myturn == WHITE) {
            return BLACK;
        } else {
            return WHITE;
        }
    }


}
