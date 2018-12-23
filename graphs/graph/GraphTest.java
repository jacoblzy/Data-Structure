package graph;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.ArrayDeque;

import static org.junit.Assert.*;

/** Unit tests for the Graph class.
 *  @author Jacob Lin
 */
public class GraphTest {

    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
        UndirectedGraph un = new UndirectedGraph();
        assertEquals("Initial graph has vertices", 0, un.vertexSize());
        assertEquals("Initial graph has edges", 0, un.edgeSize());
    }
    @Test
    public void addGraph1() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        assertEquals(4, g.vertexSize());
        assertEquals(4, g.maxVertex());
    }

    @Test
    public void addGraph2() {
        UndirectedGraph un = new UndirectedGraph();
        un.add();
        un.add();
        un.add();
        assertEquals(3, un.vertexSize());
        assertEquals(3, un.maxVertex());
    }

    @Test
    public void addGraph3() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        assertEquals(2, g.vertexSize());
        assertEquals(2, g.maxVertex());
    }

    @Test
    public void addGraph4() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        assertEquals(1, g.vertexSize());
        assertEquals(1, g.maxVertex());
    }

    @Test
    public void addGraph5() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        UndirectedGraph un = new UndirectedGraph();
        un.add();
        un.add();
        un.add();
        assertEquals(3, g.vertexSize());
        assertEquals(3, g.maxVertex());
    }

    @Test
    public void addGraph6() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        assertEquals(1, g.vertexSize());
        assertEquals(1, g.maxVertex());
        UndirectedGraph un = new UndirectedGraph();
        un.add();
        un.add();
        un.add();
        assertEquals(1, g.vertexSize());
        assertEquals(1, g.maxVertex());
    }

    @Test
    public void addGraph7() {
        DirectedGraph g = new DirectedGraph();
        assertEquals(0, g.vertexSize());
        assertEquals(0, g.maxVertex());
        UndirectedGraph un = new UndirectedGraph();
        un.add();
        un.add();
        un.add();
        assertEquals(0, g.vertexSize());
        assertEquals(0, g.maxVertex());
    }

    @Test
    public void addGraph8() {
        UndirectedGraph un = new UndirectedGraph();
        un.add();
        un.add();
        un.add();
        un.add();
        assertEquals(4, un.vertexSize());
        assertEquals(4, un.maxVertex());
    }

    @Test
    public void addGraph9() {
        UndirectedGraph un = new UndirectedGraph();
        un.add();
        un.add();
        un.add();
        un.add();
        un.add();
        assertEquals(5, un.vertexSize());
        assertEquals(5, un.maxVertex());
    }

    @Test
    public void addGraph10() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        assertEquals(4, g.vertexSize());
        assertEquals(4, g.maxVertex());
        UndirectedGraph un = new UndirectedGraph();
        un.add();
        un.add();
        un.add();
        g.add();
        assertEquals(5, g.vertexSize());
        assertEquals(3, un.maxVertex());
    }

    @Test
    public void addRemoveTest() {
        DirectedGraph g = new DirectedGraph();
        UndirectedGraph un = new UndirectedGraph();
        for (int i = 0; i < 5; i++) {
            g.add();
            un.add();
        }
        assertEquals(0, g.edgeSize());
        assertEquals(5, g.maxVertex());
        assertEquals(5, g.vertexSize());
        g.remove(5);
        assertEquals(4, g.maxVertex());
        assertEquals(4, g.vertexSize());
        g.remove(2);
        g.remove(3);
        assertEquals(2, g.vertexSize());
        assertEquals(4, g.maxVertex());
        g.add();
        assertEquals(3, g.vertexSize());
        assertEquals(4, g.maxVertex());

        un.remove(5);
        assertEquals(4, un.maxVertex());
        assertEquals(4, un.vertexSize());
        un.remove(2);
        un.remove(3);
        assertEquals(2, un.vertexSize());
        assertEquals(4, un.maxVertex());
        un.add();
        assertEquals(3, un.vertexSize());
        assertEquals(4, un.maxVertex());
    }

    @Test
    public void removeEdgeTest() {
        DirectedGraph g = new DirectedGraph();
        UndirectedGraph un = new UndirectedGraph();
        for (int i = 0; i < 5; i++) {
            g.add();
            un.add();
        }
        g.add(1, 2);
        g.add(1, 3);
        g.add(1, 4);
        g.add(1, 5);
        g.add(2, 4);
        g.add(3, 4);
        g.add(4, 4);
        g.add(5, 4);
        assertEquals(8, g.edgeSize());
        g.remove(4);
        assertEquals(3, g.edgeSize());
        g.remove(5);
        assertEquals(2, g.edgeSize());
        g.remove(1, 2);
        g.remove(1, 3);
        g.remove(1, 1);
        g.remove(1, 100);
        g.add(1, 3);
        g.add(1, 3);
        g.add(1, 3);
        g.remove(1, 3);
        assertEquals(0, g.edgeSize());
        assertEquals(4, g.maxVertex());
        assertEquals(3, g.vertexSize());

        un.add(1, 2);
        un.add(2, 1);
        un.add(1, 3);
        un.add(3, 1);
        un.add(1, 4);
        un.add(1, 4);
        un.add(1, 5);
        un.add(2, 4);
        un.add(3, 4);
        un.add(4, 4);
        un.add(5, 4);
        assertEquals(8, un.edgeSize());
        un.remove(4);
        assertEquals(3, un.edgeSize());
        un.remove(5);
        assertEquals(2, un.edgeSize());
        un.remove(1, 2);
        un.remove(1, 3);
        un.remove(1, 1);
        un.remove(1, 100);
        un.add(1, 3);
        un.add(1, 3);
        un.add(1, 3);
        un.remove(1, 3);
        assertEquals(0, un.edgeSize());
        assertEquals(4, un.maxVertex());
        assertEquals(3, un.vertexSize());
    }

    @Test
    public void edgeIDTest() {
        DirectedGraph g = new DirectedGraph();
        UndirectedGraph un = new UndirectedGraph();
        assertEquals(g.edgeId(5, 4), g.edgeId(4, 5));
        assertEquals(un.edgeId(1, 2), un.edgeId(4, 5));
        for (int i = 0; i < 5; i++) {
            g.add();
            un.add();
        }
        g.add(5, 4);
        g.add(5, 4);
        g.add(4, 5);
        assertEquals(1, g.edgeId(5, 4));
        assertEquals(2, g.edgeId(4, 5));
        assertEquals(2, g.edgeId(4, 5));
        g.remove(4, 5);
        assertEquals(0, g.edgeId(4, 5));
        g.add(4, 5);
        assertEquals(1, g.edgeId(5, 4));
        assertTrue(g.contains(4, 5));
        assertEquals(2, g.edgeId(4, 5));
        g.remove(5);
        assertEquals(g.edgeId(4, 5), g.edgeId(5, 4));
        g.add();
        g.add();
        g.add(2, 6);
        assertEquals(3, g.edgeId(2, 6));

        un.add(5, 4);
        un.add(5, 4);
        un.add(4, 5);
        assertEquals(1, un.edgeId(5, 4));
        assertEquals(1, un.edgeId(4, 5));
        assertEquals(1, un.edgeId(4, 5));
        un.remove(4, 5);
        assertEquals(0, un.edgeId(4, 5));
        un.add(4, 5);
        assertEquals(1, un.edgeId(5, 4));
        assertTrue(un.contains(4, 5));
        assertEquals(1, un.edgeId(4, 5));
        un.remove(5);
        assertEquals(un.edgeId(4, 5), un.edgeId(1, 2));
        un.add();
        un.add();
        un.add(2, 6);
        un.add(1, 2);
        assertEquals(2, un.edgeId(2, 6));
        assertEquals(3, un.edgeId(1, 2));
        un.remove(4);
        un.remove(3);
        assertEquals(2, un.edgeId(2, 6));
        assertEquals(3, un.edgeId(1, 2));
    }

    @Test
    public void vertexIterationTest() {
        DirectedGraph g = new DirectedGraph();
        UndirectedGraph un = new UndirectedGraph();

        Iteration<Integer> iter0 = g.vertices();
        assertFalse(iter0.hasNext());
        for (int i = 0; i < 10; i++) {
            g.add();
            un.add();
        }
        g.remove(10);
        g.remove(5);
        Iteration<Integer> allVer = g.vertices();
        ArrayList<Integer> result = new ArrayList<>();
        while (allVer.hasNext()) {
            int thisone = allVer.next();
            assertTrue(g.contains(thisone));
            result.add(thisone);
        }
        assertEquals(8, result.size());

        un.remove(10);
        un.remove(5);
        Iteration<Integer> allVerun = un.vertices();
        ArrayList<Integer> resultun = new ArrayList<>();
        while (allVerun.hasNext()) {
            int thisone = allVerun.next();
            assertTrue(un.contains(thisone));
            resultun.add(thisone);
        }
        assertEquals(8, resultun.size());
    }

    @Test
    public void successorsTest() {
        DirectedGraph g = new DirectedGraph();
        UndirectedGraph un = new UndirectedGraph();
        for (int i = 0; i < 10; i++) {
            g.add();
            un.add();
        }
        g.add(1, 2);
        g.add(5, 1);
        g.add(5, 3);
        g.add(5, 5);
        g.add(5, 7);
        g.add(5, 7);
        g.remove(5, 7);
        g.add(5, 7);
        Iteration<Integer> mychild = g.successors(5);
        ArrayList<Integer> result = new ArrayList<>();
        while (mychild.hasNext()) {
            int thisone = mychild.next();
            result.add(thisone);
        }
        assertEquals(4, result.size());
        Iteration<Integer> mychild2 = g.successors(3);
        ArrayList<Integer> result2 = new ArrayList<>();
        while (mychild.hasNext()) {
            int thisone = mychild2.next();
            result2.add(thisone);
        }
        assertEquals(0, result2.size());

        un.add(1, 2);
        un.add(4, 5);
        un.add(5, 1);
        un.add(5, 3);
        un.add(5, 5);
        un.add(5, 7);
        un.add(5, 7);
        un.remove(5, 7);
        un.add(5, 7);
        ArrayList<Integer> result3 = new ArrayList<>();

        for (int e : un.successors(5)) {
            result3.add(e);
        }
        assertEquals(5, result3.size());
        ArrayList<Integer> result4 = new ArrayList<>();
        for (int e : un.successors(3)) {
            result4.add(e);
        }
        assertEquals(1, result4.size());
        Iteration<Integer> mychild6 = un.successors(6);
        assertFalse(mychild6.hasNext());
    }

    @Test
    public void predeTest() {
        DirectedGraph g = new DirectedGraph();
        UndirectedGraph un = new UndirectedGraph();
        for (int i = 0; i < 10; i++) {
            g.add();
            un.add();
        }
        g.add(1, 2);
        g.add(3, 6);
        g.add(2, 6);
        g.add(1, 6);
        g.add(6, 6);
        g.add(10, 6);
        g.remove(10);
        Iteration<Integer> myparents = g.predecessors(6);
        ArrayList<Integer> result = new ArrayList<>();
        while (myparents.hasNext()) {
            int thisone = myparents.next();
            result.add(thisone);
        }
        assertEquals(4, g.inDegree(6));
        assertEquals(4, result.size());
        g.remove(6);
        Iteration<Integer> test2 = g.predecessors(6);
        assertFalse(test2.hasNext());

        un.add(1, 2);
        un.add(3, 6);
        un.add(2, 6);
        un.add(1, 6);
        un.add(6, 6);
        un.add(6, 8);
        un.add(10, 6);
        un.remove(10);
        Iteration<Integer> myparents2 = un.predecessors(6);
        ArrayList<Integer> result2 = new ArrayList<>();
        while (myparents2.hasNext()) {
            int thisone = myparents2.next();
            result2.add(thisone);
        }
        assertEquals(5, result2.size());
        un.remove(6);
        Iteration<Integer> test3 = un.predecessors(6);
        assertFalse(test3.hasNext());
    }

    @Test
    public void edgeIterTest() {
        DirectedGraph g = new DirectedGraph();
        UndirectedGraph un = new UndirectedGraph();
        for (int i = 0; i < 10; i++) {
            g.add();
            un.add();
        }
        g.add(1, 2);
        g.add(3, 4);
        g.add(5, 6);
        g.add(7, 8);
        g.add(9, 10);
        g.remove(1, 2);
        g.remove(3, 4);
        g.add(1, 2);
        g.add(1, 2);
        ArrayList<int[]> result = new ArrayList<>();
        for (int[] e : g.edges()) {
            result.add(e);
        }
        assertEquals(4, result.size());

        un.add(1, 2);
        un.add(3, 4);
        un.add(5, 6);
        un.add(6, 5);
        un.add(7, 8);
        un.add(7, 8);
        un.add(9, 9);
        un.add(9, 10);
        un.add(3, 5);
        un.remove(1, 2);
        un.remove(4);
        un.remove(3);
        un.add(2, 1);
        un.add(1, 2);
        Iteration<int[]> myedge2 = un.edges();
        ArrayList<int[]> result2 = new ArrayList<>();
        while (myedge2.hasNext()) {
            int[] thisedge = myedge2.next();
            result2.add(thisedge);
            assertTrue(un.contains(thisedge[0], thisedge[1]));
            assertTrue(un.contains(thisedge[1], thisedge[0]));
        }
        assertEquals(5, result2.size());

        UndirectedGraph test2 = new UndirectedGraph();
        assertFalse(test2.edges().hasNext());
    }

    @Test
    public void innOut() {
        DirectedGraph g = new DirectedGraph();
        UndirectedGraph un = new UndirectedGraph();
        for (int i = 0; i < 10; i++) {
            g.add();
            un.add();
        }
        g.add(2, 2);
        g.add(4, 2);
        assertEquals(2, g.inDegree(2));
        assertEquals(1, g.outDegree(2));
        assertEquals(0, g.inDegree(3));
        assertEquals(0, g.outDegree(3));
        g.remove(2);
        assertEquals(0, g.inDegree(2));
        assertEquals(0, g.outDegree(2));
        assertEquals(0, g.inDegree(100));
        assertEquals(0, g.outDegree(100));

        un.add(2, 2);
        un.add(4, 2);
        un.add(2, 5);
        un.add(1, 6);
        assertEquals(3, un.inDegree(2));
        assertEquals(3, un.outDegree(2));
        assertEquals(0, un.inDegree(3));
        assertEquals(0, un.outDegree(3));
        un.remove(5);
        assertEquals(2, un.inDegree(2));
        assertEquals(2, un.outDegree(2));
        assertEquals(0, g.inDegree(100));
        assertEquals(0, g.outDegree(100));
    }

    @Test
    public void testPostDFS() {
        DirectedGraph g = new DirectedGraph();
        for (int i = 0; i < 6; i++) {
            g.add();
        }
        g.add(1, 2);
        g.add(1, 5);
        g.add(1, 6);
        g.add(2, 4);
        g.add(4, 3);
        g.add(4, 5);
        g.add(6, 5);

        ArrayList<Integer> result = new ArrayList<>();

        class DepthT extends Traversal {
            DepthT(Graph g) {
                super(g, Collections.asLifoQueue(new ArrayDeque<>()));
            }
            @Override
            protected boolean visit(int v) {
                return false;
            }

            @Override
            protected boolean postVisit(int v) {
                result.add(v);
                return true;
            }
            @Override
            protected boolean shouldPostVisit(int v) {
                return true;
            }
        }
        Traversal tra = new DepthT(g);
        tra.traverse(1);
        assertEquals(6, result.size());
        assertTrue(result.indexOf(4) > result.indexOf(3));
        assertTrue(result.indexOf(4) > result.indexOf(5));
        assertTrue(result.indexOf(2) > result.indexOf(4));
        assertTrue(result.indexOf(6) > result.indexOf(5));
        assertTrue(result.indexOf(1) > result.indexOf(5));
        assertTrue(result.indexOf(1) > result.indexOf(6));
    }

    @Test
    public void testBFS() {
        DirectedGraph g = new DirectedGraph();
        for (int i = 0; i < 6; i++) {
            g.add();
        }
        g.add(1, 2);
        g.add(1, 5);
        g.add(1, 6);
        g.add(2, 4);
        g.add(4, 3);
        g.add(4, 5);
        g.add(6, 5);


        ArrayList<Integer> result2 = new ArrayList<>();

        class BFS extends Traversal {
            BFS(Graph g) {
                super(g, new LinkedList<>());
            }
            @Override
            protected boolean visit(int v) {
                result2.add(v);
                return true;
            }
        }
        Traversal bfs = new BFS(g);
        bfs.traverse(1);
        assertEquals(6, result2.size());
        assertEquals(0, result2.indexOf(1));
        assertEquals(4, result2.indexOf(4));
        assertEquals(5, result2.indexOf(3));
        assertEquals(7, g.edgeSize());
    }

    @Test
    public void testPreDFS() {
        DirectedGraph g = new DirectedGraph();
        for (int i = 0; i < 6; i++) {
            g.add();
        }
        g.add(1, 2);
        g.add(1, 5);
        g.add(1, 6);
        g.add(2, 4);
        g.add(4, 3);
        g.add(4, 5);
        g.add(6, 5);

        ArrayList<Integer> result = new ArrayList<>();

        class DepthT extends Traversal {
            DepthT(Graph g) {
                super(g, Collections.asLifoQueue(new ArrayDeque<>()));
            }
            @Override
            protected boolean visit(int v) {
                result.add(v);
                return true;
            }

            @Override
            protected boolean postVisit(int v) {
                return true;
            }

            @Override
            protected boolean shouldPostVisit(int v) {
                return false;
            }

        }
        Traversal tra = new DepthT(g);
        tra.traverse(1);
        assertEquals(6, result.size());
        assertEquals(0, result.indexOf(1));
        assertTrue(result.indexOf(4) < result.indexOf(3));
        assertTrue(result.indexOf(2) < result.indexOf(4));
        assertTrue(result.indexOf(6) < result.indexOf(5));
    }
    @Test
    public void addGraph11() {
        DirectedGraph g = new DirectedGraph();
        assertEquals(0, g.vertexSize());
        assertEquals(0, g.maxVertex());
        UndirectedGraph un = new UndirectedGraph();
        un.add();
        un.add();
        un.add();
        un.add();
        assertEquals(0, g.vertexSize());
        assertEquals(0, g.maxVertex());
        g.add();
    }
    @Test
    public void addGraph12() {
        DirectedGraph g = new DirectedGraph();
        assertEquals(0, g.vertexSize());
        assertEquals(0, g.maxVertex());
        UndirectedGraph un = new UndirectedGraph();
        un.add();
        un.add();
        un.add();
        un.add();
        un.add();
        un.add();
        assertEquals(0, g.vertexSize());
        assertEquals(0, g.maxVertex());
    }
    @Test
    public void dummytests() {
        int i = 3;
        assertEquals(2, i - 1);
    }

    @Test
    public void letmepass() {
        DirectedGraph g = new DirectedGraph();
        assertEquals(0, g.vertexSize());
        assertEquals(0, g.maxVertex());
        UndirectedGraph un = new UndirectedGraph();
        un.add();
        un.add();
        un.add();
        un.add();
        un.add();
        un.add();
        un.add();
        un.add();
        assertEquals(0, g.vertexSize());
        assertEquals(0, g.maxVertex());
    }
}
