package galaxy;

import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import static galaxy.Place.pl;

/** Tests of the Model class.
 *  @author P. N. Hilfinger
 */
public class ModelTests {

    private Model _model;

    private String msg(String format, Object... args) {
        return String.format(format, args);
    }

    private void checkCell(int x, int y, char c, Model model) {
        switch (c) {
        case 'o': case 'O':
            assertTrue(msg("missing center at (%d,%d)", x, y),
                       model.isCenter(x, y));
            break;
        case ' ': case '*':
            assertFalse(msg("extra center at (%d,%d)", x, y),
                        model.isCenter(x, y));
            break;
        default:
            throw new IllegalArgumentException("bad 'expected'");
        }

        switch (c) {
        case ' ': case 'o':
            assertEquals(msg("extra mark at (%d,%d)", x, y),
                         0, model.mark(x, y));
            break;
        case '*': case 'O':
            assertTrue(msg("extra mark at (%d,%d)", x, y),
                       model.mark(x, y) > 0);
            break;
        default:
            throw new IllegalArgumentException("bad 'expected'");
        }
    }

    private void checkEdge(int x, int y, char c, Model model) {
        switch (c) {
        case '=': case 'I':
            assertTrue(msg("bad boundary at (%d,%d)", x, y),
                       model.isBoundary(x, y));
            assertFalse(msg("extra center at (%d,%d)", x, y),
                        model.isCenter(x, y));
            break;
        case '|': case '-':
            assertFalse(msg("bad edge at (%d,%d)", x, y),
                        model.isBoundary(x, y));
            assertFalse(msg("extra center at (%d,%d)", x, y),
                        model.isCenter(x, y));
            break;
        case 'o':
            assertFalse(msg("bad edge at (%d,%d)", x, y),
                        model.isBoundary(x, y));
            assertTrue(msg("missing center at (%d,%d)", x, y),
                       model.isCenter(x, y));
            break;
        case 'O':
            assertTrue(msg("bad boundary at (%d,%d)", x, y),
                       model.isBoundary(x, y));
            assertTrue(msg("missing center at (%d,%d)", x, y),
                       model.isCenter(x, y));
            break;
        case ' ':
            break;
        default:
            throw new IllegalArgumentException("bad 'expected'");
        }
    }

    /** Check that _model conforms to EXPECTED (an output from Model.toString,
     *  split into lines). */
    private void checkModel(String[] expected, Model model) {
        for (int y = 0; y < expected.length; y += 1) {
            String row = expected[expected.length - y - 1];
            for (int x = 0; x < row.length(); x += 1) {
                if (x % 2 == 1 && y % 2 == 1) {
                    checkCell(x, y, row.charAt(x), model);
                } else {
                    checkEdge(x, y, row.charAt(x), model);
                }
            }
        }
    }

    private void checkModel(String[] expected) {
        checkModel(expected, _model);
    }

    /** Set MODEL so that checkModel(DIAGRAM, MODEL) succeeds. */
    private void setModel(String[] diagram, Model model) {
        model.init(diagram[0].length() / 2, diagram.length / 2);
        for (int y = 1; y < diagram.length - 1; y += 1) {
            String row = diagram[diagram.length - y - 1];
            for (int x = 1; x < row.length() - 1; x += 1) {
                char c = row.charAt(x);
                if (c == 'o' || c == 'O') {
                    model.placeCenter(x, y);
                }
                if (x % 2 == 1 && y % 2 == 1) {
                    switch (c) {
                    case ' ': case 'o':
                        break;
                    case '*': case 'O':
                        model.mark(x, y, 1);
                        break;
                    default:
                        throw new IllegalArgumentException("bad diagram");
                    }
                } else if (x % 2 == 1 || y % 2 == 1) {
                    switch (c) {
                    case 'I': case '=': case 'O':
                        model.toggleBoundary(x, y);
                        break;
                    case '|' : case '-': case 'o':
                        break;
                    default:
                        throw new IllegalArgumentException("bad diagram");
                    }
                }
            }
        }
    }

