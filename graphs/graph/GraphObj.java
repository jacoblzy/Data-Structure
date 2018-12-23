package graph;

import java.util.ArrayList;

/** A partial implementation of Graph containing elements common to
 *  directed and undirected graphs.
 *
 *  @author Jacob Lin
 */
abstract class GraphObj extends Graph {

    /** A new, empty Graph. */
    GraphObj() {
        numVer = 0;
        arrayList2D = new ArrayList<>();
        arrayList2D.add(new Node(0));
        allMyEdge = new ArrayList<>();
    }

    @Override
    public int vertexSize() {
        return numVer;
    }

    @Override
    public int maxVertex() {
        return arrayList2D.size() - 1;
    }

    @Override
    public int edgeSize() {
        int edgenum = 0;
        for (int i = 0; i < arrayList2D.size(); i++) {
            if (arrayList2D.get(i).nodeName != -1) {
                edgenum += arrayList2D.get(i).mychild.size();
                if (!isDirected() && contains(i, i)) {
                    edgenum++;
                }
            }
        }
        if (isDirected()) {
            return edgenum;
        } else {
            return edgenum / 2;
        }
    }

    @Override
    public abstract boolean isDirected();

    @Override
    public int outDegree(int v) {
        if (contains(v)) {
            return arrayList2D.get(v).mychild.size();
        } else {
            return 0;
        }
    }

    @Override
    public abstract int inDegree(int v);

    @Override
    public boolean contains(int u) {
        return u >= 1 && u <= maxVertex() && arrayList2D.get(u).nodeName == u;
    }

    @Override
    public boolean contains(int u, int v) {
        return contains(u) && arrayList2D.get(u).mychild.contains(v);
    }

    @Override
    public int add() {
        for (int i = 1; i < arrayList2D.size(); i++) {
            if (arrayList2D.get(i).nodeName == 0) {
                arrayList2D.get(i).nodeName = i;
                arrayList2D.get(i).mychild = new ArrayList<>();
                numVer++;
                return i;
            }
        }
        arrayList2D.add(new Node(arrayList2D.size()));
        numVer++;
        return arrayList2D.size() - 1;
    }

    @Override
    public int add(int u, int v) {
        checkMyVertex(u);
        checkMyVertex(v);
        if (!arrayList2D.get(u).mychild.contains(v)) {
            arrayList2D.get(u).mychild.add(v);
            if (!isDirected() && u != v) {
                arrayList2D.get(v).mychild.add(u);
            }
        }
        if (!isDirected() && u > v) {
            int swap = v;
            v = u;
            u = swap;
        }
        for (int i = 0; i < allMyEdge.size(); i++) {
            Edge thisone = allMyEdge.get(i);
            if (u == thisone.start && v == thisone.end) {
                return edgeId(u, v);
            }
        }
        Edge thisedge = new Edge(u, v);
        allMyEdge.add(thisedge);
        return edgeId(u, v);
    }

    @Override
    public void remove(int v) {
        if (!contains(v)) {
            return;
        }
        if (v == maxVertex()) {
            arrayList2D.remove(v);
            numVer--;
        } else {
            arrayList2D.get(v).nodeName = 0;
            arrayList2D.get(v).mychild.clear();
            numVer--;
        }
        for (int i = 0; i < arrayList2D.size(); i++) {
            remove(i, v);
        }
    }

    @Override
    public void remove(int u, int v) {
        if (!contains(u, v)) {
            return;
        }
        arrayList2D.get(u).mychild.remove((Integer) v);
        if (!isDirected() && contains(v, u)) {
            arrayList2D.get(v).mychild.remove((Integer) u);
        }
    }

    @Override
    public Iteration<Integer> vertices() {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < arrayList2D.size(); i++) {
            int thisname = arrayList2D.get(i).nodeName;
            if (thisname > 0) {
                result.add(thisname);
            }
        }
        return Iteration.iteration(result);
    }

    @Override
    public Iteration<Integer> successors(int v) {
        Iterable<Integer> result = new ArrayList<>();
        if (contains(v)) {
            result = arrayList2D.get(v).mychild;
        }
        return Iteration.iteration(result);
    }

    @Override
    public abstract Iteration<Integer> predecessors(int v);

    @Override
    public Iteration<int[]> edges() {
        if (isDirected()) {
            ArrayList<int[]> result = new ArrayList<>();
            for (int i = 0; i < allMyEdge.size(); i++) {
                int u = allMyEdge.get(i).start;
                int v = allMyEdge.get(i).end;
                int[] thisarray = new int[]{u, v};
                if (contains(u, v)) {
                    result.add(thisarray);
                }
            }
            return Iteration.iteration(result);
        } else {
            ArrayList<int[]> result = new ArrayList<>();
            for (int i = 0; i < arrayList2D.size(); i++) {
                Node thisnode = arrayList2D.get(i);
                ArrayList<Integer> myneighbour = thisnode.mychild;
                if (thisnode.nodeName > 0) {
                    for (int j = 0; j < myneighbour.size(); j++) {
                        int neighbour = myneighbour.get(j);
                        if (i <= neighbour) {
                            result.add(new int[]{i, neighbour});
                        }
                    }
                }
            }
            return Iteration.iteration(result);
        }

    }

    @Override
    protected int edgeId(int u, int v) {
        if (!contains(u, v)) {
            return 0;
        }
        if (isDirected()) {
            for (int i = 0; i < allMyEdge.size(); i++) {
                Edge thisone = allMyEdge.get(i);
                if (u == thisone.start && v == thisone.end) {
                    return i + 1;
                }
            }
            return 0;
        } else {
            if (u > v) {
                return edgeId(v, u);
            }
            for (int i = 0; i < allMyEdge.size(); i++) {
                Edge thisone = allMyEdge.get(i);
                if (u == thisone.start && v == thisone.end) {
                    return i + 1;
                }
            }
            return 0;
        }
    }
    /** Return package-private arrayList2D. */
    ArrayList<Node> getMasterL() {
        return arrayList2D;
    }
    /** Return package-private allMyEdge. */
    ArrayList<Edge> getAllMyEdge() {
        return allMyEdge;
    }
    /** Return package-private numVer. */
    int getNumVer() {
        return numVer;
    }
    /** Return the number of my vertices. */
    private int numVer;
    /** An ArrayList to represent my graph. */
    private ArrayList<Node> arrayList2D;
    /** An ArrayList to store all my edges. */
    private ArrayList<Edge> allMyEdge;
    /** My private Node class. */
    protected class Node {
        /** Return my nodename. */
        private int nodeName;
        /** Return mychild. */
        private ArrayList<Integer> mychild;
        /** A new Node named NUM. */
        private Node(int num) {
            this.nodeName = num;
            mychild = new ArrayList<>();
        }
        /** Return my nodename. */
        int getNodeName() {
            return nodeName;
        }
        /** Return mychild. */
        ArrayList<Integer> getMychild() {
            return mychild;
        }
    }
    /** My private Edge class. */
    protected class Edge {
        /** Starting vertex of an edge. */
        private int start;
        /** Ending vertex of an edge. */
        private int end;
        /** A new edge with S and E. */
        private Edge(int s, int e) {
            this.start = s;
            this.end = e;
        }
        /** Return my starting point. */
        int getStart() {
            return start;
        }
        /** Return my ending point. */
        int getEnd() {
            return end;
        }
    }
}
