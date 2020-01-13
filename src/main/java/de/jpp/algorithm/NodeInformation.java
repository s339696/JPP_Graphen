package de.jpp.algorithm;

import de.jpp.model.interfaces.Edge;

public class NodeInformation<N,A> {

    Edge<N,A> predecessor;
    double distance;

    public NodeInformation(Edge<N, A> predecessor, double distance) {
        this.predecessor = predecessor;
        this.distance = distance;
    }

    public Edge<N, A> getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Edge<N, A> predecessor) {
        this.predecessor = predecessor;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
