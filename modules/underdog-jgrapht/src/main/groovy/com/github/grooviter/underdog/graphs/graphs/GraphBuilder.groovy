package com.github.grooviter.underdog.graphs.graphs

import com.github.grooviter.underdog.graphs.edges.RelationshipEdge
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.TupleConstructor
import org.jgrapht.Graph

/**
 * DSL to build a new {@link Graph}
 *
 * @since 0.1.0
 */
@TupleConstructor
class GraphBuilder<V, G extends Graph<V, RelationshipEdge>> {
    G graph

    /**
     * Adds a vertex to the graph
     *
     * @param vertex
     * @return the current builder instance
     * @since 0.1.0
     */
    GraphBuilder<V,G> vertex(V vertex){
        this.graph.addVertex(vertex)
        return this
    }

    /**
     * Adds a new edge to the graph
     *
     * @param source source vertex
     * @param target target vertex
     * @param relation the relationship between source->target
     * @param weight the weight of the edge
     * @return the current builder instance
     * @since 0.1.0
     */
    @NamedVariant
    GraphBuilder<V,G> edge(
            V source,
            V target,
            @NamedParam(required = false) String relation = null,
            @NamedParam(required = false) double weight = 0.0) {
        this.graph.addEdge(source, target, new RelationshipEdge(relation, weight))
        return this
    }

    /**
     * Returns the result Graph
     *
     * @return the final graph
     * @since 0.1.0
     */
    G build() {
        return this.graph
    }
}