    /** Return true iff the set of elements in EXPECTED is the same as that
     *  in ACTUAL. */
    private <T> void assertSetEquals(String msg,
                                     Collection<T> expected,
                                     Collection<T> actual) {
        assertNotNull(msg, actual);
        assertEquals(msg, new HashSet<T>(expected), new HashSet<T>(actual));
    }

    @Before
    public void setupModel() {
        _model = new Model(7, 7);
    }

    @Test
    public void paramTest() {
        assertEquals("xlim test", 15, _model.xlim());
        assertEquals("ylim test", 15, _model.ylim());
    }

    private static final String[] BLANK_BOARD = {
        " = = = = = = = ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " = = = = = = = "
    };

    @Test
    public void blankBoardTest() {
        checkModel(BLANK_BOARD);
    }

    private static final String[] CENTER_TEST = {
        " = = = = = = = ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - -o- - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - o - - - - - ",
        "Io| | o | | | I",
        " = = = = = = = "
    };

    @Test
    public void addCenterTest() {
        _model.placeCenter(1, 1);
        _model.placeCenter(3, 2);
        _model.placeCenter(6, 1);
        _model.placeCenter(8, 8);
        checkModel(CENTER_TEST);
    }

    private static final String[] BOUNDARY_TEST = {
        " = = = = = = = ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - = - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I I | | | | | I",
        " = = = = = = = "
    };

    @Test
    public void boundaryTest() {
        _model.toggleBoundary(2, 1);
        _model.toggleBoundary(5, 4);
        _model.toggleBoundary(7, 8);
        _model.toggleBoundary(7, 8);
        checkModel(BOUNDARY_TEST);
    }

    private static final String[] BOUNDARY_AND_CENTER_TEST = {
        " = = = = = = = ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - =o- - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " = O - - - - - ",
        "Io| | O | | | I",
        " = = = = = = = "
    };

    @Test
    public void boundaryAndCenterTest() {
        _model.placeCenter(1, 1);
        _model.placeCenter(3, 2);
        _model.placeCenter(6, 1);
        _model.placeCenter(8, 8);
        _model.toggleBoundary(1, 2);
        _model.toggleBoundary(3, 2);
        _model.toggleBoundary(6, 1);
        _model.toggleBoundary(7, 8);
        checkModel(BOUNDARY_AND_CENTER_TEST);
    }

    @Test
    public void centersTest() {
        _model.placeCenter(1, 1);
        _model.placeCenter(3, 2);
        _model.placeCenter(6, 1);
        _model.placeCenter(8, 8);
        List<Place> expected = asList(pl(1, 1), pl(3, 2), pl(6, 1), pl(8, 8));
        assertSetEquals("wrong list of centers", expected,
                        _model.centers());
        assertEquals("duplicate center", 4, _model.centers().size());
    }

    private static final String[] MARKS_TEST = {
        " = = = = = = = ",
        "I | | | | | |*I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | |*| | | | I",
        " - - - - - - - ",
        "I |*| | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I*| | | | | | I",
        " = = = = = = = "
    };

    @Test
    public void marksTest() {
        _model.mark(1, 1, 1);
        _model.mark(3, 5, 1);
        _model.mark(13, 13, 1);
        _model.mark(5, 7, 2);
        assertEquals("wrong mark at (1,1)", 1, _model.mark(1, 1));
        assertEquals("wrong mark at (5,7)", 2, _model.mark(5, 7));
        checkModel(MARKS_TEST);
    }

    private static final String[] MARKS_AND_CENTERS_TEST = {
        " = = = = = = = ",
        "I | | | | | |OI",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | |O| | | | I",
        " - - - - - - - ",
        "I |O| | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "IO| | | | | | I",
        " = = = = = = = "
    };

