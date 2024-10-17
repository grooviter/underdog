package com.github.grooviter.underdog

import com.github.grooviter.underdog.graphs.Graphs
import spock.lang.Specification

class MoviesEmployeesSpec extends Specification {
    static String GRAPH_CSV = "src/test/resources/com/github/grooviter/underdog/tablesaw/Employee_Movie_Choices.txt"
    static final List<String> EMPLOYEES = [
        'Pablo',
        'Lee',
        'Georgia',
        'Vincent',
        'Andy',
        'Frida',
        'Joan',
        'Claude']

    static final List<String> MOVIES = [
        'The Shawshank Redemption',
        'Forrest Gump',
        'The Matrix',
        'Anaconda',
        'The Social Network',
        'The Godfather',
        'Monty Python and the Holy Grail',
        'Snakes on a Plane',
        'Kung Fu Panda',
        'The Dark Knight',
        'Mean Girls']

    def answerOne() {
        return Graphs.graph(String)
            .importFrom()
            .edgeList(GRAPH_CSV, hasWeight: false, sep: '\t')
    }


    /**
     * Using Underdog, load in the bipartite graph from Employee_Movie_Choices.txt and return that graph.
     * This function should return a networkx graph with 19 nodes and 24 edges
     */
    def "Question One"() {
        setup:
        def graph = answerOne()

        expect: "24 edges"
        graph.edgeSet().size() == 24

        and: "19 vertices"
        graph.vertexSet().size() == 19
    }

    def "Question Two"() {
        expect:
        true
    }

    def "Question Three: #node_1 - #node_2 (#weight)"() {
        when:
        def graph = answerOne().weightedProjectedGraph(EMPLOYEES)

        then:
        graph.getEdge(node_1, node_2).weight == weight

        where:
        node_1    | node_2   | weight
        'Claude'  | 'Andy'   | 1.0d
        'Georgia' | 'Andy'   | 1.0d
        'Georgia' | 'Claude' | 3.0d
        'Joan'    | 'Andy'   | 1.0d
        'Lee'     | 'Andy'   | 1.0d
        'Lee'     | 'Joan'   | 3.0d
        'Frida'   | 'Andy'   | 1.0d
        'Pablo'   | 'Andy'   | 1.0d
        'Pablo'   | 'Frida'  | 2.0d
        'Vincent' | 'Frida'  | 2.0d
        'Vincent' | 'Pablo'  | 1.0d
    }
}
