package com.github.grooviter.underdog.plots.guide

import memento.plots.Plots
import spock.lang.Specification

class ScatterSpec extends Specification {
    def "simple"() {
        expect:
        // tag::simple[]
        // numbers from 0 to 99
        def xs = 0..<100 // <1>

        // 100 random numbers
        def ys = (0..<100).collect { new Random().nextInt(100) } // <2>

        // plot
        Plots.plots()
            .scatter(
                xs,
                ys,
                title: "Random Numbers") // <3>
            .show()
        // end::simple[]
    }

    def "simple series"() {
        setup:
        def df = [
            xs: 0..<100,
            ys: (0..<100).collect { new Random().nextInt(100) }
        ].toDF("numbers")
        expect:
        // tag::simple_series[]
        Plots.plots()
                .scatter(
                    df['xs'].rename('X'), // <1>
                    df['ys'].rename('Y'), // <2>
                    title: "Random Numbers")
                .show()
        // end::simple_series[]
    }
}