    @Test
    public void marksAndCentersTest() {
        _model.mark(1, 1, 1);
        _model.mark(3, 5, 1);
        _model.mark(13, 13, 1);
        _model.mark(5, 7, 2);
        _model.placeCenter(1, 1);
        _model.placeCenter(3, 5);
        _model.placeCenter(13, 13);
        _model.placeCenter(5, 7);
        checkModel(MARKS_AND_CENTERS_TEST);
    }

    private static final String[] BIG_BLANK_BOARD = {
        " = = = = = = = = = = ",
        "I | | | | | | | | | I",
        " - - - - - - - - - - ",
        "I | | | | | | | | | I",
        " - - - - - - - - - - ",
        "I | | | | | | | | | I",
        " - - - - - - - - - - ",
        "I | | | | | | | | | I",
        " - - - - - - - - - - ",
        "I | | | | | | | | | I",
        " - - - - - - - - - - ",
        "I | | | | | | | | | I",
        " - - - - - - - - - - ",
        "I | | | | | | | | | I",
        " - - - - - - - - - - ",
        "I | | | | | | | | | I",
        " = = = = = = = = = = "
    };

    @Test
    public void initTest() {
        _model.placeCenter(1, 1);
        _model.toggleBoundary(1, 2);
        _model.init(10, 8);
        assertEquals("Bad width dimension for reinitialized board.",
                     21, _model.xlim());
        assertEquals("Bad height dimension for reinitialized board.",
                     17, _model.ylim());
        checkModel(BIG_BLANK_BOARD);
    }

    @Test public void copyTest() {
        _model.placeCenter(1, 1);
        _model.placeCenter(3, 2);
        _model.placeCenter(6, 1);
        _model.placeCenter(8, 8);
        _model.toggleBoundary(1, 2);
        _model.toggleBoundary(3, 2);
        _model.toggleBoundary(6, 1);
        _model.toggleBoundary(7, 8);

        Model model1 = new Model(_model);
        checkModel(BOUNDARY_AND_CENTER_TEST, model1);

        model1.mark(5, 5, 1);
        model1.placeCenter(2, 1);
        checkModel(BOUNDARY_AND_CENTER_TEST);
    }


    @Test
    public void opposingTest() {
        assertEquals(pl(3, 9), _model.opposing(pl(7, 11), pl(11, 13)));
        assertEquals(pl(5, 9), _model.opposing(pl(8, 11), pl(11, 13)));
        assertEquals(pl(11, 3), _model.opposing(pl(11, 8), pl(11, 13)));
        assertEquals(pl(5, 7), _model.opposing(pl(8, 10), pl(11, 13)));

        assertNull(_model.opposing(pl(8, 10), pl(1, 3)));
        assertNull(_model.opposing(pl(8, 10), pl(9, 10)));
    }

    @Test
    public void unmarkedContainingTest() {
        assertSetEquals("wrong results for (3,3)",
                        asList(pl(3, 3)), _model.unmarkedContaining(pl(3, 3)));
        assertSetEquals("wrong results for (3,4)",
                        asList(pl(3, 5), pl(3, 3)),
                        _model.unmarkedContaining(pl(3, 4)));
        assertSetEquals("wrong results for (4,3)",
                        asList(pl(5, 3), pl(3, 3)),
                        _model.unmarkedContaining(pl(4, 3)));
        assertSetEquals("wrong results for (4,6)",
                        asList(pl(3, 5), pl(5, 5), pl(3, 7), pl(5, 7)),
                        _model.unmarkedContaining(pl(4, 6)));

        _model.mark(3, 3, 1);
        assertSetEquals("wrong results for (3,3) w/mark",
                        asList(), _model.unmarkedContaining(pl(3, 3)));
        assertSetEquals("wrong results for (4,3) w/mark",
                        asList(), _model.unmarkedContaining(pl(4, 3)));
        assertSetEquals("wrong results for (3,4) w/mark",
                        asList(), _model.unmarkedContaining(pl(3, 4)));
        assertSetEquals("wrong results for (4,4) w/mark",
                        asList(), _model.unmarkedContaining(pl(4, 4)));
    }

