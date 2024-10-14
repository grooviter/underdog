package com.github.grooviter.underdog.graphs

import org.jgrapht.Graph

/**
 * Shows graph general information
 *
 * @since 0.1.0
 */
class IntrospectionExtensions {

    /**
     * Returns the degree of the all vertices
     *
     * @param graph the graph to get the vertices degrees from
     * @return a {@link Map} with vertices as values and degrees as values
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> Map<V, Integer> degrees(G graph) {
        return graph.vertexSet().collectEntries { [(it): graph.degreeOf(it)] }
    }

    /**
     * Returns the vertex with highest degree
     *
     * @param graph the graph to get the highest degree vertex from
     * @return the highest degree vertex
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> V maxDegree(G graph) {
        return graph.vertexSet()
                .collectEntries { [(it): graph.degreeOf(it)] }
                .max { it.value }
                .key as V
    }

    /**
     * Returns vertices of a given edge
     *
     * @param graph the graph where the edge is located
     * @param edge the edge to take the vertices from
     * @return a list with two vertex elements [source, target[
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> List<V> verticesOf(G graph, E edge) {
        return [graph.getEdgeSource(edge), graph.getEdgeTarget(edge)]
    }
}
