package com.github.grooviter.underdog.smile.guide

// tag::getting_started_simple_import[]
// imports
import com.github.grooviter.underdog.Underdog
import com.github.grooviter.underdog.smile.Smile
import memento.plots.Plots

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

        // features and labels
        def X = df['RD'] as double[][]
        def y = df['W'] as double[]

        // getting training and testing datasets
        def (xTrain, xTest, yTrain, yTest) = Smile.utils.trainTestSplit(X, y)

        // create regression model
        def model = Smile.regression.ols(xTrain, yTrain)

        // use model for prediction
        def predictedWins = model.predict(xTest)

        // end::getting_started_simple[]

        Plots.plots()
            .scatter(
                predictedWins.toList(),
                yTest.toList(),
                xLabel: 'Run difference',
                yLabel: 'Wins',
                title: "Run difference vs Wins",
                subtitle: "Correlation between Wins and Run difference"
            ).show()

        then:
        (0.87..0.89).containsWithinBounds(Smile.metrics.r2Score(yTest, predictedWins))
    }
}
