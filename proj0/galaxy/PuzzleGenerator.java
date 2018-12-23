package galaxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import static galaxy.Place.pl;

/** A creator of random Galaxies puzzles.
 *  @author P. N. Hilfinger
 */
class PuzzleGenerator implements PuzzleSource {

    /** The minimum distance between centers used for the initial set
     *  of center points. */
    private static final int MIN_CENTER_SEP = 5;

    /** A new PuzzleGenerator whose random-number source is seeded with SEED. */
    PuzzleGenerator(long seed) {
        _random = new Random(seed);
    }

    /* Strategy:  We create a possible solution, and then return a Model with
     * those centers, and otherwise clear.  To find a set of centers, we
     * randomly select a fraction of the possible centers that are separated
     * from each other by a minimum distance that guarantees that their
     * smallest containing galaxies do not overlap.  We then grow those
     * initial galaxies two cells at a time in round-robin fashion.  For
     * the remaining uncovered cells, we repeatedly select a center that
     * allows a maximal sized galaxy to be formed from the remaining
     * unused cells until all cells are used up.  */

    @Override
    public void getPuzzle(Model model) {
        model.copy(makePuzzle(model.cols(), model.rows()));
    }

    /** Return a COLS x ROWS model initialized with a new puzzle. */
    private Model makePuzzle(int cols, int rows) {
        Model M = new Model(cols, rows);

        List<Region> regions = getInitialCenters(M);
        growRegions(M, regions);
        allotUnmarked(M);
        M.markAll(0);

        return M;
    }

    /** Grow each of the items in REGIONS in round-robin fashion until no more
     *  growth is possible, marking all cells in M that are added. */
    private void growRegions(Model M, List<Region> regions) {
        boolean changed;
        changed = true;
        while (changed) {
            changed = false;
            for (Region r : regions) {
                List<Place> adjacent =
                    M.unmarkedSymAdjacent(r.center, r.cells);
                if (adjacent.isEmpty()) {
                    continue;
                }
                changed = true;
                Place p = adjacent.get(_random.nextInt(adjacent.size()));
                M.mark(p, 1);
                M.mark(M.opposing(r.center, p), 1);
                r.cells.add(p);
                r.cells.add(M.opposing(r.center, p));
            }
        }
    }

    /** Add an initial set of centers to M, also marking the cells
     *  containing them. Initial centers are all separated from each
     *  other by a Manhattan distance of at least MIN_CENTER_SEP. M
     *  must be initially empty.  Returns a list of the regions that
     *  are set (i.e., the centers and containing cells). */
    private ArrayList<Region> getInitialCenters(Model M) {
        ArrayList<Place> places = new ArrayList<>();
        for (int x = 1; x < M.xlim() - 1; x += 1) {
            for (int y = 1; y < M.ylim() - 1; y += 1) {
                places.add(pl(x, y));
            }
        }
        Collections.shuffle(places, _random);

        int numInitialCenters = M.cols() * M.rows() / 5;
        ArrayList<Region> result = new ArrayList<>();
        PlaceCenters:
        while (!places.isEmpty() && result.size() < numInitialCenters) {
            Place p = places.remove(places.size() - 1);
            for (Place c : M.centers()) {
                if (c.dist(p) < MIN_CENTER_SEP) {
                    continue PlaceCenters;
                }
            }
            M.placeCenter(p);
            List<Place> r = M.unmarkedContaining(p);
            M.markAll(r, 1);
            result.add(new Region(p, r));
        }

        return result;
    }

    /** Return a Region containing a largest unmarked galaxy in M whose
     *  center is in CANDIDATES, also removing all members of CANDIDATES
     *  that do not form the center of any unmarked galaxy. */
    private Region bestCenter(Model M, Collection<Place> candidates) {
        Region best = new Region(null, Collections.emptyList());
        for (Iterator<Place> it = candidates.iterator(); it.hasNext(); ) {
            Place c = it.next();
            Set<Place> region = M.maxUnmarkedRegion(c);
            if (region.isEmpty()) {
                it.remove();
            } else {
                if (region.size() > best.cells.size()) {
                    best.cells.clear();
                    best.cells.addAll(region);
                    best.center = c;
                }
            }
        }
        return best;
    }

    /** Create marked galaxies out of all unmarked cells in M, adding
     *  their centers.  */
    private void allotUnmarked(Model M) {
        HashSet<Place> centers = new HashSet<>();
        for (int x = 1; x < M.xlim() - 1; x += 1) {
            for (int y = 1; y < M.ylim() - 1; y += 1) {
                centers.add(pl(x, y));
            }
        }

        while (true) {
            Region r = bestCenter(M, centers);
            if (r.center == null) {
                break;
            }
            M.placeCenter(r.center);
            M.markAll(r.cells, 1);
        }
    }

    @Override
    public void setSeed(long seed) {
        _random.setSeed(seed);
    }

    /** A "proto-galaxy" containing a center and cells. */
    private static class Region {
        /** A new Region that ordinally has center CENTER0 and cells CELLS0. */
        Region(Place center0, List<Place> cells0) {
            center = center0;
            cells = new ArrayList<>(cells0);
        }

        /** The center of this region. */
        protected Place center;
        /** A set of cells symmetric around center. */
        protected ArrayList<Place> cells;
    }


    /** My PNRG. */
    private Random _random;

}
