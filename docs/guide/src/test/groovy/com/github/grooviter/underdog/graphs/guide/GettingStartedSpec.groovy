package com.github.grooviter.underdog.graphs.guide

// tag::getting_started_simple_imports[]
// importing graphs
import com.github.grooviter.underdog.graphs.Graphs
// end::getting_started_simple_imports[]
import spock.lang.Specification

class GettingStartedSpec extends Specification {
    def "initial example"() {
        setup:
        // tag::getting_started_simple[]
        // building a simple graph
        def graph = Graphs.graph(String) {
            edges(
                    'A', 'K',
                    'A', 'B',
                    'B', 'K',
                    'B', 'C',
                    'C', 'E',
                    'D', 'E',
                    'E', 'I',
                    'I', 'J',
                    'E', 'H',
                    'E', 'F',
                    'C', 'F',
                    'F', 'G'
            )
        }

        // tell me the sortest path between node A and node H
        def shortestPath = graph.shortestPathVertices('A', 'H')
        // end::getting_started_simple[]
        expect:
        shortestPath == ['A', 'B', 'C', 'E', 'H']
    }
}
