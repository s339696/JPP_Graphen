package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.*;
import de.jpp.factory.SearchStopFactory;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.WeightedGraph;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class AStarSearch<N, A, G extends WeightedGraph<N,A>> implements SearchAlgorithm<N,A,G> {

    private boolean stopped = false;
    private G graph;
    private N start;
    private N dest;
    private EstimationFunction<N> estToDest;
    private SearchResultImpl<N, A> result;


    public AStarSearch(G graph, N start, N dest, EstimationFunction<N> estToDest) {
        this.graph = graph;
        this.start = start;
        this.dest = dest;
        this.estToDest = estToDest;

        result = new SearchResultImpl<>();
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
            result.unkown(node, new NodeInformation<>(null, Double.MAX_VALUE));
        }

        queue.offer(start);
        result.setOpen(start);
        result.close(start, new NodeInformation<>(null, 0));
        if (type.stopSearch(start) || stopped) {
            System.out.println("lol");
        } else {

            while (!queue.isEmpty()) {
                N currentNode = getNextNode(queue);
                System.out.println(currentNode.toString());
                queue.remove(currentNode);
                if (currentNode != start){
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
        XYNode a = new XYNode("A", 0, 0);
        XYNode b = new XYNode("B", 1, 1);
        XYNode c = new XYNode("C", 2, 2);
        XYNode d = new XYNode("D", 3, 3);
        XYNode e = new XYNode("E", 4, 4);
        XYNode f = new XYNode("F", -1.5, -1.5);
        XYNode g = new XYNode("G", -1, 0);

        graph1.addEuclidianEdge(a,b);
        graph1.addEuclidianEdge(b,c);
        graph1.addEuclidianEdge(c,d);
        graph1.addEuclidianEdge(d,e);
        graph1.addEuclidianEdge(a,f);
        graph1.addEuclidianEdge(a,g);

        /*

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
        graph1.addEdge(d,e, Optional.of(2.0));*/

        EstimationFunction<XYNode> distnace = XYNode::euclidianDistTo;
        AStarSearch<XYNode, Double, TwoDimGraph> search = new AStarSearch<>(graph1, a, e, distnace);
        StartToDestStrategy<XYNode> start = new StartToDestStrategy<>(e);
//      SearchResultImpl<XYNode, Double> result = new SearchResultImpl<>();

        SearchResult<XYNode, Double> result =search.findPaths(start);
        Collection<Edge<XYNode, Double>> edgeCollection = result.getPathTo(e).get();


    }


    public N getNextNode(Queue<N> queue){
        double bestFValue = Double.MAX_VALUE;
        N nextNode = null;
        for (N node: queue){
            double distanze = result.getNodeInformationMap().get(node).getDistance();
            double heuristic = estToDest.getEstimatedDistance(node, dest);
            double newFValue = distanze + heuristic;
            if (bestFValue>newFValue){
                bestFValue = newFValue;
                nextNode = node;
            }
        }
        return nextNode;
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
