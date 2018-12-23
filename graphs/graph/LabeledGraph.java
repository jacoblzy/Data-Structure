package graph;

/* See restrictions in Graph.java. */

import java.util.ArrayList;

/** A Graph whose vertices are labeled with type VL and whose edges are
 *  labeled with type EL.
 *  @author P. N. Hilfinger
 */
public class LabeledGraph<VL, EL> extends GraphFilter {

    /** A labeling of the graph G.  Accessors and modifiers of the graph
     *  act upon G.  Attempts to modify the graph structure directly through
     *  G have undefined effects upon the labeled version created by this
     *  constructor. */
    public LabeledGraph(Graph G) {
        super(G);
    }

    /** Returns the label on vertex V, which must be one of my
     *  vertices. */
    public VL getLabel(int v) {
        checkMyVertex(v);
        return v < _vlabel.size() ? _vlabel.get(v) : null;
    }

    /** Returns the label on the edge (U, V), which must be one of
     *  my edges. */
    public EL getLabel(int u, int v) {
        int e = edgeId(u, v);
        if (e == 0) {
            throw new IllegalArgumentException("no such edge");
        }
        return e < _elabel.size() ? _elabel.get(e) : null;
    }

    /** Return the successor of vertex U along the edge labeled LAB, if any,
     *  and otherwise 0. Assumes LAB is not null. If multiple edges have the
     *  label EL, returns an arbitrary one of them. */
    public int getSuccessor(int u, EL lab) {
        for (int v : successors(u)) {
            if (lab.equals(getLabel(u, v))) {
                return v;
            }
        }
        return 0;
    }

    /** Set getVertexLabel(V) to LAB.  V must be one of my vertices. */
    public void setLabel(int v, VL lab) {
        checkMyVertex(v);
        if (lab != null || v < _vlabel.size()) {
            expand(_vlabel, v + 1);
            _vlabel.set(v, lab);
        }
    }

    /** Set getEdgeLabel(U, V) to LAB. (U, V) must be one of my edges. */
    public void setLabel(int u, int v, EL lab) {
        int e = edgeId(u, v);
        if (e == 0) {
            throw new IllegalArgumentException("no such edge");
        }
        if (lab != null || e < _elabel.size()) {
            expand(_elabel, e + 1);
            _elabel.set(e, lab);
        }
    }

    /** Returns a new vertex labeled LAB, and adds it to me with no
     *  incident edges. */
    public int add(VL lab) {
        int v = add();
        setLabel(v, lab);
        return v;
    }

    /** Adds an edge incident on U and V, labeled with LAB and returns
     *  the same value as for add(u, v). If I am directed,
     *  the edge is directed (leaves U and enters V). If there is already
     *  an edge (U, V), sets its label to EL. */
    public int add(int u, int v, EL lab) {
        int e = add(u, v);
        if (lab != null || e < _elabel.size()) {
            expand(_elabel, e + 1);
            _elabel.set(e, lab);
        }
        return e;
    }

    @Override
    public void remove(int v) {
        super.remove(v);
        if (v < _vlabel.size()) {
            _vlabel.set(v, null);
        }
    }

    @Override
    public void remove(int u, int v) {
        int e = edgeId(u, v);
        if (e != 0) {
            super.remove(u, v);
            if (e < _elabel.size()) {
                _elabel.set(e, null);
            }
        }
    }

    /** If necessary, add nulls to L to make its length N.  Has no effect if
     *  L's length is already at least N. */
    static void expand(ArrayList<?> L, int n) {
        while (L.size() < n) {
            L.add(null);
        }
    }

    /** Mapping of vertex numbers to vertex labels. */
    private final ArrayList<VL> _vlabel = new ArrayList<>();
    /** Mapping of unique edge ids to edge labels. */
    private final ArrayList<EL> _elabel = new ArrayList<>();
}
