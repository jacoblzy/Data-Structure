package trip;

import static trip.Main.error;

/** Represents the direction of a road segment.
 *  @author P. N. Hilfinger
 */
enum Direction {
    /** Directions: NS (from north to south), SN (from south to
     *  north), WE (from west to east), EW (from east to west). */
    NS("south"), SN("north"), WE("east"), EW("west");

    /** A new Direction, supplying DIRNAME as the full name of the
     *  forward direction. */
    Direction(String dirName) {
        _dirName = dirName;
    }

    /** Return the direction opposite me. */
    Direction reverse() {
        switch (this) {
        case NS:
            return SN;
        case SN:
            return NS;
        case EW:
            return WE;
        case WE:
            return EW;
        default:
            return this;
        }
    }

    /** Returns a printable, English name for my "to" direction. */
    String fullName() {
        return _dirName;
    }

    /** Returns valueOf(NAME), but gives a more specific message on error. */
    static Direction parse(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException excp) {
            error("improper direction name: %s", name);
            return null;
        }
    }

    /** The word describing the direction of this road segment in the
     *  forward direction. */
    private final String _dirName;

}
