import javalib.colors.*;
import javalib.worldimages.*;
import java.util.ArrayList;

// to represent a node
class Node {
    int x;
    int y;
    Edge top;
    Edge left;
    Edge bottom;
    Edge right;

    Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.top = null;
        this.left = null;
        this.bottom = null;
        this.right = null;
    }

    // determines if this node is at the same
    // position as that node
    boolean nodeEqual(Node that) {
        return this.x == that.x && this.y == that.y;
    }

    // returns an arraylist of all the nodes that can be
    // reached from this node
    ArrayList<Node> getNeighbors() {
        ArrayList<Node> result = new ArrayList<Node>();

        if (this.right != null) {
            result.add(this.right.n1);
        }
        if (this.bottom != null) {
            result.add(this.bottom.n1);
        }
        if (this.left != null) {
            result.add(this.left.n2);
        }
        if (this.top != null) {
            result.add(this.top.n2);
        }
        return result;
    }

    // draws this node in the maze
    WorldImage drawNode() {
        Posn topLeft = new Posn((this.x + 1) * Maze.SCALE - Maze.SCALE / 2,
                (this.y + 1) * Maze.SCALE - Maze.SCALE / 2);
        Posn topRight = new Posn((this.x + 1) * Maze.SCALE + Maze.SCALE / 2,
                (this.y + 1) * Maze.SCALE - Maze.SCALE / 2);
        Posn botLeft = new Posn((this.x + 1) * Maze.SCALE - Maze.SCALE / 2,
                (this.y + 1) * Maze.SCALE + Maze.SCALE / 2);
        Posn botRight = new Posn((this.x + 1) * Maze.SCALE + Maze.SCALE / 2,
                (this.y + 1) * Maze.SCALE + Maze.SCALE / 2);
        WorldImage right = new LineImage(new Posn(0, 0), new Posn(0, 0),
                new Blue());
        WorldImage bottom = new LineImage(new Posn(0, 0), new Posn(0, 0),
                new Blue());
        WorldImage top = new LineImage(new Posn(0, 0), new Posn(0, 0),
                new Blue());
        WorldImage left = new LineImage(new Posn(0, 0), new Posn(0, 0),
                new Blue());

        // return new OverlayImages(new LineImage(topLeft, topRight, new
        // Blue()), new OverlayImages(new LineImage(topRight, botRight, new
        // Blue()),
        // new OverlayImages(new LineImage(botRight, botLeft, new Blue()), new
        // LineImage(botLeft, topLeft, new Blue()))));
        if (this.right == null) {
            right = new LineImage(topRight, botRight, new Blue());
        }
        if (this.bottom == null) {
            bottom = new LineImage(botLeft, botRight, new Blue());
        }
        if (this.top == null) {
            top = new LineImage(topLeft, topRight, new Blue());
        }
        if (this.left == null) {
            left = new LineImage(topLeft, botLeft, new Blue());
        }

        return new OverlayImages(top, new OverlayImages(right,
                new OverlayImages(bottom, left)));

    }

    // removes the given edge from this node
    void removeEdge(Edge e) {
        if (this.top == e) {
            this.top.n2.bottom = null;
            this.top = null;
        }
        else if (this.left == e) {
            this.left.n2.right = null;
            this.left = null;
        }
        else if (this.right == e) {
            this.right.n2.left = null;
            this.right = null;
        }
        else if (this.bottom == e) {
            this.bottom.n2.top = null;
            this.bottom = null;
        }
    }

    // returns the edge connecting this node
    // to that node
    Edge getConnectingEdge(Node that) {

        if ((this.right != null) && (this.right.n1.nodeEqual(that))) {
            return this.right;
        }
        else if ((this.bottom != null) && (this.bottom.n1.nodeEqual(that))) {
            return this.bottom;
        }

        else if ((this.left != null) && (this.left.n2.nodeEqual(that))) {
            return this.left;
        }
        else if ((this.top != null) && (this.top.n2.nodeEqual(that))) {
            return this.top;
        }
        else {
            return null;
        }
    }

    // finds the node that this node is connected to by that edge e
    Node getOtherNode(Edge e) {
        if (e.n1.nodeEqual(this)) {
            return e.n2;
        }
        else { // this node is e.n2
            return e.n1;
        }
    }
}