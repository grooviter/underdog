package underdog.guide.ml

// --8<-- [start:import]
import underdog.Underdog
// --8<-- [end:import]
import spock.lang.Specification
import underdog.plots.Plots

class TutorialSpec extends Specification {

    def gettingData() {
        // --8<-- [start:loading_data]
        def data = Underdog.df().read_csv("src/test/resources/data/baseball.csv")
        // --8<-- [end:loading_data]

        // --8<-- [start:only_before_2002]
        data = data[data["year"] < 2002]
        // --8<-- [end:only_before_2002]
        return data
    }

    def "show playoffs"() {
        setup: "loading data"
        def data = gettingData()

        when:
        // --8<-- [start:show_playoffs]
        def figure = Underdog
            .plots()
            .scatter(
                data['W'],
                data['year'],
                group: data['playoffs'],
                title: 'Regular seasons wins by year')

        figure.show()
        // --8<-- [end:show_playoffs]
        Plots.show(figure, theme: "dark")
        then:
        figure
    }

    def "show difference"() {
        setup:
        def data = gettingData()

        when:
        // --8<-- [start:show_difference]
        data['RD'] = data['RS'] - data['RA']
        // --8<-- [end:show_difference]

        and:
        // --8<-- [start:show_correlation]
        def figure = Underdog
            .plots()
            .scatter(
                data['RD'],
                data['W'],
                title: 'Run difference vs Wins')

        figure.show()
        // --8<-- [end:show_correlation]
        Plots.show(figure, theme: "dark")
        then:
        figure
    }

    def "ols"() {
        setup:
        def data = gettingData()

        when:
        data['RD'] = data['RS'] - data['RA']

        // --8<-- [start:ols]
        // extracting features (X) and labels (y)
        def X = data['RD'] as double[][]
        def y = data['W'] as double[]

        // splitting between train and test datasets to avoid over fitting
        def (xTrain, xTest, yTrain, yTest) = Underdog.ml().utils.trainTestSplit(X, y)

        // training the model
        def winsModel = Underdog.ml().regression.ols(xTrain, yTrain)
        // --8<-- [end:ols]

        println winsModel
        // --8<-- [start:ols_sample_prediction]
        def prediction = winsModel.predict([135] as double[])
        // --8<-- [end:ols_sample_prediction]
        println prediction

        // --8<-- [start:score]
        // generating predictions for the test features
        def predictions = winsModel.predict(xTest)

        // comparing predictions with the actual truth for those features
        def r2score = Underdog.ml().metrics.r2Score(yTest, predictions)
        // --8<-- [end:score]

        then:
        (0.87..0.88).containsWithinBounds(r2score)
    }

    def "runs_scored_model"() {
        setup:
        def data = gettingData()

        when:
        // --8<-- [start:runs_scored_model]
        def X = data['OBP', 'SLG'] as double[][]
        def y = data['RS'] as double[]

        def ml = Underdog.ml()
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(X, y)
        def runsScored = ml.regression.ols(xTrain, yTrain)

        // --8<-- [end:runs_scored_model]
        println runsScored

        def residuals = runsScored.residuals()
        println residuals

        // --8<-- [start:runs_scored_histogram]
        def histogram = Underdog
            .plots()
            .histogram(residuals.toList(), title: 'Runs Scored from OBP and SLG')

        histogram.show()
        // --8<-- [end:runs_scored_histogram]
        Plots.show(histogram, theme: "dark")

        // --8<-- [start:fitted_vs_residuals]
        def modelResiduals = runsScored.residuals().toList()
        def modelFitted = runsScored.fittedValues().toList()

        def fittedVsResiduals = Underdog
            .plots()
            .scatter(modelFitted, modelResiduals,
                title: "Runs Scored from OBP and SLG",
                xLabel: "Fitted",
                yLabel: "Residuals")

        fittedVsResiduals.show()
        // --8<-- [end:fitted_vs_residuals]
        Plots.show(fittedVsResiduals, theme: "dark")
        then:
        true
    }

    def "Modeling Runs Allowed"() {
        setup:
        def data = gettingData()

        when:
        // --8<-- [start:modeling_runs_allowed_model]
        def X = data['OOBP', 'OSLG'].dropna() as double[][]
        def y = data['RA'] as double[]

        def ml = Underdog.ml()
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(X, y)
        def runsAllowed = ml.regression.ols(xTrain, yTrain)
        // --8<-- [end:modeling_runs_allowed_model]
        then:
        true
    }

    def "offensive_plus_defensive_model"() {
        setup:
        def data = gettingData()

        when:
        // --8<-- [start:offensive_plus_defensive_model]
        def X = data["OOBP", "OBP", "OSLG", "SLG"].dropna() as double[][]
        def y = data['W'] as double[]

        def ml = Underdog.ml()
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(X, y)
        def winsFinal = ml.regression.ols(xTrain, yTrain)
        // --8<-- [end:offensive_plus_defensive_model]

        and:
        // --8<-- [start:as_in_2001]
        def asIn2001 = data[
            data['team'] == 'OAK' &
            data['year'] == 2001].loc[__, ["year", "OOBP", "OBP", "OSLG", "SLG"]]
        // --8<-- [end:as_in_2001]
        println asIn2001

        // --8<-- [start:as_in_2001_prediction]
        double[][] values = asIn2001.loc[__, ["OOBP", "OBP", "OSLG", "SLG"]] as double[][]
        double[] value = winsFinal.predict(values);
        // --8<-- [end:as_in_2001_prediction]
        println "as_in_2001_prediction: ${value}"

        then:
        true
    }
}
