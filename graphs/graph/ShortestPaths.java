package graph;

/* See restrictions in Graph.java. */

import java.util.TreeSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/** The shortest paths through an edge-weighted graph.
 *  By overrriding methods getWeight, setWeight, getPredecessor, and
 *  setPredecessor, the client can determine how to represent the weighting
 *  and the search results.  By overriding estimatedDistance, clients
 *  can search for paths to specific destinations using A* search.
 *  @author Jacob Lin
 */
public abstract class ShortestPaths {

    /** The shortest paths in G from SOURCE. */
    public ShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /** A shortest path in G from SOURCE to DEST. */
    public ShortestPaths(Graph G, int source, int dest) {
        _G = G;
        _source = source;
        _dest = dest;
        allWeight = new double[_G.maxVertex() + 1];
        allPredecessor = new int[_G.maxVertex() + 1];
    }

    /** Initialize the shortest paths.  Must be called before using
     *  getWeight, getPredecessor, and pathTo. */
    public void setPaths() {
        for (int i = 0; i <= _G.maxVertex(); i++) {
            if (_G.contains(i)) {
                if (i == _source) {
                    setWeight(i, 0);
                } else {
                    setWeight(i, Double.POSITIVE_INFINITY);
                }
                allPredecessor[i] = 0;
                myTreeSet.add(new Pairss(i, getWeight(i)));
            }
        }
        while (!myTreeSet.isEmpty()) {
            Pairss ver = myTreeSet.pollFirst();
            int v = ver.name;
            if (v == _dest && _dest != 0) {
                return;
            }
            for (int w : _G.successors(v)) {
                Pairss wer = findNode(w);
                if (wer != null) {
                    double wei = getWeight(v, w);
                    if (ver.weight + wei < wer.weight) {
                        myTreeSet.remove(wer);
                        wer.weight = ver.weight + wei;
                        setWeight(w, wer.weight);
                        setPredecessor(w, v);
                        myTreeSet.add(new Pairss(wer.name, wer.weight));
                    }
                }
            }
        }
    }
    /** Return a node with node name W. */
    private Pairss findNode(int w) {
        for (Pairss thisone : myTreeSet) {
            if (thisone.name.equals(w)) {
                return thisone;
            }
        }
        return null;
    }

    /** Returns the starting vertex. */
    public int getSource() {
        return _source;
    }

    /** Returns the target vertex, or 0 if there is none. */
    public int getDest() {
        return _dest;
    }

    /** Returns the current weight of vertex V in the graph.  If V is
     *  not in the graph, returns positive infinity. */
    public abstract double getWeight(int v);

    /** Set getWeight(V) to W. Assumes V is in the graph. */
    protected abstract void setWeight(int v, double w);

    /** Returns the current predecessor vertex of vertex V in the graph, or 0 if
     *  V is not in the graph or has no predecessor. */
    public abstract int getPredecessor(int v);

    /** Set getPredecessor(V) to U. */
    protected abstract void setPredecessor(int v, int u);

    /** Returns an estimated heuristic weight of the shortest path from vertex
     *  V to the destination vertex (if any).  This is assumed to be less
     *  than the actual weight, and is 0 by default. */
    protected double estimatedDistance(int v) {
        return 0.0;
    }

    /** Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     *  not in the graph, returns positive infinity. */
    protected abstract double getWeight(int u, int v);

    /** Returns a list of vertices starting at _source and ending
     *  at V that represents a shortest path to V.  Invalid if there is a
     *  destination vertex other than V. */
    public List<Integer> pathTo(int v) {
        if ((_dest > 0 && v != _dest) || v == 0) {
            throw new IllegalArgumentException("Not a valid path");
        }
        ArrayList<Integer> result = new ArrayList<>();
        while (v != 0) {
            result.add(0, v);
            v = getPredecessor(v);
        }
        return result;
    }


    /** Returns a list of vertices starting at the source and ending at the
     *  destination vertex. Invalid if the destination is not specified. */
    public List<Integer> pathTo() {
        return pathTo(getDest());
    }
    /** Returns allWeight. */
    double[] getAllWeight() {
        return allWeight;
    }
    /** Returns allPredecessor. */
    int[] getAllPredecessor() {
        return allPredecessor;
    }
    /** The graph being searched. */
    protected final Graph _G;
    /** The starting vertex. */
    private final int _source;
    /** The target vertex. */
    private final int _dest;
    /** An array to record total min distance from the start point. */
    private double[] allWeight;
    /** An array to record each node's predecessor. */
    private int[] allPredecessor;
    /** A treeSet acting like a priority queue. */
    private TreeSet<Pairss> myTreeSet = new TreeSet<>(new MyComparator<>());
    /** Custom my treeSet comparator. */
    protected class MyComparator<Items> implements Comparator<Items> {
        @Override
        public int compare(Object o1, Object o2) {
            Pairss p1 = (Pairss) o1;
            Pairss p2 = (Pairss) o2;
            int temp = (p1.total).compareTo(p2.total);
            if (temp != 0) {
                return temp;
            } else {
                return p1.name.compareTo(p2.name);
            }
        }
    }
    /** Pair classes to be compared in the treeSet. */
    protected class Pairss {
        /** Name of a pair. */
        private Integer name;
        /** Weight of a pair. */
        private Double weight;
        /** Heuristic value from the current node. */
        private Double estimate;
        /** Sum of weight and heuristic value. */
        private Double total;
        /** New Pairss with MYNAME and MYWEIGHT. */
        Pairss(int myName, double myWeight) {
            this.name = myName;
            this.weight = myWeight;
            this.estimate = estimatedDistance(name);
            this.total = this.weight + this.estimate;
        }
        /** Return PairsName. */
        protected int getPairsName() {
            return name;
        }
        /** Return PairsWeight. */
        protected Double getPairsWeight() {
            return weight;
        }
        /** Return Estimation. */
        protected Double getEstimate() {
            return estimate;
        }
        /** Return sum of weight and estimation. */
        protected Double getTotal() {
            return total;
        }
    }

}
