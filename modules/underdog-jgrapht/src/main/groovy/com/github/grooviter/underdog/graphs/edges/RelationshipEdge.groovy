package com.github.grooviter.underdog.graphs.edges


import groovy.transform.TupleConstructor
import org.jgrapht.graph.DefaultWeightedEdge

@TupleConstructor(callSuper = true)
class RelationshipEdge extends DefaultWeightedEdge {
    String relation
    double weight

    @Override
    String toString() {
        return "$source-$target(relationship:$relation, weight:$weight)"
    }
}
