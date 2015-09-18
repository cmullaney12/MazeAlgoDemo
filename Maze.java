// Assignment 10
// Eric Murphy
// ecmurphy
// Colin Mullaney
// mullaney

import java.util.ArrayList;
import java.util.HashMap;

import javalib.funworld.World;
import javalib.colors.*;
import javalib.worldimages.*;

// to represent the mazeWorld
class Maze extends World {

    static final int HEIGHT = 60;
    static final int WIDTH = 100;
    static final int SCALE = 10;
    ArrayList<ArrayList<Node>> nodes;
    ArrayList<Edge> edges;
    ArrayList<Edge> kruskal;
    boolean pause = false;

    Node user;
    // if true uses a breadth first search,
    // else uses depth first search
    boolean breadthFirst = false;
    ArrayList<Node> visited = new ArrayList<Node>();
    HashMap<Node, Edge> cameFromEdge = new HashMap<Node, Edge>();
    ArrayList<Node> worklist = new ArrayList<Node>();

    // creates all the nodes in the maze
    void initNodes() {
        ArrayList<ArrayList<Node>> result = new ArrayList<ArrayList<Node>>();

        for (int row = 0; row < Maze.HEIGHT; row += 1) {

            ArrayList<Node> rowN = new ArrayList<Node>();

            for (int col = 0; col < Maze.WIDTH; col += 1) {
                rowN.add(new Node(col, row));
            }
            result.add(rowN);
        }
        // sets "user node" as the first node at (0,0)
        this.user = result.get(0).get(0);
        this.nodes = result;
    }

    // creates all the edges in the maze, starting their weights at 0
    void initEdgesAllZero() {
        ArrayList<Edge> result = new ArrayList<Edge>();

        for (int row = 0; row < Maze.HEIGHT; row += 1) {
            for (int col = 0; col < Maze.WIDTH; col += 1) {

                Node curNode = this.nodes.get(row).get(col);

                if (col == 0 && row == 0) {
                    // do nothing
                }
                else if (col == 0) {
                    // only set top edge
                    Node topNode = this.nodes.get(row - 1).get(col);
                    curNode.top = new Edge(curNode, topNode, 0.0);
                    topNode.bottom = curNode.top;
                    result.add(curNode.top);
                }
                else if (row == 0) {
                    // only set left edge
                    Node leftNode = this.nodes.get(row).get(col - 1);
                    curNode.left = new Edge(curNode, leftNode, 0.0);
                    leftNode.right = curNode.left;
                    result.add(curNode.left);
                }
                else {
                    // set both edges
                    Node topNode = this.nodes.get(row - 1).get(col);
                    Node leftNode = this.nodes.get(row).get(col - 1);
                    curNode.top = new Edge(curNode, topNode, 0.0);
                    topNode.bottom = curNode.top;
                    curNode.left = new Edge(curNode, leftNode, 0.0);
                    leftNode.right = curNode.left;
                    result.add(curNode.top);
                    result.add(curNode.left);
                }
            }
        }
        this.edges = result;
    }

    // Sets all the edge weights to a random value between 0 and 10000
    void initEdgeWeights() {
        for (Edge e : this.edges) {
            e.weight += Math.random() * 10000.0;
        }
    }

    // sorts the given ArrayList<Edge>
    ArrayList<Edge> mergeSort(ArrayList<Edge> edges) {
        if (edges.size() > 1) {
            ArrayList<Edge> left = new ArrayList<Edge>();
            ArrayList<Edge> right = new ArrayList<Edge>();

            for (int index = 0; index < edges.size() / 2; index += 1) {
                left.add(edges.get(index));
            }
            for (int num = edges.size() / 2; num < edges.size(); num += 1) {
                right.add(edges.get(num));
            }
            return this.merge(this.mergeSort(left), this.mergeSort(right));
        }
        else {
            return edges;
        }
    }

