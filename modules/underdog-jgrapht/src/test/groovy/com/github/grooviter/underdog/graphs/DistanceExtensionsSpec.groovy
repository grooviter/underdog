package com.github.grooviter.underdog.graphs

import spock.lang.Specification

class DistanceExtensionsSpec extends Specification {
    def 'shortest path'() {
        setup:
        def graph = Graphs.graph(String) {
            ('A'..'K').each(delegate::vertex)
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

        expect:
        graph.shortestPath('A', 'H') == ['A', 'B', 'C', 'E', 'H']
    }
}
