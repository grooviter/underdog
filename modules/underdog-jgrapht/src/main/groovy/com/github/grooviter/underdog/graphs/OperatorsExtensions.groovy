package com.github.grooviter.underdog.graphs

import org.jgrapht.Graph
import org.jgrapht.graph.AbstractGraph
import org.jgrapht.graph.AsGraphUnion

/**
 * This class adds extra methods for operating between Graphs
 *
 * @since 0.1.0
 */
class OperatorsExtensions {

    /**
     *
     * @param left
     * @param right
     * @return
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> AbstractGraph<V, E> plus(G left, G right) {
        return new AsGraphUnion<V,E>(left, right)
    }
}
