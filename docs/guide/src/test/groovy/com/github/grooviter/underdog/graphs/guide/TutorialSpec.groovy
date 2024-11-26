package com.github.grooviter.underdog.graphs.guide

import com.github.grooviter.underdog.graphs.Graphs
import memento.plots.Plots

// tag::import[]

import spock.lang.Specification

// end::import[]

class TutorialSpec extends Specification {
    def "creation"() {
        setup:
        // tag::create[]
        def graph = Graphs.graph(String) {
            // vertices and edges here
        }
        // end::create[]
        expect:
        graph
    }

    def "add vertices at creation time"() {
        when:
        // tag::add_vertices_at_creation_time[]
        def graph = Graphs.graph(String) {
            vertex("A")
            vertex("B")
        }
        // end::add_vertices_at_creation_time[]
        Plots.plots()
            .graph(graph, title: "add vertices")
            .show()

        then:
        graph.vertexSet().size() == 2
    }

    def "add vertices after creation"() {
        when:
        // tag::add_vertices_after_creation[]
        def graph = Graphs.graph(String) {}

        graph.addVertex("A")
        graph.addVertex("B")
        // end::add_vertices_after_creation[]
        then:
        graph.vertexSet().size() == 2
    }

    // tag::employee[]
    @groovy.transform.Canonical // <1>
    static class Employee {
        String name, department
    }
    // end::employee[]

    def "adding complex types"() {
        when:
        // tag::add_employees[]
        def john = new Employee("John", "Engineering")
        def peter = new Employee("Peter", "Engineering")
        def lisa = new Employee("Lisa", "Engineering")

        def graph = Graphs.graph(Employee) {
            vertex(john)
            vertex(peter)
            vertex(lisa)
        }
        // end::add_employees[]
        then:
        graph
    }

    def "adding edges at creation time"() {
        when:
        // tag::adding_edges_at_creation[]
        def graph = Graphs.graph(String) {
            // adding vertices first
            vertex('A')
            vertex('B')

            // then adding edges between vertices
            edge('A', 'B')
        }
        // end::adding_edges_at_creation[]

        Plots.plots()
            .graph(graph, title:"adding edges")
            .show()

        then:
        graph.edgeSet().size() == 1
    }

    def "adding edges at creation time (II)"() {
        when:
        // tag::adding_several_at_once[]
        def graph = Graphs.graph(String) {
            // adding vertices first
            ('A'..'D').each(delegate::vertex)

            // then adding several edges
            edges(
                'A', 'B',
                'B', 'C',
                'C', 'D'
            )
        }
        // end::adding_several_at_once[]

        Plots.plots()
            .graph(graph, title:"adding edges")
            .show()

        then:
        graph.edgeSet().size() == 3
    }

    def "adding edges after creation time"() {
        when:
        // tag::adding_edges_after_creation[]
        def graph = Graphs.graph(String) {
            // adding vertices 'A', 'B', 'C', 'D'
            ('A'..'D').each(delegate::vertex)
        }

        // adding edge between 'A' and 'B'
        graph.addEdge('A', 'B')
        // end::adding_edges_after_creation[]
        then:
        graph.verticesCount() == 4
        graph.edgesCount() == 1
    }

    def "adding vertices and edges and get shape"() {
        when:
        // tag::shape[]
        def graph = Graphs.graph(String) {
            ('A'..'D').each(delegate::vertex)
            edge('A', 'B')
            edge('B', 'C')
            edge('C', 'D')
        }

        // getting vertices and edges count
        def (nVertices, nEdges) = graph.shape()

        // or just println shape
        println(graph.shape())
        // end::shape[]
        then:
        nVertices == 4
        nEdges == 3
    }

    def "elements of a graph"() {
        when:
        // tag::elements_graph[]
        def graph = Graphs.graph(Integer) {
            (1..10).each(delegate::vertex)
            edges(
                1, 2,
                1, 3,
                3, 4
            )
        }
        // end::elements_graph[]

        then:
        // tag::elements[]
        graph.vertices.containsAll(1..10) // <1>
        graph.edges.size() == 3 // <2>
        graph.neighborsOf(1) == [2, 3] // <3>
        // end::elements[]
    }

    def "removing elements"() {
        when:
        // tag::removing_elements[]
        def graph = Graphs.graph(Integer) {
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

        // end::removing_elements[]
        then:
        graph4.vertices.containsAll([9, 11, 12, 13, 14])
    }

    def "graph types"() {
        when:
        // tag::graph_types[]
        // undirected weighted
        def graph1 = Graphs.graph(String)

        // directed weighted
        def graph2 = Graphs.digraph(String)

        // directed weighted pseudo graph
        def graph3 = Graphs.multidigraph(String)

        // weighted pseudo graph
        def graph4 = Graphs.multigraph(String)
        // end::graph_types[]
        then:
        graph1
        graph2
        graph3
        graph4
    }

    def "analyzing graph"() {
        when:
        // tag::analyzing_graph[]
        def graph = Graphs.graph(String) {
            ('a'..'f').each(delegate::vertex)

            edges(
                'a', 'b',
                    'a', 'c',
                    'a', 'd',
                    'b', 'e',
            )
        }
        // end::analyzing_graph[]

        // tag::analyzing_graph_shape[]
        def (nVertices, nEdges) = graph.shape()
        // end::analyzing_graph_shape[]

        // tag::analyzing_graph_max_degree[]
        String maxDegreeVertex = graph.maxDegree()
        // end::analyzing_graph_max_degree[]

        // tag::analyzing_graph_sort_by_degree[]
        def sortedVertices = graph.vertices.sort { -graph.degreeOf(it) }
        // end::analyzing_graph_sort_by_degree[]

        // tag::analyzing_graph_clustering_global[]
        def graphClustering = graph.clusteringGlobal()
        // end::analyzing_graph_clustering_global[]

        // tag::analyzing_graph_clustering_avg[]
        def graphAvg = graph.clusteringAvg()
        // end::analyzing_graph_clustering_avg[]

        // tag::analyzing_graph_clustering_vertex[]
        def vertexClustering = graph.clusteringOf('a')
        // end::analyzing_graph_clustering_vertex[]

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
