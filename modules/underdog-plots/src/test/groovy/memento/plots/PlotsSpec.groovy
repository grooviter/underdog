package memento.plots

import com.github.grooviter.underdog.graphs.Graphs
import groovy.transform.TupleConstructor
import memento.plots.charts.Graph
import memento.plots.charts.ToMapAware
import spock.lang.Specification
import memento.plots.Plots as plt

class PlotsSpec extends Specification {
    void 'dsl works'() {
        when:
        def xs = [2, 3, 2]
        def ys = [1, 2, 3]
        then:
        plt.plots().plot(xs, ys, title: "title", smooth: true).show()
    }

    void 'dsl merge works'() {
        when:
        def xs = [2, 3, 2]
        def ys = [1, 2, 3]

        then:
        plt.plots().plot(xs, ys).customize {
            title {
                text "this is my title"
            }
        }
    }

    void 'lists in dsl works'() {
        when:
        def xs = 0..8
        def data = [
            nvdia: [xs, [1, 3, 10, 50, 100, 20, 32, 12]],
            intel: [xs, [100, 30, 21, 120, 100, 75, 32, 30]],
            micro: [xs, [32, 3, 45, 73, 10, 28, 32, 45]]
        ]

        then:
        plt.plots().plot(data, title: "tech tickers").show()
    }

    @TupleConstructor
    static class Person implements ToMapAware {
        String name
    }

    void "graph"() {
        setup:
        def brenda = new Person("Brenda")
        def brandon = new Person("Brandon")
        def dylan = new Person("Dylan")
        def kelly = new Person("Kelly")
        def steve = new Person("Steve")
        def andrea = new Person("Andrea")
        def david = new Person("David")
        def donna = new Person("Donna")

        def friends = Graphs.digraph(Person) {
            [brenda, dylan, brandon, kelly, steve, andrea, david, donna].each(delegate::vertex)
            edge(brandon, brenda, "brother")
            edge(brandon, steve, "friends")
            edge(brandon, dylan, "friends")
            edge(dylan, brenda, "boyfriend")
            edge(david, donna, "boyfriend")
            edge(andrea, brandon, 'workmate')
            edge(steve, david, "friend")
            edge(kelly, steve, "girlfriend")
        }

        expect:
        Plots
            .plots()
            .graph(
                friends,
                title: "Beverly Hills,90210",
                subtitle: "some of the characters of the tv show"
            ).show()
    }
}
