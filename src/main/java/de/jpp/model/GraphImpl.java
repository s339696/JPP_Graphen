package de.jpp.model;


import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;


import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class GraphImpl<N, A> implements Graph<N, A> {

    List<N> nodes = new ArrayList<>();
    Map<N, List<Edge<N, A>>> edges = new HashMap<>();

    /**
     * Ensures the node is part of this graph
     *
     * @param node the node to be added to this graph
     * @return true if this graph changed as a result of the call
     */
    @Override
    public boolean addNode(N node) {

        if (nodes.contains(node)) {
            return false;
        } else {
            nodes.add(node);
            edges.put(node, new ArrayList<>());
            return true;
        }
    }

    /**
     * Ensures that all nodes in this collection are part of this graph
     *
     * @param nodes the nodes to be added to this graph
     * @return true if this graph changed as a result of the call
     */
    @Override
    public boolean addNodes(Collection<? extends N> nodes) {
        boolean returnValue = false;
        for (N node : nodes) {
            if (addNode(node)) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * Ensures that all nodes in this collection are part of this graph
     *
     * @param nodes the nodes to be added to this graph
     * @return true if this graph changed as a result of the call
     */
    @Override
    public boolean addNodes(N... nodes) {
        boolean returnValue = false;
        for (N node : nodes) {
            if (addNode(node)) {
                returnValue = true;
            }
        }
        return returnValue;

    }

    public boolean addNodes(Stream<N> nodes){
        AtomicBoolean returnValue = new AtomicBoolean(false);
        nodes.peek(p -> {
            if (addNode(p)){
                returnValue.set(true);
            }
        });

        return returnValue.get();

    }

//    public boolean addNodes(Stream<Node>)

    /**
     * Returns all nodes in this graph
     *
     * @return all nodes in this graph
     */
    @Override
    public Collection<N> getNodes() {
        return new ArrayList<>(nodes);
    }

    /**
     * Ensures that a directed edge between the specified nodes are part of the graph <br>
     * If a node is not part of this graph, it will be added automatically
     *
     * @param start       the starting point of the edge
     * @param destination the destination point of this edge.
     * @param annotation  annotations to this edge
     * @return the instance of the newly created edge
     */
    @Override
    public Edge<N, A> addEdge(N start, N destination, Optional<A> annotation) {

        addNodes(start, destination);
        Edge<N, A> edge = new Edge<>(start, destination, annotation);
        List<Edge<N, A>> edgeList = edges.get(start);
        if (!edgeList.contains(edge)){
            edgeList.add(edge);
        }
//        edges.put(start, edgeList);

        return edge;
    }

    /**
     * Ensures that the specified edge is no longer part of this graph
     *
     * @param edge the edge to be removed
     * @return true if this operation changed the model of this graph
     */
    @Override
    public boolean removeEdge(Edge<N, A> edge) {
        if (edge == null){
            return false;
        }
        if (!edges.containsKey(edge.getStart())) {
            System.out.println("Start does not exist.");
            return false;
        } else {
            List<Edge<N, A>> edgeList = edges.get(edge.getStart());
            if (edgeList.contains(edge)) {
                edgeList.remove(edge);
                return true;
            } else {

                return false;
            }
        }
    }

    /**
     * Returns all Edges starting at the specified node.
     *
     * @param node the start node of every edge in this collection
     * @return every edge of the graph starting at this node
     */
    @Override
    public Collection<Edge<N, A>> getNeighbours(N node) {
        if (!edges.containsKey(node) || edges.get(node).isEmpty()){

                return new HashSet<>();

        }
        return new HashSet<>(edges.get(node));
    }

    /**
     * Returns all Edges ending at the specified node
     *
     * @param node the destination node of every edge in this collection
     * @return every edge of the graph ending at this node
     */
    @Override
    public Collection<Edge<N, A>> getReachableFrom(N node) {
        Collection<Edge<N, A>> reachableFromList = new ArrayList<Edge<N, A>>();
        Collection<List<Edge<N, A>>> listCollection = edges.values();
        for (List<Edge<N, A>> list : listCollection) {
            for (Edge<N, A> edge : list) {
                if (edge.getDestination().equals(node)) {
                    reachableFromList.add(edge);
                }
            }
        }
        return reachableFromList;
    }

    /**
     * Returns all edges in this graph
     *
     * @return all edges in this graph
     */
    @Override
    public Collection<Edge<N, A>> getEdges() {
        Collection<Edge<N, A>> allEdges = new ArrayList<>();
        for (List<Edge<N, A>> edges1 : edges.values()) {
            allEdges.addAll(edges1);
        }

        return allEdges;
    }

    /**
     * Ensures that the specified Node is no longer part of this graph <br>
     * Removes all edges containing the specified node.
     *
     * @param node the node to be deleted
     * @return true if this operation changed the model of this graph
     */
    @Override
    public boolean removeNode(N node) {
        if (!nodes.contains(node)) {
            return false;
        } else {
            nodes.remove(node);
            edges.remove(node);
            for (List<Edge<N, A>> edges1 : edges.values()) {
                Edge<N, A> refE = null;
                for (Edge<N, A> edge : edges1) {
                    if (edge.getDestination().equals(node)) {
                        refE = edge;

                    }
                }
                if (refE != null)
                    edges1.remove(refE);
            }


            return true;
        }
    }

    /**
     * Ensures that the specified Nodes are no longer part of this graph <br>
     * Removes all edges containing the specified nodes.
     *
     * @param nodes the nodes to be deleted
     * @return true if this operation changed the model of this graph
     */
    @Override
    public boolean removeNodes(Collection<? extends N> nodes) {
        boolean b = false;
        for (N node : nodes) {
            if (removeNode(node)) {
                b = true;
            }
        }

        return b;
    }

    public boolean removeNodes(Stream<N> nodes){
        AtomicBoolean returnValue = new AtomicBoolean(false);
        nodes.peek(p -> {
            if (removeNode(p)){
                returnValue.set(true);
            }
        });

        return returnValue.get();
    }

    /**
     * Ensures that the specified Nodes are no longer part of this graph <br>
     * Removes all edges containing the specified nodes.
     *
     * @param nodes the nodes to be deleted
     * @return true if this operation changed the model of this graph
     */
    @Override
    public boolean removeNodes(N... nodes) {
        boolean b = false;
        for (N node : nodes) {
            if (removeNode(node)) {
                b = true;
            }
        }

        return b;
    }

    /**
     * Ensures that no edges or nodes are part of this graph anymore.
     */
    @Override
    public void clear() {
        nodes.clear();
        edges.clear();
    }


    @Override
    public String toString() {
        return "GraphImpl{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }
//
//    @Override
//    public boolean equals(Object o) {
//
//
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        GraphImpl<?, ?> graph = (GraphImpl<?, ?>) o;
//        return nodes.size() == (graph.nodes.size()) &&
//                Objects.equals(edges.size(), graph.edges.size());
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphImpl<?, ?> graph = (GraphImpl<?, ?>) o;
        return Objects.equals(nodes, graph.nodes) &&
                Objects.equals(edges, graph.edges);
    }


//
//    @Override
//    public int hashCode() {
//        return Objects.hash(nodes, edges);
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        GraphImpl<?, ?> graph = (GraphImpl<?, ?>) o;
//        if (nodes.size() == graph.nodes.size() && edges.size() == graph.edges.size()) return true;
//        return nodes.equals(graph.nodes) &&
//                edges.equals(graph.edges);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, edges);
    }

    public static void main(String[] args) {
        Graph<String, String> graph = new GraphImpl<>();
        graph.addNode("Pisa");
        graph.addNodes("München", "Rom", "Frankfurt");
        System.out.println(graph.getNodes().toString());
        Edge<String, String> edge = new Edge<>("Lodon", "Paris", Optional.empty());
        graph.addEdge("Frankfurt", "Zürich", Optional.empty());
        graph.addEdge("Frankfurt", "Zürich", Optional.empty());

        graph.addEdge("Frankfurt", "Paris", Optional.empty());
        graph.addEdge("Paris", "Rom", Optional.empty());
        for (Edge<String, String> edge1 : graph.getEdges()) {
            System.out.println(edge1.getStart());
        }
        System.out.println(edge.getStart());
        System.out.println(graph.getNodes().toString());
        for (Edge<String, String> edge1 : graph.getEdges()) {
            System.out.println(edge1.getDestination());
        }
        System.out.println(graph.getNodes().toString());
        for (Edge<String, String> edge1 : graph.getNeighbours("Frankfurt")) {
            System.out.println(edge1.getDestination());
        }


    }
}


