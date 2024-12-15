package underdog.guide.graphs

import underdog.Underdog
import spock.lang.Specification
import underdog.plots.Plots

class DistancesSpec extends Specification {
    def "shortest path"() {
        when:
        // --8<-- [start:shortest_path_graph]
        def distances = Underdog.graphs().graph(String) {
            ["Madrid", "Guadalajara", "Cuenca", "Zaragoza", "Teruel", "Castellon"].each(delegate::vertex)
            edge("Madrid", "Guadalajara", weight: 66.4)
            edge("Madrid", "Salamanca", weight: 210)
            edge("Guadalajara", "Zaragoza", weight: 256.9)
            edge("Zaragoza", "Cuenca", weight: 290.2)
            edge("Cuenca", "Teruel", weight: 147.9)
            edge("Teruel", "Castellon", weight: 144.2)
        }
        // --8<-- [end:shortest_path_graph]

        def plot = Underdog.plots().graph(distances)
        plot.show()
        Plots.show(plot, theme: "dark")

        // --8<-- [start:shortest_path_edges]
        def kmsDriven = distances
            .shortestPathEdges("Teruel", "Madrid")
            .sum { it.weight }
        // --8<-- [end:shortest_path_edges]

        // --8<-- [start:shortest_path_vertices]
        def citiesVisited = distances.shortestPathVertices("Teruel", "Madrid")
        // --8<-- [end:shortest_path_vertices]

        // --8<-- [start:shortest_path]
        def shortestPath = distances.shortestPath("Teruel", "Madrid")
        // --8<-- [end:shortest_path]

        def plotDistances = Underdog.plots()
            .graph(
                distances,
                title: "Path from Madrid to Teruel",
                subtitle: "Calculating shortest path from two vertices",
                paths: [shortestPath],
                symbolSize: 75,
                showEdgeLabel: true)

        plotDistances.show()
        Plots.show(plotDistances, theme: "dark")

        then:
        // --8<-- [start:shortest_path_attributes]
        // kms
        shortestPath.weight == 761.4

        // steps (edges)
        shortestPath.length == 4
        shortestPath.vertexList == ["Teruel", "Cuenca", "Zaragoza", "Guadalajara", "Madrid"] // cities (vertices)
        // --8<-- [end:shortest_path_attributes]
        citiesVisited == ["Teruel", "Cuenca", "Zaragoza", "Guadalajara", "Madrid"]
        kmsDriven == 761.4
    }
}
