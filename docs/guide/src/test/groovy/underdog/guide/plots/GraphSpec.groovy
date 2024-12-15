package underdog.guide.plots

import underdog.Underdog
import underdog.graphs.Graphs
import underdog.plots.Plots
import underdog.plots.charts.Graph
import spock.lang.Specification

class GraphSpec extends Specification {
    def "create simple"() {
        expect:
        // --8<-- [start:simple]
        // create instance of Graph
        def friends = Graphs.graph(String) {
            edge('Robert', 'Thelma', relation: 'friend')
            edge('Robert', 'Troy', relation: 'friend')
        }

        // show plot
        def plot = Underdog.plots().graph(friends)

        plot.show()
        // --8<-- [end:simple]
        Plots.show(plot, theme: "dark")
    }

    def "directed graph"() {
        expect:
        def friends = Graphs.digraph(String) {
            edge('Robert', 'Thelma', relation: 'friend')
            edge('Robert', 'Troy', relation: 'friend')
        }

        Underdog.plots().graph(friends).show()
    }

    def "directed graph"() {
        expect:
        // --8<-- [start:directed]
        def friends = Graphs.digraph(String) {
            edge('Robert', 'Thelma', relation: 'friend')
            edge('Robert', 'Troy', relation: 'friend')
        }

        def plot = Underdog.plots().graph(friends)
        plot.show()
        // --8<-- [end:directed]
        Plots.show(plot, theme: "dark")
    }

    def "show edge labels"() {
        expect:
        // --8<-- [start:show_labels]
        def friends = Graphs.digraph(String) {
            edge('Robert', 'Thelma', relation: 'friend')
            edge('Robert', 'Troy', relation: 'friend')
        }

        def plot = Plots.plots().graph(friends, showEdgeLabel: true)
        plot.show()
        // --8<-- [end:show_labels]
        Plots.show(plot, theme: "dark")
    }

    def "show graph path"() {
        expect:
        // --8<-- [start:graph_path]
        def friends = Graphs.digraph(String) {
            edge('Robert', 'Thelma', relation: 'friend')
            edge('Robert', 'Troy', relation: 'friend')
        }

        def friendship = friends.shortestPath('Robert', 'Troy')

        def plot = Underdog.plots().graph(
            friends,
            paths: [friendship],
            showEdgeLabel: true)

        plot.show()
        // --8<-- [end:graph_path]
        Plots.show(plot, theme: "dark")
    }

    def "using Graph domain classes"() {
        expect:
        // --8<-- [start:graph_domain]
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

        def plot = Underdog.plots().graph(
            nodes,
            edges,
            showEdgeLabel: true)

        plot.show()
        // --8<-- [end:graph_domain]
        Plots.show(plot, theme: "dark")
    }

    def "customizing Graph"() {
        expect:
        // --8<-- [start:customize]
        def friends = Graphs.digraph(String) {
            edge('Robert', 'Thelma', relation: 'friend')
            edge('Robert', 'Troy', relation: 'friend')
        }

        def plot = Underdog.plots()
            .graph(friends, showEdgeLabel: true)
            .customize {
                title {
                    text "New title"
                    subtext("New subtitle")
                    top("bottom")
                    left("right")
                }
            }

        plot.show()
        // --8<-- [end:customize]
        Plots.show(plot, theme: "dark")
    }
}