    // merges two sorted ArrayLists, returning one sorted array list
    ArrayList<Edge> merge(ArrayList<Edge> left, ArrayList<Edge> right) {
        ArrayList<Edge> result = new ArrayList<Edge>();

        while (left.size() > 0 && right.size() > 0) {
            if (left.get(0).weight <= right.get(0).weight) {
                result.add(left.remove(0));
            }
            else {
                result.add(right.remove(0));
            }
        }
        if (left.size() == 0) {
            for (Edge e : right) {
                result.add(e);
            }
        }
        else if (right.size() == 0) {
            for (Edge e : left) {
                result.add(e);
            }
        }
        return result;
    }

    // sorts all the edges in ascending order
    void sortEdges() {
        this.edges = this.mergeSort(this.edges);
    }

    // creates the maze using kruskals algorithm
    void makeTree() {

        HashMap<Node, Node> reps = new HashMap<Node, Node>();

        // initializes every node's representative to itself
        for (ArrayList<Node> row : this.nodes) {
            for (Node n : row) {
                reps.put(n, n);
            }
        }

        ArrayList<Edge> worklist = new ArrayList<Edge>();
        for (Edge e : this.edges) {
            worklist.add(e);
        }
        ArrayList<Edge> kruskalTree = new ArrayList<Edge>();

        while (kruskalTree.size() < (Maze.WIDTH * Maze.HEIGHT) - 1) {
            // Pick the next cheapest edge of the graph: suppose it connects X
            // and Y.
            Edge nextEdge = worklist.remove(0);

            if (find(reps, nextEdge.n1).nodeEqual(find(reps, nextEdge.n2))) {
                // do nothing because the two nodes
                // are already connected to the same tree
            }

            else {
                // else add edge to minimum spanning tree
                // and union the representatives for each node
                kruskalTree.add(nextEdge);
                reps = this.union(reps, this.find(reps, nextEdge.n1),
                        find(reps, nextEdge.n2));
            }
        }
        this.kruskal = kruskalTree;

    }

    Node find(HashMap<Node, Node> representatives, Node n) {
        Node repN = representatives.get(n);
        if (n.nodeEqual(repN)) {
            return n;
        }
        else {
            return find(representatives, repN);
        }
    }

    HashMap<Node, Node> union(HashMap<Node, Node> reps, Node n1, Node n2) {
        // set the value of one representative’s representative to the other
        reps.remove(n2);
        reps.put(n2, n1);
        return reps;
    }

    // Removes the edges not in the kruskal solution from their nodes
    void removeEdgesFromNodes() {
        for (Edge e : this.edges) {
            if (!this.kruskal.contains(e)) {
                Node thisNode = e.n1;
                thisNode.removeEdge(e);
            }
        }
    }

    // moves the player character on key presses
    void moveUser(String ke) {
        Node n = this.user;
        if (ke.equals("left") && (n.left != null)) {
            this.user = this.nodes.get(n.y).get(n.x - 1);
        }
        if (ke.equals("right") && (n.right != null)) {
            this.user = this.nodes.get(n.y).get(n.x + 1);
        }
        if (ke.equals("up") && (n.top != null)) {
            this.user = this.nodes.get(n.y - 1).get(n.x);
        }
        if (ke.equals("down") && (n.bottom != null)) {
            this.user = this.nodes.get(n.y + 1).get(n.x);
        }
    }

    // draws the player character
    WorldImage drawUser() {
        return new RectangleImage(new Posn((this.user.x + 1) * Maze.SCALE,
                (this.user.y + 1) * Maze.SCALE), Maze.SCALE, Maze.SCALE,
                new Red());
    }

    // updates the world based on key inputs
    public World onKeyEvent(String ke) {
        Maze next = this;
        if (ke.equals("b")) {
            this.breadthFirst = true;
        }
        if (ke.equals("d")) {
            this.breadthFirst = false;
        }
        if (ke.equals("p")) {
            this.pause = !this.pause;
        }
        this.moveUser(ke);
        return next;
    }

