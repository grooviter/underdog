package com.github.grooviter.underdog.dataframe.guide

// tag::getting_started_simple_imports[]
// importing Underdog
import com.github.grooviter.underdog.Underdog
// end::getting_started_simple_imports[]
import spock.lang.Specification

class GettingStartedSpec extends Specification {
    def "initial example"() {
        when:
        def csvFilePath = this.class.getResource("/data/tornadoes_1950-2014.csv").file
        // tag::getting_started_simple[]
        // reading data from CSV file
        def df = Underdog.read_csv(csvFilePath)

        df["year"] = df["date"](Date, Integer) { it.format("yyyy").toInteger() }

        // how many tornadoes hit Texas in 2012
        def result = df[df["State"] == "TX" & df["year"] == 2012].size()
        // end::getting_started_simple[]
        then:
        result == 115
    }
}
