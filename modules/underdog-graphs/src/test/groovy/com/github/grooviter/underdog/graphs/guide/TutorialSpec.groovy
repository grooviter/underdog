package com.github.grooviter.underdog.graphs.guide

// tag::import[]
import com.github.grooviter.underdog.graphs.Graphs
import com.github.grooviter.underdog.graphs.edges.RelationshipEdge
import groovy.transform.ToString

// end::import[]
import spock.lang.Specification

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
        // <1> getting vertices
        graph.vertices.containsAll(1..10)

        // <2> getting edges
        graph.edges.size() == 3

        // <3> getting neighbors of vertex 1
        graph.neighborsOf(1) == [2, 3]
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
            ('a'..'z').each(delegate::vertex)

            edges(
                'a', 'b',
                    'a', 'd',
                    'a', 'z'
            )
        }

        // getting shape
        def (nVertices, nEdges) = graph.shape()

        // degrees
        Map<String, Integer> degrees = graph.degrees()
        degrees['a'] == 3
        degrees['j'] == 0

        // max degree
        String maxDegreeVertex = graph.maxDegree()
        maxDegreeVertex == 'a'
        // end::analyzing_graph[]
        then:
        nVertices == 26
        nEdges == 3
        maxDegreeVertex == 'a'
    }
}
