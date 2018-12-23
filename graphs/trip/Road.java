package trip;

import static trip.Main.error;

/** Represents a road between two Locations.
 *  @author P. N. Hilfinger
 */
class Road {

    /** A Road whose name is NAME, going in DIRECTION, and of given
     *  LENGTH. */
    Road(String name, Direction direction, double length) {
        if (length < 0) {
            error("Road %s given negative length.", length);
        }
        _name = name;
        _direction = direction;
        _length = length;
    }

    /** Return the direction of this road. */
    Direction direction() {
        return _direction;
    }

    @Override
    public String toString() {
        return _name;
    }

    /** Return the length of this road segment. */
    public double length() {
        return _length;
    }

    /** The name given to this segment. */
    private final String _name;
    /** The direction this segment runs towards its destination. */
    private final Direction _direction;
    /** The length of this segment. */
    private final double _length;

}
