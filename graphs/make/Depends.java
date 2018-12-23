package make;

import graph.DirectedGraph;
import graph.LabeledGraph;

/** A directed, labeled subtype of Graph that describes dependencies between
 *  targets in a Makefile. The nodes correspond to Rules and edges out
 *  of rules are numbered to indicate the ordering of dependencies.
 *  @author Jacob Lin
 */
class Depends extends LabeledGraph<Rule, Integer> {
    /** An empty dependency graph. */
    Depends() {
        super(new DirectedGraph());
    }
}
