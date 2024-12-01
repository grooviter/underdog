package underdog.graphs


import org.jgrapht.Graph
import underdog.graphs.graphs.Shape

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

    /**
     * Gets a set of vertices from graph
     *
     * @param graph the graph where to get the vertices from
     * @return a set of vertices
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> Set<V> getVertices(G graph) {
        return graph.vertexSet()
    }

    /**
     * Gets a set of edges from graph
     *
     * @param graph the graph where to get the edges from
     * @return a set of edges
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> Set<E> getEdges(G graph) {
        return graph.edgeSet()
    }

    /**
     * Returns the number of vertices in the graph
     *
     * @param graph the graph to get the metric from
     * @return the number of vertices in graph
     * @since 0.1.0
     */
    static int verticesCount(Graph graph) {
        return graph.vertexSet().size()
    }

    /**
     * Returns the number of edges in the graph
     *
     * @param graph the graph to get the metric from
     * @return the number of edges in graph
     * @since 0.1.0
     */
    static int edgesCount(Graph graph) {
        return graph.edgeSet().size()
    }

    /**
     * Returns the {@link underdog.graphs.graphs.Shape} of the graph (vertices, edges)
     *
     * @param graph the graph to get the metric from
     * @return the {@link underdog.graphs.graphs.Shape} of the graph
     * @since 0.1.0
     */
    static Shape shape(Graph graph) {
        return new Shape(graph.verticesCount(), graph.edgesCount())
    }

    /**
     * Returns a list of vertices that are neighbors of the vertex passed as parameter
     *
     * @param graph the graph the vertices are in
     * @param vertex the vertex we want the neighbors from
     * @return a list of vertices that are neighbors of the vertex passed as parameter
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> List<V> neighborsOf(G graph, V vertex) {
        return graph.edgesOf(vertex).collectMany(edge -> graph.verticesOf(edge)) - vertex
    }
}
