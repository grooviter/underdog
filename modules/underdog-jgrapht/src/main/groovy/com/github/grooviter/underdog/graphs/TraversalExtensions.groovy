package com.github.grooviter.underdog.graphs

import org.jgrapht.Graph
import org.jgrapht.graph.AbstractBaseGraph
import org.jgrapht.traverse.BreadthFirstIterator
import org.jgrapht.traverse.DepthFirstIterator
import org.jgrapht.traverse.MaximumCardinalityIterator

/**
 * This class adds extra methods for traversing Graphs
 *
 * @since 0.1.0
 */
class TraversalExtensions {

    /**
     * @param graph
     * @return
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> BreadthFirstIterator<V,E> 'get*'(G graph) {
        return new BreadthFirstIterator<V, E>(graph)
    }

    /**
     * @param graph
     * @param startVertex
     * @return
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> BreadthFirstIterator<V,E> 'get*'(G graph, V startVertex) {
        return new BreadthFirstIterator<V, E>(graph, startVertex)
    }

    /**
     * @param graph
     * @return
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> BreadthFirstIterator<V,E> breadthFirst(G graph) {
        return new BreadthFirstIterator<V, E>(graph)
    }

    /**
     * @param graph
     * @param startVertex
     * @return
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> BreadthFirstIterator<V,E> breadthFirst(G graph, V startVertex) {
        return new BreadthFirstIterator<V, E>(graph, startVertex)
    }

    /**
     *
     * @param graph
     * @return
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> DepthFirstIterator<V, E> 'get**'(G graph) {
        return new DepthFirstIterator<V,E>(graph)
    }

    /**
     * @param graph
     * @param startVertex
     * @return
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> DepthFirstIterator<V, E> 'get**'(G graph, V startVertex) {
        return new DepthFirstIterator<V,E>(graph, startVertex)
    }

    /**
     * @param graph
     * @return
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> DepthFirstIterator<V, E> depthFirst(G graph) {
        return new DepthFirstIterator<V,E>(graph)
    }

    /**
     * @param graph
     * @param startVertex
     * @return
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> DepthFirstIterator<V, E> depthFirst(G graph, V startVertex) {
        return new DepthFirstIterator<V,E>(graph, startVertex)
    }

    /**
     * @param graph
     * @return
     * @since 0.1.0
     */
    static <V,E, G extends AbstractBaseGraph<V, E>> MaximumCardinalityIterator<V,E> iterateWithMaximumCardinality(G graph) {
        return new MaximumCardinalityIterator<V,E>(graph)
    }


    /**
     * Returns vertices from a given edge
     *
     * @param graph the graph where the edge is located
     * @param edge the edge to take the vertices from
     * @return a list with two vertex elements [source, target[
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> List<V> getVerticesFromEdge(G graph, E edge) {
        return [graph.getEdgeSource(edge), graph.getEdgeTarget(edge)]
    }
}
