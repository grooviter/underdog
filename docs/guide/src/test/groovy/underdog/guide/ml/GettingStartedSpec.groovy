package underdog.guide.ml

// tag::getting_started_simple_import[]
// imports
import underdog.Underdog
// end::getting_started_simple_import[]
import spock.lang.Specification
import underdog.plots.Plots

class GettingStartedSpec extends Specification {
    def "initial example"() {
        when:
        // tag::getting_started_simple[]
        // loading data
        def df = Underdog.df().read_csv("src/test/resources/data/baseball.csv")

        // transform dataframe
        df["RD"] = df["RS"] - df["RA"]

        // features and labels
        def X = df['RD'] as double[][]
        def y = df['W'] as double[]

        // using ml extensions
        def ml = Underdog.ml()

        // getting training and testing datasets
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(X, y)

        // create regression model
        def model = ml.regression.ols(xTrain, yTrain)

        // use model for prediction
        def predictedWins = model.predict(xTest)

        // end::getting_started_simple[]

        def plot = Underdog.plots()
            .scatter(
                predictedWins.toList(),
                yTest.toList(),
                xLabel: 'Run difference',
                yLabel: 'Wins',
                title: "Run difference vs Wins",
                subtitle: "Correlation between Wins and Run difference"
            )

        plot.show()
        Plots.show(plot, theme: "dark")

        def testScore = Underdog.ml().metrics.r2Score(yTest, predictedWins)

        then:
        (0.86..0.88).containsWithinBounds(testScore)
    }
}
