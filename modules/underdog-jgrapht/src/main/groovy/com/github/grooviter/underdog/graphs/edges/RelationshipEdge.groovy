package com.github.grooviter.underdog.graphs.edges


import groovy.transform.TupleConstructor
import org.jgrapht.graph.DefaultWeightedEdge

@TupleConstructor(callSuper = true)
class RelationshipEdge extends DefaultWeightedEdge {
    String relationship
    double weight

    @Override
    String toString() {
        return "$source-$target(relationship:$relationship, weight:$weight)"
    }
}
