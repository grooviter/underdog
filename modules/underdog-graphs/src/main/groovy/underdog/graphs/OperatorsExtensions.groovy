package underdog.graphs

import org.jgrapht.Graph
import org.jgrapht.graph.AbstractGraph
import org.jgrapht.graph.AsGraphUnion
import org.jgrapht.graph.DefaultWeightedEdge

/**
 * This class adds extra methods for operating between Graphs
 *
 * @since 0.1.0
 */
class OperatorsExtensions {

    /**
     * Creates a Read-only union of two graphs.
     *
     * @param left source graph
     * @param right graph to union with
     * @return the union of both graphs
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> AbstractGraph<V, E> plus(G left, G right) {
        return new AsGraphUnion<V,E>(left, right)
    }

    /**
     * Removes a given vertex from the graph
     *
     * @param graph the graph to remove the vertex from
     * @param vertex the vertex to remove
     * @return the graph without the removed vertex
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> G minus(G graph, V vertex) {
        graph.removeVertex(vertex)
        return graph
    }

    /**
     * Removes a list of vertices from a given graph
     *
     * @param graph the graph to remove the vertices from
     * @param vertices the vertices to remove
     * @return the graph without the removed vertices
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> G minus(G graph, List<V> vertices) {
        graph.removeAllVertices(vertices)
        return graph
    }

    /**
     * Removes an edge from a given graph
     *
     * @param graph the graph to remove the edge from
     * @param edge the edge to remove
     * @return the graph without the removed edge
     * @since 0.1.0
     */
    static <V, E extends DefaultWeightedEdge, G extends Graph<V,E>> G minus(G graph, E edge) {
        graph.removeEdge(edge)
        return graph
    }
}