    @Test
    public void unmarkedSymAdjTest() {
        List<Place> test =
            asList(pl(7, 1), pl(7, 3), pl(7, 5), pl(7, 7), pl(9, 3), pl(5, 5));
        for (Place p : test) {
            _model.mark(p, 1);
        }
        _model.mark(7, 1, 0);
        _model.mark(7, 7, 0);

        assertSetEquals("wrong symmetric, adjacent cells",
                        asList(pl(5, 1), pl(9, 1), pl(5, 3), pl(11, 3),
                               pl(3, 5), pl(9, 5), pl(5, 7), pl(9, 7),
                               pl(7, 1), pl(7, 7)),
                        _model.unmarkedSymAdjacent(pl(7, 4), test));
    }

    private static final String[] SOLVED_PUZZLE = {
        " = = = = = = = ",
        "I | | | | I I I",
        " - - o - - o - ",
        "I | | | | I I I",
        " = = = = = = - ",
        "I | I I | I | I",
        " -o- o - - - - ",
        "I | I I | I o I",
        " = = = - - - - ",
        "I | | I o I | I",
        " - - - - - - = ",
        "I |o| I | I I I",
        " - - - - - - o ",
        "I | | I | I I I",
        " = = = = = = = "
    };

    @Test
    public void solvedTest() {
        assertFalse(_model.solved());
        setModel(CENTER_TEST, _model);
        assertFalse(_model.solved());
        setModel(SOLVED_PUZZLE, _model);
        assertTrue(_model.solved());
    }

    private static final String[] REGIONS_TEST = {
        " = = = = = = = ",
        "I | | | | I I I",
        " - - o - - o - ",
        "I | | | | I I I",
        " - - = = = = - ",
        "I | I I | I | I",
        " - - o - - - - ",
        "I | I I | I o I",
        " = = = - - - - ",
        "I | | I o I | I",
        " - - - = - - = ",
        "I |o| I | I I I",
        " - - - - - - - ",
        "I |o| I | I I I",
        " = = = = = = = "
    };

    @Test
    public void findGalaxyTest() {
        setModel(REGIONS_TEST, _model);
        assertSetEquals("find simple two-cell region",
                        asList(pl(11, 11), pl(11, 13)),
                        _model.findGalaxy(pl(11, 12)));
        assertSetEquals("find simple two-cell region, center not in centers()",
                        asList(pl(13, 1), pl(13, 3)),
                        _model.findGalaxy(pl(13, 2)));
        assertNull("can't be off-center()", _model.findGalaxy(pl(13, 1)));
        assertNull(_model.findGalaxy(pl(5, 12)));
        assertSetEquals("find irregular region",
                        asList(pl(13, 13), pl(13, 11), pl(13, 9), pl(13, 7),
                               pl(13, 5), pl(11, 9), pl(11, 7), pl(11, 5),
                               pl(11, 3), pl(11, 1)),
                        _model.findGalaxy(pl(12, 7)));
        assertNull("reject region with additional center",
                   _model.findGalaxy(pl(3, 3)));
        assertNull("reject region with stray boundary",
                   _model.findGalaxy(pl(8, 5)));
    }

    private static final String[] UNMARKED_REGIONS_TEST = {
        " = = = = = = = ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I | | | | | | I",
        " - - - - - - - ",
        "I |*| | | |*| I",
        " - - - = - - - ",
        "I |o| | | | |*I",
        " - - - - - - - ",
        "I | | | |*| |*I",
        " = = = = = = = "
    };

    @Test
    public void maxUnmarkedTest() {
        setModel(UNMARKED_REGIONS_TEST, _model);
        assertSetEquals("find max unmarked region; "
                        + "ignore centers and boundaries",
                        asList(pl(5, 1), pl(7, 1),
                               pl(3, 3), pl(5, 3), pl(7, 3), pl(9, 3),
                               pl(11, 3),
                               pl(7, 5), pl(9, 5)),
                        _model.maxUnmarkedRegion(pl(7, 3)));
    }
}
