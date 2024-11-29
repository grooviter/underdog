package com.github.grooviter.underdog.plots.guide

import com.github.grooviter.underdog.graphs.Graphs
import memento.plots.Plots
import memento.plots.charts.Graph
import spock.lang.Specification

class GraphSpec extends Specification {
    def "create simple"() {
        expect:
        // tag::simple[]
        // create instance of Graph
        def friends = Graphs.graph(String) {
            edge('Robert', 'Thelma', relation: 'friend')
            edge('Robert', 'Troy', relation: 'friend')
        }

        // show plot
        Plots.plots().graph(friends).show()
        // end::simple[]
    }

    def "directed graph"() {
        expect:
        def friends = Graphs.digraph(String) {
            edge('Robert', 'Thelma', relation: 'friend')
            edge('Robert', 'Troy', relation: 'friend')
        }

        Plots.plots().graph(friends).show()
    }

    def "directed graph"() {
        expect:
        // tag::directed[]
        def friends = Graphs.digraph(String) {
            edge('Robert', 'Thelma', relation: 'friend')
            edge('Robert', 'Troy', relation: 'friend')
        }

        Plots.plots().graph(friends).show()
        // end::directed[]
    }

    def "show edge labels"() {
        expect:
        // tag::show_labels[]
        def friends = Graphs.digraph(String) {
            edge('Robert', 'Thelma', relation: 'friend')
            edge('Robert', 'Troy', relation: 'friend')
        }

        Plots.plots().graph(friends, showEdgeLabel: true).show()
        // end::show_labels[]
    }

    def "show graph path"() {
        expect:
        // tag::graph_path[]
        def friends = Graphs.digraph(String) {
            edge('Robert', 'Thelma', relation: 'friend')
            edge('Robert', 'Troy', relation: 'friend')
        }

        def friendship = friends.shortestPath('Robert', 'Troy')

        Plots.plots().graph(
            friends,
            paths: [friendship],
            showEdgeLabel: true).show()
        // end::graph_path[]
    }

    def "using Graph domain classes"() {
        expect:
        // tag::graph_domain[]
        List<Graph.Node> nodes = [
            new Graph.Node(id: "robert", name: "Robert", symbolSize: 75),
            new Graph.Node(id: "thelma", name: "Thelma", symbolSize: 40),
            new Graph.Node(id: "troy", name: "Troy", symbolSize: 40)
        ]

        List<Graph.Edge> edges = [
            new Graph.Edge(
                source: "robert",
                target: "thelma",
                color: "green",
                width: 2,
                value: "bff"),
            new Graph.Edge(
                source: "robert",
                target: "troy",
                color: "red",
                width: 10,
                value: "friend")
        ]

        Plots.plots().graph(
            nodes,
            edges,
            showEdgeLabel: true).show()
        // end::graph_domain[]
    }

    def "customizing Graph"() {
        expect:
        // tag::customize[]
        def friends = Graphs.digraph(String) {
            edge('Robert', 'Thelma', relation: 'friend')
            edge('Robert', 'Troy', relation: 'friend')
        }

        Plots.plots()
            .graph(friends, showEdgeLabel: true)
            .customize {
                title {
                    text "New title"
                    subtext("New subtitle")
                    top("bottom")
                    left("right")
                }
            }.show()
        // end::customize[]
    }
}
