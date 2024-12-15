package underdog.guide.graphs


import underdog.plots.Plots
import spock.lang.Specification

// --8<-- [start:import]
import underdog.Underdog
// --8<-- [end:import]

class TutorialSpec extends Specification {
    def "creation"() {
        setup:
        // --8<-- [start:create]
        def graph = Underdog.graphs().graph(String) {
            // vertices and edges here
        }
        // --8<-- [end:create]
        expect:
        graph
    }

    def "add vertices at creation time"() {
        when:
        // --8<-- [start:add_vertices_at_creation_time]
        def graph = Underdog.graphs().graph(String) {
            vertex("A")
            vertex("B")
        }
        // --8<-- [end:add_vertices_at_creation_time]
        def plot = Underdog.plots()
            .graph(graph, title: "add vertices")

        plot.show()
        Plots.show(plot, theme: "dark")

        then:
        graph.vertexSet().size() == 2
    }

    def "add vertices after creation"() {
        when:
        // --8<-- [start:add_vertices_after_creation]
        def graph = Underdog.graphs().graph(String) {}

        graph.addVertex("A")
        graph.addVertex("B")
        // --8<-- [end:add_vertices_after_creation]
        then:
        graph.vertexSet().size() == 2
    }

    // --8<-- [start:employee]
    /*
     * @Canonical implements equals and hashcode among other things.
     * These methods will help the graph to id each node in the graph.
    */
    @groovy.transform.Canonical
    static class Employee {
        String name, department
    }
    // --8<-- [end:employee]

    def "adding complex types"() {
        when:
        // --8<-- [start:add_employees]
        def john = new Employee("John", "Engineering")
        def peter = new Employee("Peter", "Engineering")
        def lisa = new Employee("Lisa", "Engineering")

        def graph = Underdog.graphs().graph(Employee) {
            vertex(john)
            vertex(peter)
            vertex(lisa)
        }
        // --8<-- [end:add_employees]
        then:
        graph
    }

    def "adding edges at creation time"() {
        when:
        // --8<-- [start:adding_edges_at_creation]
        def graph = Underdog.graphs().graph(String) {
            // adding vertices first
            vertex('A')
            vertex('B')

            // then adding edges between vertices
            edge('A', 'B')
        }
        // --8<-- [end:adding_edges_at_creation]

        // --8<-- [start:adding_edges_no_need_for_vertices]
        def graph2 = Underdog.graphs().graph(String) {
            edge('A', 'B')
        }
        // --8<-- [end:adding_edges_no_need_for_vertices]

        def plot = Underdog.plots()
            .graph(graph, title:"adding edges")

        plot.show()
        Plots.show(plot, theme: "dark")

        then:
        graph.edgeSet().size() == 1
        graph2.edgeSet().size() == 1
        graph2.vertices.size() == 2
    }

    def "adding edges at creation time (II)"() {
        when:
        // --8<-- [start:adding_several_at_once]
        def graph = Underdog.graphs().graph(String) {
            // adding vertices first
            ('A'..'D').each(delegate::vertex)

            // then adding several edges
            edges(
                'A', 'B',
                'B', 'C',
                'C', 'D'
            )
        }
        // --8<-- [end:adding_several_at_once]

        def plot = Underdog.plots()
            .graph(graph, title:"adding edges")

        plot.show()
        Plots.show(plot, theme: "dark")

        then:
        graph.edgeSet().size() == 3
    }

    def "adding edges after creation time"() {
        when:
        // --8<-- [start:adding_edges_after_creation]
        def graph = Underdog.graphs().graph(String) {
            // adding vertices 'A', 'B', 'C', 'D'
            ('A'..'D').each(delegate::vertex)
        }

        // adding edge between 'A' and 'B'
        graph.addEdge('A', 'B')
        // --8<-- [end:adding_edges_after_creation]
        then:
        graph.verticesCount() == 4
        graph.edgesCount() == 1
    }

    def "adding vertices and edges and get shape"() {
        when:
        // --8<-- [start:shape]
        def graph = Underdog.graphs().graph(String) {
            ('A'..'D').each(delegate::vertex)
            edge('A', 'B')
            edge('B', 'C')
            edge('C', 'D')
        }

        // getting vertices and edges count
        def (nVertices, nEdges) = graph.shape()

        // or just println shape
        println(graph.shape())
        // --8<-- [end:shape]
        then:
        nVertices == 4
        nEdges == 3
    }

