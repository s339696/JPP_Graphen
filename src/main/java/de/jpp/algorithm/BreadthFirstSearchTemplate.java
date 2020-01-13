package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.*;
import de.jpp.factory.SearchStopFactory;
import de.jpp.model.GraphImpl;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;

import java.util.*;

public abstract class BreadthFirstSearchTemplate<N, A, G extends Graph<N, A>> implements SearchAlgorithm<N, A, G> {

    private boolean stopped = false;
    private G graph;
    private N start;
    private SearchResultImpl<N, A> result;


    public BreadthFirstSearchTemplate(G graph, N start) {
        this.graph = graph;
        this.start = start;

        result = new SearchResultImpl<>();

    }

    public NodeInformation<N, A> getNodeInformation(Edge<N, A> edge, double d) {


        return null;
    }

    public void openIfShorter(N node, NodeInformation<N, A> information) {

    }

    /**
     * Starts the search process. Stops the search with the specified strategy
     *
     * @param type
     * @return
     */
    @Override
    public SearchResult<N, A> findPaths(SearchStopStrategy<N> type) {
        this.stopped = false;
        Queue<N> queue = new LinkedList<>();
        for (N node : graph.getNodes()) {
            result.setUnkown(node);
        }


        queue.offer(start);
        result.close(start, new NodeInformation<>(null, 0));
        if (type.stopSearch(start) || stopped) {
            System.out.println("lol");
        } else {

            while (!queue.isEmpty()) {
                N currentNode = queue.poll();
                result.setClosed(currentNode);
                        if (type.stopSearch(currentNode) || stopped) {
                            return result;
                        }
                for (Edge<N, A> edge : graph.getNeighbours(currentNode)) {

                    if (result.getNodeStatusMap().get(edge.getDestination()) == NodeStatus.UNKOWN) {
                        queue.offer(edge.getDestination());



                        if (edge.getAnnotation().isPresent()){
                            result.open(edge.getDestination(), new NodeInformation<N,A>(edge, 1));
                        } else {
                            result.open(edge.getDestination(), new NodeInformation<>(edge, 1));
                        }
                        if (type.stopSearch(edge.getDestination())){
                            return result;
                        }
                    }
                }
            }


        }


//        while (!stopped){
//
//            for (Edge<N,A> edge: graph.getNeighbours(currentNode)){
//
//                if (result.getNodeStatusMap().get(edge.getDestination()) == NodeStatus.UNKOWN){
//
//                    result.getNodeStatusMap().put(edge.getDestination(), NodeStatus.OPEN);
//                    queue.add(edge.getDestination());
//                    result.getNodeInformationMap().put(edge.getDestination(), new NodeInformation<N,A>(edge, result.getNodeInformationMap().get(currentNode).getDistance()+1));
//
//                }
//            }
//
//            queue.remove(currentNode);
//            result.getNodeStatusMap().put(currentNode, NodeStatus.CLOSED);
//            if (queue.size()>0){
////                currentNode = queue.get(0);
//            } else{
//                stopped = true;
//            }
//        }

        return result;

    }


    public static void main(String[] args) {
        Graph<String, String> graph = new GraphImpl<>();
        graph.addNode("Pisa");
        graph.addNodes("München", "Rom", "Frankfurt");
        System.out.println(graph.getNodes().toString());
        Edge<String, String> edge = new Edge<>("Lodon", "Paris", Optional.empty());
        graph.addEdge("Frankfurt", "Zürich", Optional.empty());
        graph.addEdge("Frankfurt", "Timi", Optional.empty());

        graph.addEdge("Frankfurt", "Paris", Optional.empty());
        graph.addEdge("Paris", "Rom", Optional.empty());


        BreadthFirstSearch<String, String, Graph<String, String>> search = new BreadthFirstSearch<>(graph, "Frankfurt");
        SearchResult<String, String> result = search.findPaths(new SearchStopFactory().expandAllNodes());
        System.out.println(result.toString());
    }

    /**
     * Starts the search process with a non-stopping search strategy
     *
     * @return the result of the search algorithm
     */
    @Override
    public SearchResult<N, A> findAllPaths() {
        SearchStopFactory factory = new SearchStopFactory();
        return findPaths(factory.expandAllNodes());
    }

    /**
     * Returns the observable search result so listener can be added before executing the search
     *
     * @return the observable search result
     */
    @Override
    public ObservableSearchResult<N, A> getSearchResult() {
        return result;
    }

    /**
     * Returns the start node of this search
     *
     * @return the start node of this search
     */
    @Override
    public N getStart() {
        return start;
    }

    /**
     * Returns the graph on which to search
     *
     * @return the graph on which to search
     */
    @Override
    public G getGraph() {
        return graph;
    }

    /**
     * This method stops the search. No further nodes will be added to the open or closed list, the searchResult won´t change anymore and no listener must be called after this method
     */
    @Override
    public void stop() {
        stopped = true;
    }


}
