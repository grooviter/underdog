package underdog.guide.graphs

import underdog.Underdog
import spock.lang.Specification

class DistancesSpec extends Specification {
    def "shortest path"() {
        when:
        // tag::shortest_path_graph[]
        def distances = Underdog.graphs().graph(String) {
            ["Madrid", "Guadalajara", "Cuenca", "Zaragoza", "Teruel", "Castellon"].each(delegate::vertex)
            edge("Madrid", "Guadalajara", weight: 66.4)
            edge("Madrid", "Salamanca", weight: 210)
            edge("Guadalajara", "Zaragoza", weight: 256.9)
            edge("Zaragoza", "Cuenca", weight: 290.2)
            edge("Cuenca", "Teruel", weight: 147.9)
            edge("Teruel", "Castellon", weight: 144.2)
        }
        // end::shortest_path_graph[]

        // tag::shortest_path_edges[]
        def kmsDriven = distances
            .shortestPathEdges("Teruel", "Madrid")
            .sum { it.weight }
        // end::shortest_path_edges[]

        // tag::shortest_path_vertices[]
        def citiesVisited = distances.shortestPathVertices("Teruel", "Madrid")
        // end::shortest_path_vertices[]

        // tag::shortest_path[]
        def shortestPath = distances.shortestPath("Teruel", "Madrid")
        // end::shortest_path[]

        Underdog.plots()
            .graph(
                distances,
                title: "Path from Madrid to Teruel",
                subtitle: "Calculating shortest path from two vertices",
                paths: [shortestPath],
                symbolSize: 75,
                showEdgeLabel: true).show()

        then:
        // tag::shortest_path_attributes[]
        shortestPath.weight == 761.4 // kms
        shortestPath.length == 4 // steps (edges)
        shortestPath.vertexList == ["Teruel", "Cuenca", "Zaragoza", "Guadalajara", "Madrid"] // cities (vertices)
        // end::shortest_path_attributes[]
        citiesVisited == ["Teruel", "Cuenca", "Zaragoza", "Guadalajara", "Madrid"]
        kmsDriven == 761.4
    }
}
