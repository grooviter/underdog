package com.github.grooviter.underdog.plots.guide

import com.github.grooviter.underdog.Underdog
import spock.lang.Specification

class ExtensionsSpec extends Specification {
    def "dataframe"() {
        expect:
        // tag::dataframe[]
        Underdog.df()
            .from(X: 10..<20, Y: [1, 3, 9, 3, 19, 10, 11, 4, 14, 20], "dataframe name") // <1>
            .scatter() // <2>
            .show()
        // end::dataframe[]
    }
}
