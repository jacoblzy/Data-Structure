package galaxy;

/** Describes a source of Galaxies puzzles.  Each puzzle consists of a list of
 *  locations of galactic centers.
 *  @author P. N. Hilfinger
 */
interface PuzzleSource {

    /** Initializes MODEL to a puzzle.  MODEL will contain centers, but no
     *  interior boundaries. */
    void getPuzzle(Model model);

    /** Reseed the random number generator with SEED. */
    void setSeed(long seed);

}
