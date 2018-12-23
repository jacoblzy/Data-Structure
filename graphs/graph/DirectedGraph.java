package graph;

import java.util.ArrayList;

/** Represents a general unlabeled directed graph whose vertices are denoted by
 *  positive integers. Graphs may have self edges.
 *
 *  @author Jacob Lin
 */
public class DirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public int inDegree(int v) {
        ArrayList<Node> masterL = getMasterL();
        int result = 0;
        for (int i = 0; i < masterL.size(); i++) {
            Node thisnode = masterL.get(i);
            if (thisnode.getNodeName() > 0
                && thisnode.getMychild().contains(v)) {
                result++;
            }
        }
        return result;
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        ArrayList<Node> masterL = getMasterL();
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < masterL.size(); i++) {
            Node thisnode = masterL.get(i);
            if (thisnode.getNodeName() > 0
                && thisnode.getMychild().contains(v)) {
                result.add(i);
            }
        }
        return Iteration.iteration(result);

    }
}
