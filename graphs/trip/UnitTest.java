package trip;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your trip package per se (that is, it must be
 * possible to remove them and still have your package work). */

import org.junit.Test;
import ucb.junit.textui;
import static org.junit.Assert.*;

/** Unit tests for the trip package. */
public class UnitTest {

    /** Run all JUnit tests in the graph package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(trip.UnitTest.class));
    }

    @Test
    public void dummyTest() {
        int i = 2;
        assertEquals(3, i + 1);
    }

}
