package com.github.grooviter.underdog

import com.github.grooviter.underdog.graphs.Graphs
import com.github.grooviter.underdog.graphs.edges.RelationshipEdge
import groovy.transform.Canonical
import org.jgrapht.Graph
import spock.lang.Specification

class JGraphTExampleSpec extends Specification {

    @Canonical
    static class Person {
        String name
        Integer age
        Object getAt(Integer index) {
            return this.properties.values().indexed()[index]
        }
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

    def "iterators"() {
        setup:
        def school = loadSchoolGraph()

        when:
        def chris = school.'**'.find {it.name == 'Chris' && it.age < 30 } as Person
        def (name, age) = chris

        then:
        name == 'Chris' && age == 26

        when:
        def chrisFriends = school
            .edgesOf(chris)
            .findAll {it.relationship == 'friend' }
            .collectMany {school.getVerticesFromEdge(it) } - chris

        def (teacher, _) = school
            .edgesOf(chris)
            .find { it.relationship == 'teacher' }
            .with { school.getVerticesFromEdge(it) }

        then:
        chrisFriends.size() == 2
        chrisFriends.name.containsAll('Peter', 'Laura')

        and:
        teacher.name == 'Bobby'
    }

    def "graph union: plus operator"() {
        setup:
        def school = loadSchoolGraph()
        def karate = loadKarateGraph()

        when:
        def neighborhood = school + karate

        then:
        neighborhood.vertexSet().size() == 5
    }

    private static Graph<Person, RelationshipEdge> loadSchoolGraph() {
        def peter = new Person("Peter", 22)
        def laura = new Person("Laura", 23)
        def chris = new Person("Chris", 26)
        def bobby = new Person("Bobby", 56)

        return Graphs.graph(Person) {
            // 1) adding all vertices
            [peter, laura, chris, bobby].each(delegate::vertex)
            // 2) peter laura and troy are friends
            [peter, laura, chris].permutations().each { Person a, Person b, Person c ->
                edge(a, b, is: "friend")
            }
            // 3) troyTeacher is teacher of the rest of the people+
            [[bobby], [peter, laura, chris]].combinations().each { Person a, Person b ->
                edge(a, b, is: "teacher")
            }
        }
    }

    private static Graph<Person, RelationshipEdge> loadKarateGraph() {
        def peter = new Person("Peter", 22)
        def laura = new Person("Laura", 23)
        def pablo = new Person("Pablo", 45)
        return Graphs.digraph(Person) {
            [peter, laura, pablo].each(delegate::vertex)
            [[pablo], [peter, laura]].combinations().each { Person master, Person pupil ->
                edge(master, pupil, is: "master")
            }
        }
    }
}
