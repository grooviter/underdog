package underdog.graphs


import org.jgrapht.Graph
import underdog.graphs.edges.RelationshipEdge

class ImportExtensionsSpec extends BaseSpec {
    def "import: adjacency list - string vertex"() {
        when:
        Graph<String, RelationshipEdge> graph = Graphs.graph()
                .importFrom()
                .adjacencyList(ADJ_LIST_SCHOOL_PATH)

        then:
        getVerticesAndEdgesSizes(graph) == [4, 5]
    }

    def "import: adjacency list - complex vertex"() {
        when:
        Graph<Person, RelationshipEdge> graph = Graphs.graph(Person)
                .importFrom()
                .adjacencyList(ADJ_LIST_SCHOOL_PATH){
                    return new Person(it)
                }

        then:
        getVerticesAndEdgesSizes(graph) == [4, 5]

        and:
        graph.vertexSet().every { it instanceof Person }
    }

    def "import: edgeList"() {
        when:
        def graph = Graphs.graph(String)
                .importFrom()
                .edgeList(EDGE_LIST_SCHOOL_PATH, hasWeight: true)

        then:
        getVerticesAndEdgesSizes(graph) == [4, 5]

        and:
        graph.edgeSet()*.weight.sum() == 8.0
    }

    def "import: edgeList custom vertex"() {
        when:
        def graph = Graphs.graph(Person)
                .importFrom()
                .edgeList(EDGE_LIST_SCHOOL_PATH, hasWeight: true) {
                    new Person(it)
                }

        then:
        getVerticesAndEdgesSizes(graph) == [4, 5]

        and:
        graph.edgeSet()*.weight.sum() == 8.0
    }
}
