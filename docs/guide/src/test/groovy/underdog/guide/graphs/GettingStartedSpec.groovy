package underdog.guide.graphs

// tag::getting_started_simple_imports[]
// importing graphs
import underdog.Underdog
// end::getting_started_simple_imports[]
import underdog.plots.Plots
import spock.lang.Specification

class GettingStartedSpec extends Specification {
    def "initial example"() {
        setup:
        // --8<-- [start:getting_started_simple]
        // building a simple graph
        def graph = Underdog.graphs().graph(String) {
                edges(
                        'A', 'K',
                        'A', 'B',
                        'B', 'K',
                        'B', 'C',
                        'C', 'E',
                        'D', 'E',
                        'E', 'I',
                        'I', 'J'
                )
            }

        // tell me the sortest path between node A and node H
        def shortestPath = graph.shortestPathVertices('A', 'J')
        // --8<-- [end:getting_started_simple]

        def plot = Underdog.plots()
            .graph(
                graph,
                title: 'Nodes & Edges',
                subtitle: 'Creating graphs with Underdog')

        plot.show()
        Plots.show(plot, theme: "dark")

        expect:
        shortestPath == ['A', 'B', 'C', 'E', 'I', 'J']
    }
}
