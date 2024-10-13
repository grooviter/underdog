package com.github.grooviter.underdog.graphs

class CreationSpec extends BaseSpec {
    def "create: DSL"() {
        when:
        def graph = Graphs.graph(String) {
            ('A'..'C').each(delegate::vertex)
            edge('A', 'B', is: "employer")
            edge('A', 'C', is: "employer")
            edge('B', 'A', is: "employee")
            edge('C', 'A', is: "employee")
        }

        then:
        graph.vertexSet().size() == 3
        graph.edgeSet().size() == 2
    }

    def "create: #type"() {
        expect:
        employees.edgeSet().size() == expectedEdges

        where:
        type           | employees                                    | expectedEdges
        'graph'        | Graphs.graph(String, creationClosure)        | 2
        'digraph'      | Graphs.digraph(String, creationClosure)      | 4
        'multigraph'   | Graphs.multigraph(String, creationClosure)   | 4
        'multidigraph' | Graphs.multidigraph(String, creationClosure) | 4
    }

    private static Closure getCreationClosure() {
        return  {
            ('A'..'C').each(delegate::vertex)
            edge('A', 'B', is: "employer")
            edge('A', 'C', is: "employer")
            edge('B', 'A', is: "employee")
            edge('C', 'A', is: "employee")
        }
    }
}
