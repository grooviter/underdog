package com.github.grooviter.underdog.graphs

import com.github.grooviter.underdog.graphs.edges.RelationshipEdge
import groovy.transform.NamedVariant
import org.jgrapht.Graph
import org.jgrapht.graph.AbstractBaseGraph
import org.jgrapht.graph.AbstractGraph
import org.jgrapht.graph.AsGraphUnion
import org.jgrapht.traverse.BreadthFirstIterator
import org.jgrapht.traverse.DepthFirstIterator
import org.jgrapht.traverse.MaximumCardinalityIterator

/**
 * This class adds extra methods to Graphs created with the JGraphT library
 *
 * @since 0.1.0
 */
class GraphsExtensions {

    /**
     * @since 0.1.0
     */
    static <V, T extends AbstractBaseGraph<V, RelationshipEdge>> Iterator<V> iterateWithMaximumCardinality(T graph) {
        return new MaximumCardinalityIterator(graph)
    }

    /**
     * Returns the vertices from a given edge
     *
     * @param graph the graph where the edge is located
     * @param edge the edge to take the vertices from
     * @return a list with two vertex elements [source, target[
     * @since 0.1.0
     */
    static <V, E, G extends Graph<V,E>> List<V> getVerticesFromEdge(G graph, E edge) {
        return [graph.getEdgeSource(edge), graph.getEdgeTarget(edge)]
    }

    static <V, E, G extends Graph<V,E>> BreadthFirstIterator<V,E> 'get*'(G graph) {
        return new BreadthFirstIterator<V, E>(graph)
    }

    static <V, E, G extends Graph<V,E>> BreadthFirstIterator<V,E> 'get*'(G graph, V startVertex) {
        return new BreadthFirstIterator<V, E>(graph)
    }

    static <V, E> BreadthFirstIterator<V,E> breadthFirst(AbstractBaseGraph<V,E> graph) {
        return new BreadthFirstIterator<V, E>(graph)
    }

    static <V, E> BreadthFirstIterator<V,E> breadthFirst(AbstractBaseGraph<V,E> graph, V startVertex) {
        return new BreadthFirstIterator<V, E>(graph, startVertex)
    }

    static <V, E, G extends Graph<V,E>> DepthFirstIterator<V, E> 'get**'(G graph) {
        return new DepthFirstIterator<V,E>(graph)
    }

    static <V, E> DepthFirstIterator<V, E> 'get**'(AbstractBaseGraph<V,E> graph, V startVertex) {
        return new DepthFirstIterator<V,E>(graph, startVertex)
    }

    static <V, E> DepthFirstIterator<V, E> depthFirst(AbstractBaseGraph<V,E> graph) {
        return new DepthFirstIterator<V,E>(graph)
    }

    static <V, E> DepthFirstIterator<V, E> depthFirst(AbstractBaseGraph<V,E> graph, V startVertex) {
        return new DepthFirstIterator<V,E>(graph, startVertex)
    }

    static <V, E, G extends Graph<V,E>> AbstractGraph<V, E> plus(G left, G right) {
        return new AsGraphUnion<V,E>(left, right)
    }
}
