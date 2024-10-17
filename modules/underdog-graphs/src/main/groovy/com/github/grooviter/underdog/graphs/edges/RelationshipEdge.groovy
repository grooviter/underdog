package com.github.grooviter.underdog.graphs.edges

import groovy.transform.TupleConstructor
import org.jgrapht.graph.DefaultWeightedEdge

@TupleConstructor
class RelationshipEdge extends DefaultWeightedEdge {
    String relation

    double getWeight() {
        return super.weight
    }

    @Override
    String toString() {
        StringBuilder builder = new StringBuilder()

        if (relation) {
            builder.append("relation: $relation")
        }

        if (weight) {
            builder.append("weight: $weight")
        }

        return "$source-$target($builder)"
    }
}
