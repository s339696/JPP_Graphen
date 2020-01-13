package de.jpp.model;

import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;
import de.jpp.model.interfaces.ObservableGraph;

import java.util.*;
import java.util.function.Consumer;

public class ObservableGraphImpl<N,A> implements ObservableGraph<N,A> {

    private Graph<N,A> graph = new GraphImpl<>();
    private List<Consumer<N>> nodeAddedListener = new ArrayList<>();
    private List<Consumer<N>> nodeRemovedListener = new ArrayList<>();
    private List<Consumer<Edge<N,A>>> edgeAddedListener = new ArrayList<>();
    private List<Consumer<Edge<N,A>>> edgeRemovedListener = new ArrayList<>();

    private List<Consumer<Collection<Edge<N, A>>>> neighboursListedListener = new ArrayList<>();
    private List<Consumer<Collection<Edge<N, A>>>> reachableListedListener = new ArrayList<>();
    private List<Consumer<Collection<Edge<N, A>>>> edgesListedListener = new ArrayList<>();
    private List<Consumer<Collection<N>>> nodesListedListener = new ArrayList<>();

    public ObservableGraphImpl() {

    }

    @Override
    public String toString() {
        String output = "ObservableGraphImpl{+" +
                "graph="+ graph+
                "nodes=";
        for (N node : graph.getNodes()) {
            output += node.toString() + ";";
        }
        output += "\nedges=";
        for ( Edge<N,A> edge: graph.getEdges()){
            output += edge.toString() + ";";
        }
        output +="}";

        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObservableGraphImpl<?, ?> that = (ObservableGraphImpl<?, ?>) o;
        if (getNodes().size() == graph.getNodes().size() && getEdges().size() == graph.getEdges().size()) return true;
        return graph.equals(that.graph);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph);
    }

    //    public ObservableGraphImpl(Graph<N, A> graph) {
