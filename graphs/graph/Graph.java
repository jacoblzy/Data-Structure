package graph;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  Do not add or remove public classes.
 *
 * You may make changes that don't affect the API (much) as seen from outside
 * the graph package:
 *   + You may make methods in GraphObj abstract, if you want different
 *     implementations in DirectedGraph and UndirectedGraph.
 *   + You may add bodies to abstract methods, modify existing bodies,
 *     or override inherited methods.
 *   + You may change parameter names, or add 'final' modifiers to parameters.
 *   + You may add private and package private members.
 *   + You may add additional non-public classes to the graph package.
 */

/** Represents a general unlabeled graph whose vertices are denoted by
 *  positive integers.  A graph may be directed or undirected.  For
 *  an undirected graph, outgoing and incoming edges are the same.
 *  Graphs may have self edges, but no multi-edges (edges with the same
 *  end points).
 *
 *  @author P. N. Hilfinger
 */
public abstract class Graph {

    /** Returns the number of vertices in me. */
    public abstract int vertexSize();

    /** Returns my maximum vertex number, or 0 if I am empty. */
    public abstract int maxVertex();

    /** Returns the number of edges in me. */
    public abstract int edgeSize();

    /** Returns true iff I am a directed graph. */
    public abstract boolean isDirected();

    /** Returns the number of outgoing edges incident to V, or 0 if V is not
     *  one of my vertices. */
    public abstract int outDegree(int v);

    /** Returns the number of incoming edges incident to V, or 0 if V is not
     *  one of my vertices. */
    public abstract int inDegree(int v);

    /** Returns outDegree(V). This is simply a synonym, intended for
     *  use in undirected graphs. */
    public final int degree(int v) {
        return outDegree(v);
    }

    /** Returns true iff U is one of my vertices. */
    public abstract boolean contains(int u);

    /** Returns true iff U and V are my vertices and I have an edge (U, V). */
    public abstract boolean contains(int u, int v);

    /** Returns a new vertex and adds it to me with no incident edges.
     *  The vertex number is always the smallest integer >= 1 that is not
     *  currently one of my vertex numbers.  */
    public abstract int add();

    /** Add an edge incident on U and V. If I am directed, the edge is
     *  directed (leaves U and enters V).  Requires that U and V are my
     *  vertices.  Has no effect if there is already an edge from U to
     *  V.  Returns a unique positive number identifying the edge,
     *  different from those returned for any other existing edge. */
    public abstract int add(int u, int v);

    /** Remove V, if present, and all adjacent edges. */
    public abstract void remove(int v);

    /** Remove edge (U, V) from me, if present. */
    public abstract void remove(int u, int v);

    /** Returns an Iteration over all vertices in numerical order. */
    public abstract Iteration<Integer> vertices();

    /** Returns an iteration over all successors of V.
     *  Empty if V is not my vertex. */
    public abstract Iteration<Integer> successors(int v);

    /** Returns an iteration over all predecessors of V.
     *  Empty if V is not my vertex. */
    public abstract Iteration<Integer> predecessors(int v);

    /** Returns successors(V).  This is a synonym typically used on
     *  undirected graphs. */
    public final Iteration<Integer> neighbors(int v) {
        return successors(v);
    }

    /** Returns an iteration over all edges in me.  Edges are returned
     *  as two-element arrays (u, v), which are directed if the graph
     *  is.  The values in the array returned by .next() may have changed
     *  on the next call of .next() (that is, .next() is free to use the same
     *  array to return all results). */
    public abstract Iteration<int[]> edges();

    /* Non-public methods for internal use. */

    /** Throw exception if V is not one of my vertices. */
    protected void checkMyVertex(int v) {
        if (!contains(v)) {
            throw new IllegalArgumentException("vertex not from Graph");
        }
    }

    /** Returns a unique positive identifier for the edge (U, V), if it
     *  is present, or 0 otherwise.  If edges are not removed from the graph,
     *  this value should be a small multiple of the number of the edges in
     *  the graph. Its purpose is to provide a mapping of edges to integers
     *  for use by classes such as LabeledGraph. It is the same value as that
     *  returned by add(u, v). */
    protected abstract int edgeId(int u, int v);

}
