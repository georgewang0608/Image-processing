package graphs;

import java.util.*;

public class ToposortDAGSolver<V> implements ShortestPathSolver<V> {
    private final Map<V, Edge<V>> edgeTo;
    private final Map<V, Double> distTo;
    private final V start;

    public ToposortDAGSolver(Graph<V> graph, V start) {
        this.edgeTo = new HashMap<>();
        this.distTo = new HashMap<>();
        this.start = start;
        Stack<V> perimeter = new Stack<>();
        Set<V> discovered = new HashSet<>();
        List<V> order = new ArrayList<>();
        edgeTo.put(start, null);
        distTo.put(start, 0.0);
        dfs(graph, start, order, discovered);
        Collections.reverse(order);
        for (V from : order) {
            for (Edge<V> edge : graph.neighbors(from)) {
                V to = edge.to();
                double oldDist = distTo.getOrDefault(to, Double.POSITIVE_INFINITY);
                double newDist = distTo.get(from) + edge.weight();
                if (newDist < oldDist) {
                distTo.put(to, newDist);
                edgeTo.put(to, edge);
                }
            }      // TODO: Your code here!
        }
    }

    public void dfs(Graph<V> graph, V curr, List<V> order, Set<V> discovered) {
        if(graph.neighbors(curr).size() == 0) {
            order.add(curr);
            return;
        } else if (!discovered.contains(curr)) {
            discovered.add(curr);
            for(Edge<V> e : graph.neighbors(curr)) {
                V to = e.to();
                dfs(graph, to, order, discovered);                                                                                                                                                                                      
            }
            order.add(curr); 
        }
    }
    // TODO: Your code here!

    public List<V> solution(V goal) {
        List<V> path = new ArrayList<>();
        V curr = goal;
        path.add(curr);
        while (edgeTo.get(curr) != null) {
            curr = edgeTo.get(curr).from();
            path.add(curr);
        }
        Collections.reverse(path);
        return path;
    }
}
