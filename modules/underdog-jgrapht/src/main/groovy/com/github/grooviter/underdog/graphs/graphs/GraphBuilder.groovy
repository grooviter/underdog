package com.github.grooviter.underdog.graphs.graphs

import com.github.grooviter.underdog.graphs.edges.RelationshipEdge
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.TupleConstructor
import org.jgrapht.graph.AbstractBaseGraph

@TupleConstructor
class GraphBuilder<V, G extends AbstractBaseGraph<V, RelationshipEdge>> {
    G graph

    GraphBuilder<V,G> vertex(V id){
        this.graph.addVertex(id)
        return this
    }

    @NamedVariant
    GraphBuilder<V,G> edge(
            V source,
            V target,
            @NamedParam(required = false) String is = null,
            @NamedParam(required = false) double weight = 0.0) {
        this.graph.addEdge(source, target, new RelationshipEdge(is, weight))
        return this
    }

    G build() {
        return this.graph
    }
}
