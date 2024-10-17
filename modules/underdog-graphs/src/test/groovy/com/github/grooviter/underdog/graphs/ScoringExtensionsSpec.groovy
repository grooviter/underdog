package com.github.grooviter.underdog.graphs

class ScoringExtensionsSpec extends BaseSpec {
    def "clustering coefficient"() {
        when:
        def graph = loadSchoolGraph()

        then:
        graph.vertexSet().collect { graph.clusteringOf(it) }.sum() == 4.0
    }

    def "clustering coefficient for node #node(#range)"() {
        setup:
        def graph = Graphs
            .graph(String)
            .fromEdges(fromEdgesSource())

        expect:
        range.containsWithinBounds(graph.clusteringOf(node))

        where:
        node | range
        'F'  | 0.33..0.34
        'A'  | 0.66..0.67
        'J'  | 0.0..0.0
    }

    def "average clustering"() {
        setup:
        def graph = Graphs
            .graph(String)
            .fromEdges(fromEdgesSource())

        expect:
        (0.28..0.29).containsWithinBounds(graph.clusteringAvg())
    }

    def "global clustering"() {
        setup:
        def graph = Graphs
                .graph(String)
                .fromEdges(fromEdgesSource())

        expect:
        (0.40..0.41).containsWithinBounds(graph.clusteringGlobal())
    }

    private static List fromEdgesSource() {
        return [
            ['A', 'K'],
            ['A', 'B'],
            ['A', 'C'],
            ['B', 'C'],
            ['B', 'K'],
            ['C', 'E'],
            ['C', 'F'],
            ['D', 'E'],
            ['E', 'F'],
            ['E', 'H'],
            ['F', 'G'],
            ['I', 'J']
        ]
    }
}
