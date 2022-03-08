import graphs.*;

import java.util.*;

public class GenerativeSeamFinder implements SeamFinder {
    // Chao: So basically this is almost exactly the same as AdjacencyListSeam
    // I'll leave my insight to wtf i think all this code means

    // This solver is provided, so we aren't actually writing the graph traversing part 
    private final ShortestPathSolver.Constructor<Node> sps;

    // Constructor
    public GenerativeSeamFinder(ShortestPathSolver.Constructor<Node> sps) {
        this.sps = sps;
    }

    // This is exactly same as AdjacencyListSeam, should find and return seam w/ lowest energy
    public List<Integer> findSeam(Picture picture, EnergyFunction f) {
        PixelGraph graph = new PixelGraph(picture, f);
        List<Node> seam = sps.run(graph, graph.source).solution(graph.sink);
        seam = seam.subList(1, seam.size() - 1);
        List<Integer> result = new ArrayList<>(seam.size());
        for (Node pixel : seam) {
            result.add(((PixelGraph.Pixel) pixel).y);
        }
        return result;
    }

    // Here's the PixelGraph subclass, also almost exactly the same as it is in AdjacencyListSeam
    private class PixelGraph implements Graph<Node> {
        public final Picture picture;
        public final EnergyFunction f;
        // Except I removed 'pixels' as the spec says
        // 'pixels' was a 2d array that basically stored every single pixel and their neighbors
        // which is not great for memory (if u have a 3x3 picture, it would be a 9 element array,
        // with the 6 element on the left each storing 3 neighbors)

        public PixelGraph(Picture picture, EnergyFunction f) {
            this.picture = picture;
            this.f = f;
            // Since 'pixels' is removed, the chunk of code used to populate 'pixels' is also removed
            // So there's actually less code here lol
        }

        public List<Edge<Node>> neighbors(Node node) {
            return node.neighbors(picture, f);
        }

        // source is a node that doesn't actually represent a part of the picture
        // it has edges that connects to all the pixels on the left edge of the picture
        // so something like
        //         ___________________________  
        //        /|                         |
        //        /|                         |
        // source -|         picture         |
        //        \|                         |
        //        \|_________________________|
        //
        public final Node source = new Node() {
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                List<Edge<Node>> result = new ArrayList<>(picture.height());
                for (int j = 0; j < picture.height(); j += 1) {
                    Pixel to = new Pixel(0, j);
                    result.add(new Edge<>(this, to, f.apply(picture, 0, j)));
                }
                return result;
            }
        };

        public final Node sink = new Node() {
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                return List.of(); // Sink has no neighbors
            }
        };

        // neighbors, source and sink are just the same as well

        public class Pixel implements Node {
            public final int x;
            public final int y;
            // Here we remove the 'neighbors' variable as the spec says

            public Pixel(int x, int y) {
                this.x = x;
                this.y = y;
            }

            // But how would we get neighbors now?
            // Originally, the neighbors variable is populated in the PixelGraph's constructor
            // Now instead of returning the populated neighbors list, we return what would have been populated on demand
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                // Think of a horizontal seam going from left to right
                // Then each pixel has 3 neighbors, the pixel on it's top right, right and bottom right
                // ex.       / 100 (algo should choose this bc it has least energy i think)
                //       123 - 140
                //           \ 150
                // The x position of the neighbor pixel is the x of current pixel (i.e. this pixel) + 1
                // The y position ranges from this.y - 1 to this.y + 1, so we loop from -1 to 1
                List<Edge<Node>> result = new ArrayList<>();
                // We return empty if this pixel is at the right edge i think?
                if(this.x + 1 == picture.width()) {
                    Pixel from = new Pixel(this.x, this.y);
                    result.add(new Edge<>(from, sink, 0));
                    return result;
                    //return a new edge, use sink method
                    //return List.of();
                }
                //if (this.x == 0) {
                    //Pixel from = new Pixel(picture.width() - 1, this.y);
                    //result.add(new Edge<>(from, sink, 0));
                //}
                for (int j = -1; j <= 1; j ++) {

                    //in case pixel is top or bottom, then they only have 2 neighbors
                    if (this.y + j >= 0 && this.y + j < picture.height()) {
                        Pixel to = new Pixel(this.x + 1, this.y + j);
                        result.add(new Edge<>(this, to, f.apply(picture, this.x + 1, this.y + j)));
                    }
                }

                
                
                // I don't actually know if any of this code works at all, just commenting so i don't forget how any of this works
                return result;
            }

            public String toString() {
                return "(" + x + ", " + y + ")";
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                } else if (!(o instanceof Pixel)) {
                    return false;
                }
                Pixel other = (Pixel) o;
                return this.x == other.x && this.y == other.y;
            }

            public int hashCode() {
                return Objects.hash(x, y);
            }
        }
    }
}
