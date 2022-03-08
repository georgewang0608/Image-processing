package graphs;

import java.util.*;

public interface ShortestPathSolver<V> {

    // Returns the shortest path from the start vertex to the given goal vertex.
    List<V> solution(V goal);

    @FunctionalInterface
    interface Constructor<V> {
        ShortestPathSolver<V> run(Graph<V> graph, V start);
    }
}
