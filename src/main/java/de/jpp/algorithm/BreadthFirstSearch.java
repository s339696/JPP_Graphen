package de.jpp.algorithm;

import de.jpp.algorithm.interfaces.ObservableSearchResult;
import de.jpp.algorithm.interfaces.SearchResult;
import de.jpp.algorithm.interfaces.SearchStopStrategy;
import de.jpp.model.interfaces.Graph;

public class BreadthFirstSearch<N,A , G extends Graph<N,A>> extends BreadthFirstSearchTemplate<N, A, G> {

    public BreadthFirstSearch(G graph, N start) {
        super(graph, start);
    }
}
