package com.github.grooviter.underdog.graphs

import com.github.grooviter.underdog.graphs.edges.RelationshipEdge
import org.jgrapht.Graph
import spock.lang.Specification

class BaseSpec extends Specification {
    static final String ADJ_LIST_SCHOOL_PATH = "src/test/resources/adjacency-lists/school-adjlist.txt"
    static final String EDGE_LIST_SCHOOL_PATH = "src/test/resources/adjacency-lists/school-edglist.txt"

    Graph<Person, RelationshipEdge> loadSchoolGraph() {
        def peter = new Person("Peter", 22)
        def laura = new Person("Laura", 23)
        def chris = new Person("Chris", 26)
        def bobby = new Person("Bobby", 56)

        return Graphs.graph(Person) {
            // 1) adding all vertices
            [peter, laura, chris, bobby].each(delegate::vertex)
            // 2) peter laura and troy are friends
            [peter, laura, chris].permutations().each { Person a, Person b, Person c ->
                edge(a, b, relation: "friend")
            }
            // 3) troyTeacher is teacher of the rest of the people+
            [[bobby], [peter, laura, chris]].combinations().each { Person a, Person b ->
                edge(a, b, relation: "teacher")
            }
        }
    }

    Graph<Person, RelationshipEdge> loadKarateGraph() {
        def peter = new Person("Peter", 22)
        def laura = new Person("Laura", 23)
        def pablo = new Person("Pablo", 45)
        return Graphs.digraph(Person) {
            [peter, laura, pablo].each(delegate::vertex)
            [[pablo], [peter, laura]].combinations().each { Person master, Person pupil ->
                edge(master, pupil, relation: "master")
            }
        }
    }

    Graph<Object, RelationshipEdge> loadBasketballGraph() {
        def vertices = ('A'..'E') + (1..6)
        return Graphs.digraph(Object) {
            vertices.each(delegate::vertex)
            [['A'], [1]].combinations().each { String fan, Integer team -> edge(fan, team, relation: 'fan') }
            [['B'], (1..2)].combinations().each { String fan, Integer team -> edge(fan, team, relation: 'fan') }
            [['C'], (3..4)].combinations().each { String fan, Integer team -> edge(fan, team, relation: 'fan') }
            [['D'], (4..6)].combinations().each { String fan, Integer team -> edge(fan, team, relation: 'fan') }
            [['E'], (2..4)].combinations().each { String fan, Integer team -> edge(fan, team, relation: 'fan') }
        }
    }

    List<Integer> getVerticesAndEdgesSizes(Graph graph) {
        return [graph.vertexSet(), graph.edgeSet()]*.size()
    }
}
