package com.github.grooviter.underdog.graphs

import com.github.grooviter.underdog.graphs.edges.RelationshipEdge
import com.github.grooviter.underdog.graphs.graphs.GraphBuilder

import org.jgrapht.graph.DefaultDirectedWeightedGraph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultUndirectedWeightedGraph
import org.jgrapht.graph.DirectedWeightedPseudograph
import org.jgrapht.graph.WeightedPseudograph

/**
 * This class creates different types of graphs using the <a href="https://jgrapht.org/">JGraphT</a> library. Some
 * of the methods allow to create different types of graphs and vertices and edges using a Groovy DSL.
 *
 * @since 0.1.0
 */
class Graphs {

    /**
     * The default implementation of an undirected graph. A default undirected graph is a non-simple undirected graph
     * in which multiple (parallel) edges between any two vertices are not permitted, but loops are.
     *
     * @param valueClass vertex type
     * @return an instance of {@link DefaultUndirectedWeightedGraph}
     * @since 0.1.0
     */
    static <V> DefaultUndirectedWeightedGraph<V, RelationshipEdge> graph(Class<V> valueClass) {
        return new DefaultUndirectedWeightedGraph<V, RelationshipEdge>(RelationshipEdge)
    }

    /**
     * The default implementation of an undirected graph. A default undirected graph is a non-simple undirected graph
     * in which multiple (parallel) edges between any two vertices are not permitted, but loops are.
     *
     * @param valueClass vertex type
     * @param builder {@link Closure} which makes available a DSL backed with class {@link GraphBuilder}
     * @return an instance of {@link DefaultUndirectedWeightedGraph}
     * @since 0.1.0
     */
    static <V, X extends GraphBuilder<V, DefaultUndirectedWeightedGraph<V, RelationshipEdge>>> DefaultUndirectedWeightedGraph<V, RelationshipEdge> graph(
            Class<V> valueClass,
            @DelegatesTo(type='X') Closure builder) {
        def closure = builder.clone() as Closure<X>
        def builderSource = new DefaultUndirectedWeightedGraph<V, RelationshipEdge>(RelationshipEdge)
        def graphBuilder = new GraphBuilder<V, DefaultUndirectedWeightedGraph<V, RelationshipEdge>>(builderSource)
        graphBuilder.with(closure)
        return graphBuilder.build()
    }

    /**
     * The default implementation of a directed weighted graph. A default directed weighted graph is a non-simple
     * directed graph in which multiple (parallel) edges between any two vertices are not permitted, but loops are.
     * The graph has weights on its edges.
     *
     * @param valueClass vertex type
     * @return
     * @since 0.1.0
     */
    static <V> DefaultDirectedWeightedGraph<V, DefaultEdge> digraph(Class<V> valueClass) {
        return new DefaultDirectedWeightedGraph<V, DefaultEdge>(DefaultEdge)
    }

    /**
     * The default implementation of a directed weighted graph. A default directed weighted graph is a non-simple
     * directed graph in which multiple (parallel) edges between any two vertices are not permitted, but loops are.
     * The graph has weights on its edges.
     *
     * @param valueClass vertex type
     * @param builder {@link Closure} which makes available a DSL backed with class {@link GraphBuilder}
     * @return
     * @since 0.1.0
     */
    static <V, X extends GraphBuilder<V, DefaultDirectedWeightedGraph<V, RelationshipEdge>>> DefaultDirectedWeightedGraph<V, RelationshipEdge> digraph(
            Class<V> valueClass,
            @DelegatesTo(type='X') Closure builder) {
        def closure = builder.clone() as Closure<X>
        def builderSource = new DefaultDirectedWeightedGraph<V, RelationshipEdge>(RelationshipEdge)
        def graphBuilder =  new GraphBuilder<V, DefaultDirectedWeightedGraph<V, RelationshipEdge>>(builderSource)
        graphBuilder.with(closure)
        return graphBuilder.build()
    }

    /**
     * A weighted pseudograph. A weighted pseudograph is a non-simple undirected graph in which both graph loops and
     * multiple (parallel) edges are permitted. The edges of a weighted pseudograph have weights.
     *
     * @param valueClass vertex type
     * @return an instance of {@link WeightedPseudograph}
     * @since 0.1.0
     */
    static <V> WeightedPseudograph multigraph(Class<V> valueClass) {
        return new WeightedPseudograph<V, DefaultEdge>(DefaultEdge)
    }

    /**
     * A weighted pseudograph. A weighted pseudograph is a non-simple undirected graph in which both graph loops and
     * multiple (parallel) edges are permitted. The edges of a weighted pseudograph have weights.
     *
     * @param valueClass vertex type
     * @param builder {@link Closure} which makes available a DSL backed with class {@link GraphBuilder}
     * @return an instance of {@link WeightedPseudograph}
     * @since 0.1.0
     */
    static <V, X extends GraphBuilder<V, WeightedPseudograph<V, RelationshipEdge>>> WeightedPseudograph<V, RelationshipEdge> multigraph(
            Class<V> valueClass,
            @DelegatesTo(type='X') Closure builder) {
        def closure = builder.clone() as Closure<X>
        def builderSource = new WeightedPseudograph<V, RelationshipEdge>(RelationshipEdge)
        def graphBuilder = new GraphBuilder<V, WeightedPseudograph<V, RelationshipEdge>>(builderSource)
        graphBuilder.with(closure)
        return graphBuilder.build()
    }

    /**
     * A directed weighted pseudograph. A directed weighted pseudograph is a non-simple directed graph in which both
     * graph loops and multiple (parallel) edges are permitted, and edges have weights.
     *
     * @param valueClass vertex type
     * @return an instance of {@link DirectedWeightedPseudograph}
     * @since 0.1.0
     */
    static <V> DirectedWeightedPseudograph multidigraph(Class<V> valueClass) {
        return new DirectedWeightedPseudograph<V, DefaultEdge>(DefaultEdge)
    }

    /**
     * A directed weighted pseudograph. A directed weighted pseudograph is a non-simple directed graph in which both
     * graph loops and multiple (parallel) edges are permitted, and edges have weights.
     *
     * @param valueClass vertex type
     * @param builder {@link Closure} which makes available a DSL backed with class {@link GraphBuilder}
     * @return an instance of {@link DirectedWeightedPseudograph}
     * @since 0.1.0
     */
    static <V, X extends GraphBuilder<V, DirectedWeightedPseudograph<V, RelationshipEdge>>> DirectedWeightedPseudograph<V, RelationshipEdge> multidigraph(
            Class<V> valueClass,
            @DelegatesTo(type='X') Closure builder) {
        def closure = builder.clone() as Closure<X>
        def builderSource = new DirectedWeightedPseudograph<V, RelationshipEdge>(RelationshipEdge)
        def graphBuilder = new GraphBuilder<V, DirectedWeightedPseudograph<V, RelationshipEdge>>(builderSource)
        graphBuilder.with(closure)
        return graphBuilder.build()
    }
}