    // returns the image of the maze
    public WorldImage makeImage() {
        WorldImage result = new RectangleImage(new Posn(0, 0), Maze.WIDTH
                * Maze.SCALE, Maze.HEIGHT * Maze.SCALE, new White());

        if (this.visited.size() > 0) {
            result = new OverlayImages(result, this.drawVisited());
        }

        for (ArrayList<Node> row : this.nodes) {
            WorldImage rowImage = new RectangleImage(new Posn(0, 0), 0, 0,
                    new Blue());
            for (Node n : row) {
                rowImage = new OverlayImages(rowImage, n.drawNode());
            }
            result = new OverlayImages(result, rowImage);
        }
        result = new OverlayImages(result, this.drawUser());

        // for (Edge e: this.kruskal) {
        // result = new OverlayImages(result, e.drawEdge());
        // }

        return result;
    }

    // draws all the visited nodes in the maze
    WorldImage drawVisited() {
        WorldImage result = new LineImage(new Posn(0, 0), new Posn(0, 0),
                new White());
        for (Node n : this.visited) {
            WorldImage square = new RectangleImage(new Posn((n.x + 1)
                    * Maze.SCALE, (n.y + 1) * Maze.SCALE), 10, 10, new Green());
            result = new OverlayImages(result, square);
        }
        return result;
    }

    // draws the solution to the maze
    WorldImage drawFinalPath(ArrayList<Node> path) {
        WorldImage result = new LineImage(new Posn(0, 0), new Posn(0, 0),
                new White());
        for (Node n : path) {
            WorldImage square = new RectangleImage(new Posn((n.x + 1)
                    * Maze.SCALE, (n.y + 1) * Maze.SCALE), 10, 10, new Black());
            result = new OverlayImages(result, square);
        }
        return result;
    }

    // ticks the world to the next step
    public Maze onTick() {

        Maze next = this;
        if (!this.visited.contains(this.nodes.get(Maze.HEIGHT - 1).get(
                Maze.WIDTH - 1))
                && !this.pause) {
            this.search();
        }
        return next;
    }

    // performs a binary search or depth search through the maze,
    // based on the current state of the boolean this.breadthFirst.
    void search() {


        // gets next node in worklist, and removes it
        Node next = this.worklist.remove(0);
        if (this.visited.contains(next)) {
            this.search();
        }

        else {
            this.visited.add(next);
            for (Node n : next.getNeighbors()) {
                if (this.breadthFirst) {
                    this.worklist.add(n);
                }
                if (!this.breadthFirst) {
                    this.worklist.add(0, n);
                }
                if (this.cameFromEdge.get(n) == null) {
                    this.cameFromEdge.put(n, next.getConnectingEdge(n));
                }
            }
        }

    }

    // reconstructs the solution to the maze
    ArrayList<Node> reconstruct(HashMap<Node, Edge> path, Node curNode,
            ArrayList<Node> answer) {

        Node source = this.nodes.get(0).get(0);

        if (curNode.nodeEqual(source)) {
            return answer;
        }
        else {

            Edge backEdge = path.get(curNode);
            answer.add(curNode);
            return this.reconstruct(path, curNode.getOtherNode(backEdge),
                    answer);

        }

    }

    // game state for when the maze is solved
    public WorldEnd worldEnds() {
        WorldImage bg = new OverlayImages(this.makeImage(), new RectangleImage(
                new Posn(10, 10), Maze.SCALE, Maze.SCALE, new Black()));

        Node end = this.nodes.get(Maze.HEIGHT - 1).get(Maze.WIDTH - 1);
        if (this.visited.contains(end)) {

            WorldImage correctPath = this.drawFinalPath(this.reconstruct(
                    this.cameFromEdge, end, new ArrayList<Node>()));
            return new WorldEnd(true, new OverlayImages(bg, correctPath));
        }
        if (this.user.nodeEqual(end)) {
            return new WorldEnd(true, bg);
        }
        else {
            return new WorldEnd(false, bg);
        }
    }
}
