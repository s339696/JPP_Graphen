package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.*;
import de.jpp.factory.SearchStopFactory;
import de.jpp.model.GraphImpl;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;
import de.jpp.model.interfaces.WeightedGraph;

import java.util.*;

public class DijkstraSearch<N, A, G extends WeightedGraph<N,A>> implements SearchAlgorithm<N,A,G> {

    private boolean stopped = false;
    private G graph;
    private N start;
    private SearchResultImpl<N, A> result;


    public DijkstraSearch(G graph, N start) {
        this.graph = graph;
        this.start = start;

        result = new SearchResultImpl<>();
    }





    @Override
    public SearchResult<N, A> findPaths(SearchStopStrategy<N> type) {
        this.stopped = false;
        Queue<N> queue = new LinkedList<>();
        for (N node : graph.getNodes()) {
            result.unkown(node, new NodeInformation<>(null, Double.MAX_VALUE));
        }

        queue.offer(start);
        result.setOpen(start);
        result.close(start, new NodeInformation<>(null, 0));
        if (type.stopSearch(start) || stopped) {
            System.out.println("lol");
        } else {

            while (!queue.isEmpty()) {
                N currentNode = getShortestToStart(queue);
                System.out.println(currentNode.toString());
                queue.remove(currentNode);
                if (currentNode!=start){
                result.setClosed(currentNode);

                }
                if (type.stopSearch(currentNode) || stopped) {
                    return result;
                }

                for (Edge<N, A> edge : graph.getNeighbours(currentNode)) {
                    if (result.getNodeStatusMap().get(edge.getDestination()) != NodeStatus.CLOSED) {
                        N childNode = edge.getDestination();
                        if (!queue.contains(childNode)){
                            queue.offer(childNode);
                        }

                        double currentDistanz = result.getNodeInformationMap().get(childNode).getDistance();
                        double newDistanz = 0;

                        if (edge.getAnnotation().isPresent()) {

                            newDistanz = result.getNodeInformationMap().get(currentNode).getDistance() + (Double) edge.getAnnotation().get();
                        } else {
                            newDistanz = result.getNodeInformationMap().get(currentNode).getDistance() + graph.getDistance(edge);
                        }
                            if (currentDistanz> newDistanz){
                                result.open(childNode, new NodeInformation<N,A>(edge, newDistanz));
//                            } else {
//                                result.open(childNode, new NodeInformation<>(edge, currentDistanz));
                                }
                        }
                    }
                }



        }
        return  result;
    }
    public static void main(String[] args) {
        TwoDimGraph graph1 = new TwoDimGraph();
        XYNode a = new XYNode("A", 2, 2);
        XYNode b = new XYNode("B", 1, 2);
        XYNode c = new XYNode("C", 1, 1);
        XYNode d = new XYNode("D", 1, 3);
        XYNode e = new XYNode("E", 3, 1);

        graph1.addEdge(a,b, Optional.of(4.0));
        graph1.addEdge(a,c, Optional.of(3.0));
        graph1.addEdge(a,e, Optional.of(7.0));
        graph1.addEdge(b,c, Optional.of(6.0));
        graph1.addEdge(b,d, Optional.of(5.0));
        graph1.addEdge(c,b, Optional.of(6.0));
        graph1.addEdge(c,d, Optional.of(11.0));
        graph1.addEdge(c,e, Optional.of(8.0));
        graph1.addEdge(e,d, Optional.of(2.0));

        graph1.addEdge(b,a, Optional.of(4.0));
        graph1.addEdge(c,a, Optional.of(3.0));
        graph1.addEdge(e,a, Optional.of(7.0));
        graph1.addEdge(c,b, Optional.of(6.0));
        graph1.addEdge(d,b, Optional.of(5.0));
        graph1.addEdge(b,c, Optional.of(6.0));
        graph1.addEdge(d,c, Optional.of(11.0));
        graph1.addEdge(e,c, Optional.of(8.0));
        graph1.addEdge(d,e, Optional.of(2.0));


        DijkstraSearch<XYNode, Double, TwoDimGraph> search = new DijkstraSearch<>(graph1, a);
        StartToDestStrategy<XYNode> start = new StartToDestStrategy<>(d);
//        SearchResultImpl<XYNode, Double> result = new SearchResultImpl<>();

        SearchResult<XYNode, Double> result =search.findPaths(start);


    }



    public N getShortestToStart(Queue<N> queue){
        double shortestDistanz = Double.MAX_VALUE;
        N shortestNode = null;
        for (N node: queue) {
            double newDistanz = result.getNodeInformationMap().get(node).getDistance();
            if (newDistanz<shortestDistanz){
                shortestNode = node;
                shortestDistanz = newDistanz;
            }
        }
        return shortestNode;
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
//
//        ArrayList<N> queue = new ArrayList<>();
//
//
//        for (N node : getGraph().getNodes()){
//            getResult().getNodeStatusMap().put(node, NodeStatus.UNKOWN);
//        }
//
//        N currentNode = getStart();
//        queue.add(getStart());
//        getResult().getNodeInformationMap().put(currentNode, new NodeInformation<>(null, 0));
//        getResult().getNodeStatusMap().put(currentNode, NodeStatus.OPEN);
//
//
//        while (b){
//
//            for (Edge<N,A> edge: getGraph().getNeighbours(currentNode)){
//
//                double edgeDistanze = getGraph().getDistance(edge);
//                if (getResult().getNodeStatusMap().get(edge.getDestination()) == NodeStatus.UNKOWN ){
//
//                    getResult().getNodeStatusMap().put(edge.getDestination(), NodeStatus.OPEN);
//                    queue.add(edge.getDestination());
//                    getResult().getNodeInformationMap().put(edge.getDestination(), new NodeInformation<>(edge, getResult().getNodeInformationMap().get(currentNode).getDistance()+ edgeDistanze));
//
//                } else if (getResult().getNodeStatusMap().get(edge.getDestination()) == NodeStatus.OPEN){
//
//                    double newDistanz = getResult().getNodeInformationMap().get(currentNode).getDistance()+ edgeDistanze;
//
//                    if (newDistanz< getResult().getNodeInformationMap().get(edge.getDestination()).getDistance()){
//                        getResult().getNodeInformationMap().put(edge.getDestination(), new NodeInformation<>(edge, newDistanz));
//                    }
//
//                }
//            }
//
//
//            queue.remove(currentNode);
//            getResult().getNodeStatusMap().put(currentNode, NodeStatus.CLOSED);
//
//            double smallest = -1;
//            for (Map.Entry<N, NodeInformation<N, A>> entry : getResult().getNodeInformationMap().entrySet()){
//                if (getResult().getNodeStatusMap().get(entry.getKey()) == NodeStatus.OPEN){
//
//                    if (smallest<0){
//                        smallest = entry.getValue().getDistance();
//                    } else{
//                        if (smallest> entry.getValue().getDistance()){
//                            smallest = entry.getValue().getDistance();
//                        }
//                    }
//                }
//            }
//
//            for (N node: queue){
//                if (smallest == getResult().getNodeInformationMap().get(node).getDistance()){
//                    currentNode = node;
//                }
//            }
//
//            if (queue.size()==0){
//                b = true;
//            }
//        }
//
//        return getResult();




}
