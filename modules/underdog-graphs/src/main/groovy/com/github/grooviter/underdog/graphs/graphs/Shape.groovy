package com.github.grooviter.underdog.graphs.graphs

import groovy.transform.Canonical

/**
 * Represents the shape of a graph, meaning how many vertices and edges has the graph
 *
 * @since 0.1.0
 */
@Canonical
class Shape {
    /**
     * The number of vertices in the graph
     *
     * @since 0.1.0
     */
    int vertices

    /**
     * The number of edges in the graph
     *
     * @since 0.1.0
     */
    int edges

    /**
     * Used for allowing destructuring
     *
     * <code>
     * def (vertices, edges) = graph.shape()
     * <code>
     *
     * @param index the property index, 0 for vertices and 1 for edges
     * @return the value of the indexed property
     * @since 0.1.0
     */
    int getAt(int index) {
        assert (0..1).containsWithinBounds(index), "index out of bounds ($index): 0 -> vertex count, 1 -> edge count"
        return [vertices, edges][index]
    }

    @Override
    String toString() {
        return "$vertices vertices X $edges edges"
    }
}
