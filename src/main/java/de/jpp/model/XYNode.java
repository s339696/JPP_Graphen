package de.jpp.model;

import java.util.Objects;

public class XYNode {

    private double x;
    private double y;
    private String label;


    /**
     * Creates a new XYNode with the specified label and coordinate
     *
     * @param label the label
     * @param x     the x value of the coordinate
     * @param y     the y value of the coordinate
     */
    public XYNode(String label, double x, double y) {
        if (label == null){
            label = "";
        }
        this.label = label;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the label of this node
     * @return
     */
    public String getLabel() {

        return label;
    }

    /**
     * Returns the x coordinate of this node
     * @return
     */
    public double getX() {

        return x;
    }

    /**
     * Returns the y coordinate of this node
     * @return
     */
    public double getY() {

        return y;
    }

    /**
     * Calculates the euclidian distance to the specified XYNode
     *
     * @param other the node to calculate the distance to
     * @return the euclidian distance to the specified XYNode
     */
    public double euclidianDistTo(XYNode other) {
        double x = (this.getX()-other.getX())*(this.getX()-other.getX());
        double y = (this.getY()- other.getY())*(this.getY()- other.getY());
        return Math.sqrt(x + y);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XYNode xyNode = (XYNode) o;
        return Double.compare(xyNode.x, x) == 0 &&
                Double.compare(xyNode.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "XYNode{" +
                "x=" + x +
                ", y=" + y +
                ", label='" + label + '\'' +
                '}';
    }

    public boolean isNeighbour(XYNode other){
        if (this.getY() == other.getY() - 1 && this.getX() == other.getX()
                || this.getY() == other.getY() + 1 && this.getX() == other.getX()
                    || this.getX() == other.getX() - 1 && this.getY() == other.getY()
                        ||this.getX() == other.getX() + 1 && this.getY() == other.getY()) {
            return true;
        } else {
            return false;
        }


    }
}
