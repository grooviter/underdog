package com.github.grooviter.underdog.graphs

import com.github.grooviter.underdog.graphs.imports.Imports
import org.jgrapht.Graph

/**
 * @since 0.1.0
 */
class ImportExtensions {

    /**
     * @param graph
     * @return
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> Imports<V, E, G> importFrom(G graph) {
        return new Imports<>(graph)
    }

    static <V, E, G extends Graph<V,E>> G fromEdges(G graph, List<List<V>> tuples) {
        assert tuples.every { it.size() == 2 }, "tuples should be a list of list of 2 elements (tuples)"

        tuples.each { V a, V b ->
            graph.addVertex(a)
            graph.addVertex(b)
            graph.addEdge(a, b)
        }

        return graph
    }
}
