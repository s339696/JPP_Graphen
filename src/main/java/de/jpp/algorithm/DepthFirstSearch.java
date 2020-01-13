package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.*;
import de.jpp.factory.SearchStopFactory;
import de.jpp.model.GraphImpl;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;

import java.util.*;

public class DepthFirstSearch<N, A, G extends Graph<N, A>> implements SearchAlgorithm<N, A, G> {

    private boolean stopped = false;
    private G graph;
    private N start;
    private SearchResultImpl<N, A> result;


    public DepthFirstSearch(G graph, N start) {
        this.graph = graph;
        this.start = start;
        this.stopped = false;

        result = new SearchResultImpl<>();

//        result = new SearchResultImpl<>();
//        for (N node : graph.getNodes()) {
//            result.setOpen(node);
//        }
    }

    /**
     * Starts the search process. Stops the search with the specified strategy
     *
     * @param type
     * @return
     */
    @Override
    public SearchResult<N, A> findPaths(SearchStopStrategy<N> type) {
        for (N node : graph.getNodes()) {
            result.setUnkown(node);
        }

            Stack<N> stack = new Stack<>();
            Stack<Edge<N, A>> edges = new Stack<>();


            stack.add(start);
            result.setOpen(start);
            result.close(start, new NodeInformation<>(null, 0));


            while (!stack.empty()) {
                N currentNode = stack.pop();


                if (type.stopSearch(currentNode) || stopped) {
                    result.setClosed(currentNode);
                    return result;
                }
                if (!currentNode.equals(start)){
                    result.setClosed(currentNode);
                }

                for (Edge<N, A> edge : graph.getNeighbours(currentNode)) {
                    if (result.getNodeStatusMap().get(edge.getDestination()) == NodeStatus.UNKOWN) {
                        stack.push(edge.getDestination());
//                        edges.push(edge);
                        result.open(edge.getDestination(), new NodeInformation<>(edge, 1));

                    }
                }
            }

        return result;
    }

    public static void main(String[] args) {

        Graph<String, String> graph1 = new GraphImpl<>();
        graph1.addNode("Pisa");
        graph1.addNodes("München", "Rom", "Frankfurt");
        System.out.println(graph1.getNodes().toString());
        Edge<String, String> edge = new Edge<>("Lodon", "Paris", Optional.empty());
        graph1.addEdge("Frankfurt", "Zürich", Optional.empty());
        graph1.addEdge("Frankfurt", "Timi", Optional.empty());

        graph1.addEdge("Frankfurt", "Paris", Optional.empty());
        graph1.addEdge("Timi", "Rom", Optional.empty());


        DepthFirstSearch<String, String, Graph<String, String>> search = new DepthFirstSearch<>(graph1, "Frankfurt");
        SearchResult<String, String> result = search.findPaths(new SearchStopFactory().maxNodeCount(3));


        System.out.println(result.toString());
        System.out.println(result.getPathTo("Rom").get().size());


    }


//        ArrayList<N> nodesInQueue = new ArrayList<>();
//        N currentNode = start;
//
//
//        while(!type.stopSearch(currentNode)){
//
//
//            Collection<Edge<N,A>> children =  graph.getNeighbours(currentNode);
//
//
//            for (Edge<N, A> edge: children){
//                 N child = edge.getDestination();
//                nodesInQueue.add(child);
//                result.open(child, result.getInfomation(child));
//            }
//            nodesInQueue.remove(currentNode);
//            result.setClosed(currentNode);
//            currentNode = nodesInQueue.get(nodesInQueue.size()-1);
//            }
//        stopped = true;
//        stop();
//
//
//
//
//        return result;


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
//        result.getOnOpenList().clear();
//        result.getOnCloseList().clear();
    }
}
