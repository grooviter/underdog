package underdog.guide.ml

import underdog.Underdog
import underdog.plots.Plots
import spock.lang.Specification

class TutorialSpec extends Specification {

    def gettingData() {
        // tag::loading_data[]
        def data = Underdog.df().read_csv("src/test/resources/data/baseball.csv")
        // end::loading_data[]

        // tag::only_before_2002[]
        data = data[data["year"] < 2002]
        // end::only_before_2002[]
        return data
    }

    def "show playoffs"() {
        setup: "loading data"
        def data = gettingData()

        when:
        // tag::show_playoffs[]
        def figure = Plots
            .plots()
            .scatter(
                data['W'],
                data['year'],
                group: data['playoffs'],
                title: 'Regular seasons wins by year')

        figure.show()
        // end::show_playoffs[]
        then:
        figure
    }

    def "show difference"() {
        setup:
        def data = gettingData()

        when:
        // tag::show_difference[]
        data['RD'] = data['RS'] - data['RA']
        // end::show_difference[]

        and:
        // tag::show_correlation[]
        def figure = Underdog
            .plots()
            .scatter(
                data['RD'],
                data['W'],
                title: 'Run difference vs Wins'
            )

        figure.show()
        // end::show_correlation[]
        then:
        figure
    }

    def "ols"() {
        setup:
        def data = gettingData()

        when:
        data['RD'] = data['RS'] - data['RA']

        // tag::ols[]
        // extracting features (X) and labels (y)
        def X = data['RD'] as double[][]
        def y = data['W'] as double[]

        // splitting between train and test datasets to avoid over fitting
        def (xTrain, xTest, yTrain, yTest) = Underdog.ml().utils.trainTestSplit(X, y)

        // training the model
        def winsModel = Underdog.ml().regression.ols(xTrain, yTrain)
        // end::ols[]

        println winsModel
        // tag::ols_sample_prediction[]
        def prediction = winsModel.predict([135] as double[])
        // end::ols_sample_prediction[]
        println prediction

        // tag::score[]
        // generating predictions for the test features
        def predictions = winsModel.predict(xTest)

        // comparing predictions with the actual truth for those features
        def r2score = Underdog.ml().metrics.r2Score(yTest, predictions)
        // end::score[]

        then:
        (0.88..0.89).containsWithinBounds(r2score)
    }

    def "runs_scored_model"() {
        setup:
        def data = gettingData()

        when:
        // tag::runs_scored_model[]
        def X = data['OBP', 'SLG'] as double[][]
        def y = data['RS'] as double[]

        def ml = Underdog.ml()
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(X, y)
        def runsScored = ml.regression.ols(xTrain, yTrain)

        // end::runs_scored_model[]
        println runsScored

        def residuals = runsScored.residuals()
        println residuals

        // tag::runs_scored_histogram[]
        def histogram = Plots
            .plots()
            .histogram(residuals.toList(), title: 'Runs Scored from OBP and SLG')

        histogram.show()
        // end::runs_scored_histogram[]

        // tag::fitted_vs_residuals[]
        def modelResiduals = runsScored.residuals().toList()
        def modelFitted = runsScored.fittedValues().toList()

        def fittedVsResiduals = Plots.plots()
            .scatter(modelFitted, modelResiduals,
                title: "Runs Scored from OBP and SLG",
                xLabel: "Fitted",
                yLabel: "Residuals")

        fittedVsResiduals.show()
        // end::fitted_vs_residuals[]
        then:
        true
    }

    def "Modeling Runs Allowed"() {
        setup:
        def data = gettingData()

        when:
        // tag::modeling_runs_allowed_model[]
        def X = data['OOBP', 'OSLG'].dropna() as double[][]
        def y = data['RA'] as double[]

        def ml = Underdog.ml()
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(X, y)
        def runsAllowed = ml.regression.ols(xTrain, yTrain)
        // end::modeling_runs_allowed_model[]
        then:
        true
    }

    def "offensive_plus_defensive_model"() {
        setup:
        def data = gettingData()

        when:
        // tag::offensive_plus_defensive_model[]
        def X = data["OOBP", "OBP", "OSLG", "SLG"].dropna() as double[][]
        def y = data['W'] as double[]

        def ml = Underdog.ml()
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(X, y)
        def winsFinal = ml.regression.ols(xTrain, yTrain)
        // end::offensive_plus_defensive_model[]

        and:
        // tag::as_in_2001[]
        def asIn2001 = data[
            data['team'] == 'OAK' &
            data['year'] == 2001].loc[__, ["year", "OOBP", "OBP", "OSLG", "SLG"]]
        // end::as_in_2001[]
        println asIn2001

        // tag::as_in_2001_prediction[]
        double[][] values = asIn2001.loc[__, ["OOBP", "OBP", "OSLG", "SLG"]] as double[][]
        double[] value = winsFinal.predict(values);
        // end::as_in_2001_prediction[]
        println "as_in_2001_prediction: ${value}"

        then:
        true
    }
}
