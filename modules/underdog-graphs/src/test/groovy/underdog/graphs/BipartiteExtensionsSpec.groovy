package underdog.graphs

class BipartiteExtensionsSpec extends BaseSpec {

    def "check: is bipartite"() {
        expect:
        loadBasketballGraph().isBipartite()
    }

    def "projected graph [#vertices, #edgesCount]"() {
        when:
        def projected = Graphs
            .graph(Object)
            .fromEdges(fromEdgesSource())
            .projectedGraph(vertices.toList())

        then:
        projected.edgeSet().size() == edgesCount

        where:
        vertices | edgesCount
        1..4     | 5
        'A'..'J' | 25
    }

    def "weighted projected graph"() {
        when:
        def projected = Graphs
            .graph(Object)
            .fromEdges(fromEdgesSource())
            .weightedProjectedGraph(vertices.toList())

        then:
        projected.edgeSet().size() == edgesCount

        and:
        projected.getEdge(node_1, node_2).weight == weight

        where:
        vertices | edgesCount | node_1 | node_2 | weight
        1..4     | 5          | 2      | 1      | 3.0d
        'A'..'J' | 25         | 'D'    | 'B'    | 2.0d
    }

    private static List fromEdgesSource() {
        return [
            ['A', 1],
            ['B', 1],
            ['C', 1],
            ['D', 1],
            ['H', 1],
            ['B', 2],
            ['C', 2],
            ['D', 2],
            ['E', 2],
            ['G', 2],
            ['E', 3],
            ['F', 3],
            ['H', 3],
            ['J', 3],
            ['E', 4],
            ['I', 4],
            ['J', 4]
        ]
    }
}
