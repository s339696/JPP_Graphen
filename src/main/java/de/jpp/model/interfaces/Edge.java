package de.jpp.model.interfaces;

import java.util.Objects;
import java.util.Optional;

public class Edge<N, A> {

    private N start;
    private N dest;
    private Optional<A> annotation;
    /**
     * Creates a new edge with the specified start node, destination node and annotation
     *
     * @param start      the start node
     * @param dest       the destination node
     * @param annotation the annotation
     */
    public Edge(N start, N dest, Optional<A> annotation) {
        if (start == null){
            throw new NullPointerException("start is null");
        }
        this.start = start;
        if (dest == null){
            throw new NullPointerException("destination is null");
        }
        this.dest = dest;
        if (!annotation.isPresent()){
            this.annotation = Optional.empty();
        }
        this.annotation = annotation;
    }

    public void setStart(N start) {
        this.start = start;
    }

    public void setDest(N dest) {
        this.dest = dest;
    }

    public void setAnnotation(Optional<A> annotation) {
        this.annotation = annotation;
    }

    /**
     * Returns the start node of this edge
     *
     * @return the start node of this edge
     */
    public N getStart() {
        return start;
    }

    /**
     * Returns the destination node of this edge
     *
     * @return the destination node of this edge
     */
    public N getDestination() {

        return dest;
    }

    /**
     * Returns the annotation of this edge
     *
     * @return the annotation of this edge
     */
    public Optional<A> getAnnotation() {
        return annotation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge<N, A> edge = (Edge<N, A>) o;
        return start.equals(edge.start) &&
                dest.equals(edge.dest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, dest);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "start=" + start +
                ", dest=" + dest +
                ", annotation=" + annotation +
                '}';
    }
}
