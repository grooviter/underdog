package com.github.grooviter.underdog.graphs

import org.jgrapht.Graph
import org.jgrapht.alg.shortestpath.DijkstraShortestPath

/**
 * Methods to calculate paths in the graph
 *
 * @since 0.1.0
 */
class DistanceExtensions {
    /**
     * An implementation of Dijkstra's shortest path algorithm using a pairing heap by default.
     *
     * @param graph the graph to apply the algorithm to
     * @param from the starting point vertex
     * @param to the destination vertex
     * @return a list of vertices with the shortest path
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> List<V> shortestPath(G graph, V from, V to) {
        return new DijkstraShortestPath<>(graph).getPath(from, to).vertexList
    }
}
