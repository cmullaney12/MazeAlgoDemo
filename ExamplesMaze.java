import tester.*;

import java.util.ArrayList;

class ExamplesMaze {
    Maze m1 = new Maze();

    void testNodes(Tester t) {
        m1.initNodes();
        Node n1 = m1.nodes.get(10).get(50);
        Node n2 = m1.nodes.get(59).get(99);
        Node n3 = m1.nodes.get(0).get(0);
        t.checkExpect(n1.x, 50);
        t.checkExpect(n1.y, 10);
        t.checkExpect(n2.x, 99);
        t.checkExpect(n2.y, 59);
        t.checkExpect(n3.x, 0);
        t.checkExpect(n3.y, 0);
        t.checkExpect(n1.top, null);
        t.checkExpect(n2.left, null);
    }

    Maze m2 = new Maze();

    void testEdges(Tester t) {
        m2.initNodes();
        m2.initEdgesAllZero();
        Node n1 = m2.nodes.get(10).get(50);
        Node n2 = m2.nodes.get(9).get(50);
        Node n3 = m2.nodes.get(9).get(49);
        Node n4 = m2.nodes.get(0).get(0);
        Node n5 = m2.nodes.get(40).get(0);

        t.checkExpect(n1.top.n2.nodeEqual(n2), true);
        t.checkExpect(n2.left.n2.nodeEqual(n3), true);
        t.checkExpect(n3.top.weight, 0.0);
        t.checkExpect(m2.nodes.get(0).get(0).top, null);
        t.checkExpect(m2.nodes.get(0).get(0).left, null);
        t.checkExpect(n1.getOtherNode(n1.top) == n2, true);
        t.checkExpect(n2.getOtherNode(n2.bottom) == n1, true);
        t.checkExpect(n1.getConnectingEdge(n2) == n1.top, true);
        t.checkExpect(n2.getConnectingEdge(n1) == n1.top, true);
        t.checkExpect(n1.getConnectingEdge(n2) == n2.bottom, true);
        t.checkExpect(n2.getConnectingEdge(n1) == n2.bottom, true);
        t.checkExpect(n4.getConnectingEdge(n1) == null, true);
        t.checkExpect(n4.getConnectingEdge(n5) == null, true);
    }

    Maze m3 = new Maze();

    void testEdgeWeights(Tester t) {
        m3.initNodes();
        m3.initEdgesAllZero();
        m3.initEdgeWeights();
        Node n1 = m3.nodes.get(23).get(69);
        Node n2 = m3.nodes.get(22).get(69);
        Node n3 = m3.nodes.get(22).get(68);

        t.checkExpect(n1.top.n2.nodeEqual(n2), true);
        t.checkExpect(n2.left.n2.nodeEqual(n3), true);
        t.checkRange(n1.top.weight, 0.0, 10000.0);
        t.checkRange(n2.left.weight, 0.0, 10000.0);
    }

    void testMergeSort(Tester t) {
        Edge e1 = new Edge(null, null, 0.0);
        Edge e2 = new Edge(null, null, 0.1);
        Edge e3 = new Edge(null, null, 2.9);
        Edge e4 = new Edge(null, null, 3.4);
        Edge e5 = new Edge(null, null, 3.4);
        Edge e6 = new Edge(null, null, 5.6);
        Edge e7 = new Edge(null, null, 9.9);
        Edge e8 = new Edge(null, null, 10000.0);

        ArrayList<Edge> empty = new ArrayList<Edge>();
        ArrayList<Edge> edges = new ArrayList<Edge>();
        ArrayList<Edge> sorted = new ArrayList<Edge>();

        edges.add(e4);
        edges.add(e8);
        edges.add(e1);
        edges.add(e3);
        edges.add(e2);
        edges.add(e6);
        edges.add(e5);
        edges.add(e7);

        sorted.add(e1);
        sorted.add(e2);
        sorted.add(e3);
        sorted.add(e4);
        sorted.add(e5);
        sorted.add(e6);
        sorted.add(e7);
        sorted.add(e8);

        t.checkExpect(m1.mergeSort(empty), empty);
        t.checkExpect(m1.mergeSort(edges), sorted);
    }

    Maze m4 = new Maze();

    void testKruskal(Tester t) {
        m4.initNodes();
        m4.initEdgesAllZero();
        m4.initEdgeWeights();
        m4.sortEdges();
        m4.makeTree();
        m4.removeEdgesFromNodes();
        m4.worklist.add(m4.nodes.get(0).get(0));

        t.checkExpect(m4.kruskal.size(), (Maze.WIDTH * Maze.HEIGHT) - 1);
        this.m4.bigBang((Maze.WIDTH + 1) * Maze.SCALE, (Maze.HEIGHT + 1)
                * Maze.SCALE, 0.01);
    }

}