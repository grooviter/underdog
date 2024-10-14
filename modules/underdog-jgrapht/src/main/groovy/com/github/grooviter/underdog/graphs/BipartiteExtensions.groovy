package com.github.grooviter.underdog.graphs

import org.jgrapht.Graph
import org.jgrapht.alg.partition.BipartitePartitioning

/**
 * Functions to evaluate and deal with bipartite graphs
 *
 * @since 0.1.0
 */
class BipartiteExtensions {
    /**
     * Check whether a graph is bipartite or not
     *
     * @param graph graph to evaluate
     * @return true if graph is bipartite, false otherwise
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> boolean isBipartite(G graph) {
        return new BipartitePartitioning<>(graph).isBipartite()
    }

    static <V, E, G extends Graph<V,E>> Set<E> projectedGraph(G graph, List<V> vertices) {
        return vertices.collectMany { graph.edgesOf(it) } as Set
    }
}