//        this.graph = graph;
//    }

    /**
     * Adds the specified listener which is called whenever a node is added to the graph
     *
     * @param listener the listener
     */
    @Override
    public void addNodeAddedListener(Consumer<N> listener) {
        nodeAddedListener.add(listener);

    }

    /**
     * Adds the specified listener which is called whenever a node is removed from the graph
     *
     * @param listener the listener
     */
    @Override
    public void addNodeRemovedListener(Consumer<N> listener) {
        nodeRemovedListener.add(listener);

    }

    /**
     * Adds the specified listener which is called whenever a edge is added to the graph
     *
     * @param listener the listener
     */
    @Override
    public void addEdgeAddedListener(Consumer<Edge<N, A>> listener) {
        edgeAddedListener.add(listener);
    }

    /**
     * Adds the specified listener which is called whenever a edge is removed from the graph
     *
     * @param listener the listener
     */
    @Override
    public void addEdgeRemovedListener(Consumer<Edge<N, A>> listener) {
        edgeRemovedListener.add(listener);
    }

    /**
     * Removes the specified listener
     *
     * @param listener the listener
     */
    @Override
    public void removeNodeAddedListener(Consumer<N> listener) {
        nodeAddedListener.remove(listener);
    }

    /**
     * Removes the specified listener
     *
     * @param listener the listener
     */
    @Override
    public void removeNodeRemovedListener(Consumer<N> listener) {
        nodeRemovedListener.remove(listener);
    }

    /**
     * Removes the specified listener
     *
     * @param listener the listener
     */
    @Override
    public void removeEdgeAddedListener(Consumer<Edge<N, A>> listener) {
        edgeAddedListener.remove(listener);
    }

    /**
     * Removes the specified listener
     *
     * @param listener the listener
     */
    @Override
    public void removeEdgeRemovedListener(Consumer<Edge<N, A>> listener) {
        edgeRemovedListener.remove(listener);
    }

    /**
     * Adds the specified listener which is called whenever graph.getNeighbours(...) is called
     *
     * @param listener the listener
     */
    @Override
    public void addNeighboursListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        neighboursListedListener.add(listener);
    }

    /**
     * Adds the specified listener which is called whenever graph.getReachable(...) is called
     *
     * @param listener the listener
     */
    @Override
    public void addReachableListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        reachableListedListener.add(listener);
    }

    /**
     * Adds the specified listener which is called whenever graph.getNodes() is called
     *
     * @param listener the listener
     */
    @Override
    public void addNodesListedListener(Consumer<Collection<N>> listener) {
        nodesListedListener.add(listener);
    }

    /**
     * Adds the specified listener which is called whenever graph.getEdges() is called
     *
     * @param listener the listener
     */
    @Override
    public void addEdgesListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        edgesListedListener.add(listener);
    }

    /**
     * Removes the specified listener
     *
     * @param listener the listener
     */
    @Override
    public void removeNeighboursListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        neighboursListedListener.remove(listener);
    }

    /**
     * Removes the specified listener
     *
     * @param listener the listener
     */
    @Override
    public void removeReachableListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        reachableListedListener.remove(listener);
    }

    /**
     * Removes the specified listener
     *
     * @param listener the listener
     */
    @Override
    public void removeNodesListedListener(Consumer<Collection<N>> listener) {
        nodesListedListener.remove(listener);
    }

    /**
     * Removes the specified listener
     *
     * @param listener the listener
     */
    @Override
    public void removeEdgesListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        edgesListedListener.remove(listener);
    }

    /**
     * Ensures the node is part of this graph
     *
     * @param node the node to be added to this graph
     * @return true if this graph changed as a result of the call
     */
    @Override
    public boolean addNode(N node) {
        boolean b = graph.addNode(node);
        if (b){
            for ( Consumer<N> consumer : nodeAddedListener) {
                consumer.accept(node);
            }
        }

        return b;
    }

    /**
     * Ensures that all nodes in this collection are part of this graph
     *
     * @param nodes the nodes to be added to this graph
     * @return true if this graph changed as a result of the call
     */
    @Override
    public boolean addNodes(Collection<? extends N> nodes) {
        boolean b = false;
        for (N node : nodes){
            if (addNode(node)){
                b = true;
            }
            for (Consumer<Collection<N>> consumer : nodesListedListener){
                consumer.accept((Collection<N>) nodes);
            }
        }

        return b;
    }

    /**
     * Ensures that all nodes in this collection are part of this graph
     *
     * @param nodes the nodes to be added to this graph
     * @return true if this graph changed as a result of the call
     */
    @Override
    public boolean addNodes(N... nodes) {
        boolean b = false;
        for (N node : nodes){
            if (addNode(node)){
                b = true;
            }
            for (Consumer<Collection<N>> consumer : nodesListedListener){
                consumer.accept(Arrays.asList(nodes));
            }
        }

        return b;

    }

    /**
     * Returns all nodes in this graph
     *
     * @return all nodes in this graph
     */
    @Override
    public Collection<N> getNodes() {
        Collection<N> collection = graph.getNodes();
        for (Consumer<Collection<N>> consumer : nodesListedListener){
            consumer.accept(collection);
        }
        return collection;
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

        addNode(start);
        addNode(destination);
        Edge<N,A> edge = graph.addEdge(start, destination, annotation);
        for (Consumer<Edge<N, A>> consumer : edgeAddedListener){
            consumer.accept(edge);
        }

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
        boolean b = graph.removeEdge(edge);
        if (b){
            for (Consumer<Edge<N, A>> consumer : edgeRemovedListener){
                consumer.accept(edge);
            }
        }
        return b;
    }

    /**
     * Returns all Edges starting at the specified node.
     *
     * @param node the start node of every edge in this collection
     * @return every edge of the graph starting at this node
     */
    @Override
    public Collection<Edge<N, A>> getNeighbours(N node) {
        Collection<Edge<N,A>> neighbours = graph.getNeighbours(node);
        for ( Consumer<Collection<Edge<N, A>>> consumer : neighboursListedListener) {
            consumer.accept(neighbours);
        }

        return neighbours;
    }

    /**
     * Returns all Edges ending at the specified node
     *
     * @param node the destination node of every edge in this collection
     * @return every edge of the graph ending at this node
     */
    @Override
    public Collection<Edge<N, A>> getReachableFrom(N node) {
        Collection<Edge<N,A>> reachable = graph.getReachableFrom(node);
        for ( Consumer<Collection<Edge<N, A>>> consumer : reachableListedListener) {
            consumer.accept(reachable);
        }

        return reachable;
    }

    /**
     * Returns all edges in this graph
     *
     * @return all edges in this graph
     */
    @Override
    public Collection<Edge<N, A>> getEdges() {
        Collection<Edge<N, A>> edges = graph.getEdges();
        for (Consumer<Collection<Edge<N, A>>> consumer : edgesListedListener){
            consumer.accept(edges);
        }

        return edges;
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
        Collection<Edge<N,A>> collectionEdge = graph.getNeighbours(node);
            for ( Consumer<N> consumer : nodeRemovedListener) {
                consumer.accept(node);
            }

            if( collectionEdge != null){

            ArrayList<Edge<N,A>> list = new ArrayList<>();
            list.addAll(collectionEdge);

            for (Edge<N,A> edge : list){
                removeEdge(edge);
            }

            for (Edge<N,A> edge: graph.getReachableFrom(node)){
                removeEdge(edge);
            }
            }

        boolean b = graph.removeNode(node);


        return b;
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
       if (nodes != null){
        for (N node : nodes){
            if (removeNode(node)){
                b = true;
            }
        }

       }
        return b;

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
        for (N node : nodes){
            if (removeNode(node)){
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

        Collection<Edge<N,A>> collectionEdges = graph.getEdges();
        Collection<N> collectionNodes= graph.getNodes();
        for ( Edge<N,A> edge : collectionEdges) {
            removeEdge(edge);
        }
        for ( N node : collectionNodes) {
            removeNode(node);
        }
        graph.clear();
    }

//    public static void main(String[] args) {
//        Graph<String, String> observableGraph = new ObservableGraphImpl<>();
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("moin");
//        arrayList.add("was");
//        arrayList.add("geht");
//        arrayList.add("bei");
//        arrayList.add("dir");
//        observableGraph.addEdge("London", "Paris", Optional.empty());
//
//
//
////        observableGraph.addNode("?");
//      observableGraph.addNodes("moin", "alda", "alles", "klar");
//        observableGraph.removeNode("London");
//    }
}


