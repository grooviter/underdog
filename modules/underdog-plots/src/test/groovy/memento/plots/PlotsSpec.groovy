package memento.plots

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
        plt.plots().plot(xs, ys) {
            title {
                text "this is my title"
            }
        }
    }

    void 'lists in dsl works'() {
        when:
        def xs = 0..8
        def ys = [
            nvdia: [1, 3, 10, 50, 100, 20, 32, 12],
            intel: [100, 30, 21, 120, 100, 75, 32, 30],
            micro: [32, 3, 45, 73, 10, 28, 32, 45]
        ]

        then:
        plt.plots().plot(xs, ys, title: "tech tickers").show()
    }
}
