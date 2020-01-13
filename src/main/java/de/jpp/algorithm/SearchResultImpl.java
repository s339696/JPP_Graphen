package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.NodeStatus;
import de.jpp.algorithm.interfaces.ObservableSearchResult;
import de.jpp.algorithm.interfaces.SearchResult;
import de.jpp.model.interfaces.Edge;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static de.jpp.algorithm.interfaces.NodeStatus.OPEN;

public class SearchResultImpl<N,A> implements ObservableSearchResult<N,A> {

    private Map<N, NodeStatus> nodeStatusMap = new HashMap<>();
    private Map<N, NodeInformation<N,A>> nodeInformationMap = new HashMap<>();
    private List<BiConsumer<N, SearchResult<N,A>>> onOpenList = new ArrayList<>();
    private List<BiConsumer<N, SearchResult<N,A>>> onCloseList = new ArrayList<>();


    public List<BiConsumer<N, SearchResult<N, A>>> getOnOpenList() {
        return onOpenList;
    }

    public List<BiConsumer<N, SearchResult<N, A>>> getOnCloseList() {
        return onCloseList;
    }

    /**
     * Adds a listener to the search result which is called whenever a new node is opened
     *
     * @param onOpen the listener to be added to the search result
     */
    @Override
    public void addNodeOpenedListener(BiConsumer<N, SearchResult<N, A>> onOpen) {
        onOpenList.add(onOpen);
    }

    /**
     * removes the specified listener from the search result
     *
     * @param onOpen the listener
     */
    @Override
    public void removeNodeOpenedListener(BiConsumer<N, SearchResult<N, A>> onOpen) {
        onOpenList.remove(onOpen);
    }

    /**
     * Adds a listener to the search result which is called whenever a new node is closed
     *
     * @param onClose
     */
    @Override
    public void addNodeClosedListener(BiConsumer<N, SearchResult<N, A>> onClose) {
        onCloseList.add(onClose);
    }

    /**
     * removes the specified listener from the search result
     *
     * @param onClose
     */
    @Override
    public void removeNodeClosedListener(BiConsumer<N, SearchResult<N, A>> onClose) {
        onCloseList.remove(onClose);
    }

    /**
     * Returns the status of the specified node
     *
     * @param node the node
     * @return the statuf of the specified node
     */
    @Override
    public NodeStatus getNodeStatus(N node) {
        return nodeStatusMap.get(node);
    }

    /**
     * Returns the predecessor of a path to the specified node (if already calculated)
     *
     * @param node the node
     * @return the predecessor of a path to the specified node (if already calculated)
     */
    @Override
    public Optional<Edge<N, A>> getPredecessor(N node) {
        if (!nodeInformationMap.containsKey(node)){
            return Optional.empty();
        } else {
            return Optional.ofNullable(nodeInformationMap.get(node).getPredecessor());
        }
    }

    /**
     * Returns all known (OPEN or CLOSED) nodes in this search
     *
     * @return all known (OPEN or CLOSED) nodes in this search
     */
    @Override
    public Collection<N> getAllKnownNodes() {
        return nodeStatusMap.keySet();
    }



    /**
     * Returns all OPEN nodes in this search
     *
     * @return all OPEN nodes in this search
     */
    @Override
    public Collection<N> getAllOpenNodes() {
        List<N> nodeList = new ArrayList<>();
        for ( Map.Entry<N, NodeStatus> entry:nodeStatusMap.entrySet()){
            if (entry.getValue() == OPEN){
                nodeList.add(entry.getKey());
            }
        }

        return nodeList;
    }

    /**
     * Sets the status of the specified node to CLOSED
     *
     * @param node the node
     */
    @Override
    public void setClosed(N node) {
        for (BiConsumer<N, SearchResult<N, A>> biConsumer: onCloseList){
            biConsumer.accept(node, this);
        }
       nodeStatusMap.put(node, NodeStatus.CLOSED);

    }

    /**
     * Sets the status of the specified node to OPEN
     *
     * @param node
     */

    public void setUnkown(N node){
        nodeStatusMap.put(node, NodeStatus.UNKOWN);
    }
    @Override
    public void setOpen(N node) {
        for (BiConsumer<N, SearchResult<N, A>> biConsumer: onOpenList){
            biConsumer.accept(node, this);
        }

        nodeStatusMap.put(node, OPEN);
    }

    /**
     * Removes all information from this SearchResult
     */
    @Override
    public void clear() {
        nodeStatusMap.clear();
        nodeInformationMap.clear();
    }

    public Map<N, NodeStatus> getNodeStatusMap() {
        return nodeStatusMap;
    }

    public Map<N, NodeInformation<N, A>> getNodeInformationMap() {
        return nodeInformationMap;
    }

    /**
     * Returns the path to the specified destination (if calculated) or an empty Optional
     *
     * @param dest the destination
     * @return the path to the specified destination (if calculated) or an empty Optional
     */
    @Override
    public Optional<List<Edge<N, A>>> getPathTo(N dest) {
        List<Edge<N,A>> edgeList = new ArrayList<>();
        Edge<N,A> startEdge = null;
        if (nodeInformationMap.containsKey(dest)){
            startEdge = nodeInformationMap.get(dest).getPredecessor();
        }
        if (startEdge == null){
            return Optional.empty();
        } else {
            edgeList.add(startEdge);
            boolean keepGoing = true;
            while(keepGoing) {
            N start = startEdge.getStart();
            Edge<N,A> edge = nodeInformationMap.get(start).getPredecessor();
            if (edge == null){
                keepGoing = false;
            } else {
                edgeList.add(edge);
                startEdge = edge;
            }

        }

        }
          List<Edge<N,A>> reverse = reverseList(edgeList);

        return Optional.of(reverse);
    }
    public static <T> List<T> reverseList(List<T> list)
    {
        return IntStream.range(0, list.size())
                .mapToObj(i -> list.get(list.size() - 1 - i))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void close(N node, NodeInformation<N,A> information){
        nodeInformationMap.put(node, information);
        setClosed(node);


    }
    public void open(N node, NodeInformation<N,A> information){
        nodeInformationMap.put(node, information);
        setOpen(node);
    }

    public void unkown(N node, NodeInformation<N,A> information){
        nodeInformationMap.put(node, information);
        nodeStatusMap.put(node, NodeStatus.UNKOWN);
    }
    public NodeInformation<N,A> getInfomation(N node){
        return nodeInformationMap.get(node);
    }

    @Override
    public String toString() {
        return "SearchResultImpl{" +
                "nodeStatusMap=" + nodeStatusMap +
                ", nodeInformationMap=" + nodeInformationMap +
                '}';
    }
}