    def "elements of a graph"() {
        when:
        // --8<-- [start:elements_graph]
        def graph = Underdog.graphs().graph(Integer) {
            (1..10).each(delegate::vertex)
            edges(
                1, 2,
                1, 3,
                3, 4
            )
        }
        // --8<-- [end:elements_graph]

        then:
        // --8<-- [start:elements]
        //  getting graph vertices
        graph.vertices.containsAll(1..10)

        // getting graph edges
        graph.edges.size() == 3

        // getting neighbors of a specific vertex
        graph.neighborsOf(1) == [2, 3]
        // --8<-- [end:elements]
    }

    def "removing elements"() {
        when:
        // --8<-- [start:removing_elements]
        def graph = Underdog.graphs().graph(Integer) {
            (1..14).each(delegate::vertex)
            edges(
                3, 5,
                5, 7,
                7, 9,
                9, 11,
                11, 12,
                12, 14
            )
        }

        // removing vertices using - operator
        def graph2 = graph - [2, 4, 6, 8, 10]
        def graph3 = graph2 - 1

        // removing vertices using function
        graph3.removeVertex(3)
        graph3.removeAllVertices([5, 7])

        // removing edge using - operator
        def graph4 = graph3 - graph3.edgesOf(9).first()

        // removing edges
        graph4.removeEdge(graph4.edgesOf(11).first())
        // graph4.removeAllEdges(graph4.edgesOf(12)) <--- this fails: ConcurrentModificationException
        graph4.removeAllEdges(graph4.edgesOf(12).toList()) // <--- this works

        // --8<-- [end:removing_elements]
        then:
        graph4.vertices.containsAll([9, 11, 12, 13, 14])
    }

    def "graph types"() {
        when:
        // --8<-- [start:graph_types]
        // graphs
        def g = Underdog.graphs()

        // undirected weighted
        def graph1 = g.graph(String)

        // directed weighted
        def graph2 = g.digraph(String)

        // directed weighted pseudo graph
        def graph3 = g.multidigraph(String)

        // weighted pseudo graph
        def graph4 = g.multigraph(String)
        // --8<-- [end:graph_types]
        then:
        graph1
        graph2
        graph3
        graph4
    }

    def "analyzing graph"() {
        when:
        // --8<-- [start:analyzing_graph]
        def graph = Underdog.graphs().graph(String) {
            ('a'..'f').each(delegate::vertex)

            edges(
                'a', 'b',
                'a', 'c',
                'a', 'd',
                'b', 'e',
            )
        }
        // --8<-- [end:analyzing_graph]

        // --8<-- [start:analyzing_graph_shape]
        def (nVertices, nEdges) = graph.shape()
        // --8<-- [end:analyzing_graph_shape]

        // --8<-- [start:analyzing_graph_max_degree]
        String maxDegreeVertex = graph.maxDegree()
        // --8<-- [end:analyzing_graph_max_degree]

        // --8<-- [start:analyzing_graph_sort_by_degree]
        def sortedVertices = graph.vertices.sort { -graph.degreeOf(it) }
        // --8<-- [end:analyzing_graph_sort_by_degree]

        // --8<-- [start:analyzing_graph_clustering_global]
        def graphClustering = graph.clusteringGlobal()
        // --8<-- [end:analyzing_graph_clustering_global]

        // --8<-- [start:analyzing_graph_clustering_avg]
        def graphAvg = graph.clusteringAvg()
        // --8<-- [end:analyzing_graph_clustering_avg]

        // --8<-- [start:analyzing_graph_clustering_vertex]
        def vertexClustering = graph.clusteringOf('a')
        // --8<-- [end:analyzing_graph_clustering_vertex]

        then:
        sortedVertices == ['a', 'b', 'c', 'd', 'e', 'f']
        nVertices == 6
        nEdges == 4
        maxDegreeVertex == 'a'
        graphClustering == 0
        graphAvg == 0
        vertexClustering == 0
    }
}
