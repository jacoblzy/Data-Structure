package trip;

import static java.lang.Math.sqrt;

/** Represents a location on a map.
 *  @author P. N. Hilfinger
 */
class Location {

    /** A location identified by NAME, at coordinates (X, Y). */
    Location(String name, double x, double y) {
        _name = name;
        _x = x;
        _y = y;
    }

    /** Return the distance between me and Y. */
    double dist(Location y) {
        double dx = _x - y._x;
        double dy = _y - y._y;
        return sqrt(dx * dx + dy * dy);
    }

    /** Returns the distance between locations X and Y. */
    public double dist(Location x, Location y) {
        return x.dist(y);
    }

    @Override
    public String toString() {
        return _name;
    }

    /** Returns my estimated distance from the origin. */
    public double distance() {
        return _dist;
    }

    /** Set distance() to W. */
    public void setDistance(double w) {
        _dist = w;
    }

    /** The identifying name of this location. */
    private String _name;
    /** Coordinates of this location. */
    private double _x, _y;
    /** Estimated distance from starting point. */
    private double _dist;
}
