package graphs;

import java.util.*;

// A weighted directed edge.
public class Edge<V> {
    private final V from;
    private final V to;
    private final double weight;

    public Edge(V from, V to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public V from() {
        return from;
    }

    public V to() {
        return to;
    }

    public double weight() {
        return weight;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Edge)) {
            return false;
        }
        Edge other = (Edge) o;
        return Objects.equals(this.from, other.from) && Objects.equals(this.to, other.to)
            && Double.compare(this.weight, other.weight) == 0;
    }

    public int hashCode() {
        return Objects.hash(from, to, weight);
    }
}
