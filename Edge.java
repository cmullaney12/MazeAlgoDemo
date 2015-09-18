import javalib.colors.*;
import javalib.worldimages.*;

// to represent an edge
class Edge {

    Node n1;
    Node n2;
    Double weight;

    Edge(Node n1, Node n2, Double weight) {
        this.n1 = n1;
        this.n2 = n2;
        this.weight = weight;
    }

    // draws this edge
    WorldImage drawEdge() {
        return new LineImage(new Posn((this.n1.x + 1) * Maze.SCALE,
                (this.n1.y + 1) * Maze.SCALE), new Posn((this.n2.x + 1)
                        * Maze.SCALE, (this.n2.y + 1) * Maze.SCALE), new Red());
    }
}
