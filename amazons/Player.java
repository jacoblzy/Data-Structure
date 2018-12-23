package amazons;

/** A generic Amazons Player.
 *  @author Jacob Lin
 */
abstract class Player {

    /** A Player playing PIECE (WHITE or BLACK) in a game managed
     *  by CONTROLLER. */
    Player(Piece piece, Controller controller) {
        _controller = controller;
        _myPiece = piece;
    }

    /** Return my piece's color. */
    Piece myPiece() {
        return _myPiece;
    }

    /** Return the board I am playing on. */
    Board board() {
        return _controller.board();
    }

    /** A factory method that returns a Player with my concrete type that
     *  plays the side indicated by PIECE in a game controlled by
     *  CONTROLLER.  This typically will call the constructor for the class
     *  it appears in. */
    abstract Player create(Piece piece, Controller controller);

    /** Return either a String denoting either a legal move for me
     *  or another command (which may be invalid).  Always returns the
     *  latter if board().turn() is not myPiece() or if board.winner()
     *  is not null. */
    abstract String myMove();

    /** My Controller. */
    protected Controller _controller;
    /** The Piece I play (WHITE or BLACK). */
    protected final Piece _myPiece;
}
