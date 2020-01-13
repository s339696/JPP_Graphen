package de.jpp.model;

import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;
import de.jpp.model.interfaces.ObservableGraph;
import de.jpp.model.interfaces.WeightedGraph;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A Two Dimensional graph. <br>
 * The abstract-tag is only set because the tests will not compile otherwise. You should remove it!
 */
public  class TwoDimGraph extends ObservableGraphImpl<XYNode, Double> implements WeightedGraph<XYNode, Double>  {




    /**
     * Adds an edge to the graph which is automatically initialised with the euclidian distance of the nodes <br>
     * Returns the newly created edge
     *
     * @param start the start node of the edge
     * @param dest  the destination node of the edge
     * @return the newly created edge
     */
    public Edge<XYNode, Double> addEuclidianEdge(XYNode start, XYNode dest) {
       return addEdge(start, dest, Optional.of(start.euclidianDistTo(dest)));
    }




    /**
     * Returns the weight of the specified edge
     *
     * @param edge the edge
     * @return the weight of the specified edge
     */
    @Override
    public double getDistance(Edge<XYNode, Double> edge) {
        return edge.getAnnotation().get();
    }
}

