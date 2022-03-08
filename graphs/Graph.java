package graphs;

import java.util.*;

// Directed, edge-weighted graph.
@FunctionalInterface
public interface Graph<V> {

    // Returns a list of the outgoing edges from the given vertex.
    List<Edge<V>> neighbors(V vertex);
}
