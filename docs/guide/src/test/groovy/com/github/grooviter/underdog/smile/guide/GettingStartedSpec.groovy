package com.github.grooviter.underdog.smile.guide

// tag::getting_started_simple_import[]
// imports
import com.github.grooviter.underdog.Underdog
import com.github.grooviter.underdog.smile.Smile
// end::getting_started_simple_import[]
import spock.lang.Specification

class GettingStartedSpec extends Specification {
    def "initial example"() {
        when:
        def baseballFilePath = "src/test/resources/data/baseball.csv"
        // tag::getting_started_simple[]
        // loading data
        def df = Underdog.read_csv(baseballFilePath)

        // transform dataframe
        df["RD"] = df["RS"] - df["RA"]

        // create regression model
        def model = Smile.regression.ols(df["RD"] as double[][], df["W"] as double[])

        // use model for prediction
        def predictedWins = model.predict([10] as double[])
        // end::getting_started_simple[]
        then:
        (81.0..85.0).containsWithinBounds(predictedWins)
    }
}
