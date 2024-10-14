package com.github.grooviter.underdog.graphs

class ScoringExtensionsSpec extends BaseSpec {
    def "clustering coefficient"() {
        when:
        def graph = loadSchoolGraph()

        then:
        graph.vertexSet().collect { graph.clusteringOf(it) }.sum() == 4.0
    }

    def "clustering coefficient reference"() {
        setup:
        def graph = Graphs.graph(String) {
            ('A'..'K').each(delegate::vertex)
            edge('A', 'K')
            edge('A', 'B')
            edge('A', 'C')
            edge('B', 'C')
            edge('B', 'K')
            edge('C', 'E')
            edge('C', 'F')
            edge('D', 'E')
            edge('E', 'F')
            edge('E', 'H')
            edge('F', 'G')
            edge('I', 'J')
        }

        expect:
        (0.33..0.34).containsWithinBounds(graph.clusteringOf('F'))

        and:
        (0.66..0.67).containsWithinBounds(graph.clusteringOf('A'))

        and:
        graph.clusteringOf('J') == 0

        and:
        (0.28..0.29).containsWithinBounds(graph.clusteringAvg())
    }
}
