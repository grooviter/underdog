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
        // tag::getting_started_simple[]
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
        // end::getting_started_simple[]

        Plots.plots().graph(graph, title: 'Nodes & Edges', subtitle: 'Creating graphs with Underdog').show()

        expect:
        shortestPath == ['A', 'B', 'C', 'E', 'I', 'J']
    }
}
