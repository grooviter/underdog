package underdog.blog.y2024.m12

import underdog.DataFrame

import java.time.LocalDate

// --8<-- [start:import_json]
import static groovy.json.JsonOutput.toJson
import static groovy.json.JsonOutput.prettyPrint
// --8<-- [end:import_json]
import smile.feature.extraction.PCA
import spock.lang.Specification
// --8<-- [start:import]
import underdog.Underdog
// --8<-- [end:import]
import underdog.plots.Options
import underdog.plots.Plots
import underdog.plots.dsl.series.LineSeries
import underdog.plots.dsl.series.ScatterSeries

class LinearRegressionNotesSpec extends Specification {

    static final String FILE_PATH = "src/test/resources/data/blog/2024/12/day.csv"

    def "correlation"() {
        when:
        def df = loadData()

        // --8<-- [start:correlation_matrix]
        def plot = Underdog
            .plots()
            .correlationMatrix(df)

        plot.show()
        // --8<-- [end:correlation_matrix]
        Plots.show(plot, theme: 'dark')

        then:
        plot
    }

    private static DataFrame loadData() {
        // --8<-- [start:load_data]
        def data = Underdog.df()
            .read_csv(FILE_PATH, dateFormat: 'yyyy-MM-dd')
            .dropna()  // removing missing values
            .sort_values(by: ['dteday'])
            .drop('instant', 'yr', 'casual', 'cnt', 'dteday') // removing some columns
        // --8<-- [end:load_data]
        println(data.columns)
        println(data.head(5))
        return data
    }

    def "pair plot"() {
        when:
        def df = loadData()

        // --8<-- [start:pair_plot]
        def plot = Underdog
            .plots()
            .scatterMatrix(df['temp', 'registered'])

        plot.show()
        // --8<-- [end:pair_plot]
        Plots.show(plot, theme: 'dark')

        then:
        plot
    }

    def "linear regression: OLS non polynomial"() {
        setup:
        def df = loadData()

        when:
        // --8<-- [start:linear_regression]
        // extracting features and labels
        def X = df['temp'] as double[][]
        def y = df['registered'] as double[]

        def ml = Underdog.ml()

        // train test split (more data for training)
        def (
            xTrain,
            xTest,
            yTrain,
            yTest
        ) = ml.utils.trainTestSplit(X, y)

        // creating and training model
        def model = ml.regression.ols(xTrain, yTrain)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrain, yTrain).round(2)
        def scoreTest = model.score(xTest, yTest).round(2)

        print("train: ${scoreTrain}, test: ${scoreTest}")
        // --8<-- [end:linear_regression]

        // --8<-- [start:linear_regression_plot]
        def plt = Options.create {
            xAxis { show(true) }
            yAxis { show(true) }
            // SCATTER PLOT
            series(ScatterSeries){
                data(
                    toData(
                        xTest, // x1, x2,...xn
                        yTest  // y1, y2,...yn
                    ) // [[x1,y1], [x2,y2],...[xn, yn]]
                )
            }
            // REGRESSION LINE
            series(LineSeries) {
                data(
                    toData(
                        xTest,
                        model.predict(xTest)
                    )
                )
            }
        }

        plt.show()
        // --8<-- [end:linear_regression_plot]
        Plots.show(plt, theme: 'dark')
        then:
        (0.40..0.50).containsWithinBounds(scoreTrain)
        (-1.25..-1.20).containsWithinBounds(scoreTest)
    }

    def "linear regression: OLS polynomial features"() {
        setup:
        def df = loadData()

        when:
        // extracting features and labels
        def X = df['temp'] as double[][]
        def y = df['registered'] as double[]

        def ml = Underdog.ml()

        // --8<-- [start:linear_regression_polynomial]
        // transforming X adding new generated features
        def xPoly = ml.features.polynomialFeatures(X)
        def xNormalized = ml.features.minMaxScaler(xPoly).apply(xPoly)

        // train test split (more data for training)
        def (
            xTrain,
            xTest,
            yTrain,
            yTest
        ) = ml.utils.trainTestSplit(xNormalized, y)

        // creating and training model
        def model = ml.regression.ols(xTrain, yTrain)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrain, yTrain).round(2)
        def scoreTest = model.score(xTest, yTest).round(2)

        print("train: ${scoreTrain}, test: ${scoreTest}")
        // --8<-- [end:linear_regression_polynomial]

        // --8<-- [start:linear_regression_polynomial_plot]
        // building chart
        def plt = Options.create {
            legend {
                top("5%")
                show(true)
            }
            xAxis { show(true) }
            yAxis { show(true) }
            // SCATTER PLOT
            series(ScatterSeries){
                data(toData(X, y))
            }
            // REGRESSION LINES
            def names = ['1', 'temp', 'temp^2'].indexed()
            def colors = ['gray', 'green', 'brown'].indexed()

            (0..<xTest.shape().cols).each {feature ->
                series(LineSeries) {
                    smooth(true)
                    name(names[feature])
                    itemStyle { color(colors[feature]) }
                    data(
                        toData(
                            xTest.collect { it[feature] },
                            model.predict(xTest)
                        )
                    )
                }

            }
        }

        // showing chart
        plt.show()
        // --8<-- [end:linear_regression_polynomial_plot]
        Plots.show(plt, theme: 'dark')

        then:
        (0.50..0.55).containsWithinBounds(scoreTrain)
        (-1.0..-0.95).containsWithinBounds(scoreTest)
    }

    def "feature extraction: Lasso"() {
        setup:
        def df = loadData()

        when:
        // --8<-- [start:lasso_feature_extraction]
        def features = df.columns - "registered"

        def X = df[features] as double[][]
        def y = df['registered'] as double[]

        // creating lasso model
        def model = Underdog.ml().regression.lasso(X, y)

        // extracting coefficients for every feature
        def featCoefficients = [features, model.coefficients()].transpose().collectEntries()

        println(prettyPrint(toJson(featCoefficients)))
        // --8<-- [end:lasso_feature_extraction]

        // --8<-- [start:best_features]
        def bestFeatures = featCoefficients
                .findAll { it.value > 0 }  // all with coefficient > 0
                .sort { -it.value }        // sort desc by coefficient
                *.key as List<String>      // getting feature names

        println(bestFeatures)
        // --8<-- [end:best_features]
        then:
        bestFeatures == ['atemp', 'temp', 'workingday', 'season', 'weekday']
    }

    def "linear regression: best features"() {
        def df = loadData()

        when:
        def X = df['atemp', 'temp', 'workingday', 'season', 'weekday'] as double[][]
        def y = df['registered'] as double[]

        def ml = Underdog.ml()

        and:
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(X, y, train_size: 0.2)

        and:
        def model = ml.regression.ols(xTrain, yTrain)
        def scoreTest = model.score(xTest, yTest).round(2)

        then:
        (-0.20..-0.15).containsWithinBounds(scoreTest)
    }

    def "regularization & normalization: Ridge"() {
        setup:
        def df = loadData()

        when:
        // --8<-- [start:ridge_regression]
        def X = df['atemp', 'temp', 'workingday', 'season', 'weekday'] as double[][]
        def y = df['registered'] as double[]
        def ml = Underdog.ml()

        // train test split (more data for training)
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(X, y)

        // creating and training model (RIDGE)
        def model = ml.regression.ridge(xTrain, yTrain, alpha: 20)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrain, yTrain).round(2)
        def scoreTest = model.score(xTest, yTest).round(2)

        print("train: ${scoreTrain}, test: ${scoreTest}")
        // --8<-- [end:ridge_regression]

        then:
        (0.60..0.70).containsWithinBounds(scoreTrain)
        (-1.05..-1.00).containsWithinBounds(scoreTest)
    }

    def "regularization & normalization: Ridge (MinMaxScaler)"() {
        setup:
        def df = loadData()

        when:
        def X = df['atemp', 'temp', 'workingday', 'season', 'weekday'] as double[][]
        def y = df['registered'] as double[]
        def ml = Underdog.ml()

        // --8<-- [start:ridge_regression_min_max]
        // normalizing all features
        def xScaled = ml.features.minMaxScaler(X).apply(X)

        def (
            xTrain,
            xTest,
            yTrain,
            yTest
        ) = ml.utils.trainTestSplit(xScaled, y)

        // creating and training model (RIDGE w/ minMax scaler)
        def model = ml.regression.ridge(xTrain, yTrain, alpha: 20)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrain, yTrain).round(2)
        def scoreTest = model.score(xTest, yTest).round(2)

        print("train: ${scoreTrain}, test: ${scoreTest}")
        // --8<-- [end:ridge_regression_min_max]

        then:
        (0.60..0.70).containsWithinBounds(scoreTrain)
        (-1.05..-1.00).containsWithinBounds(scoreTest)
    }

    def "regularization & normalization: Lasso"() {
        setup:
        def df = loadData()

        when:
        def X = df['atemp', 'temp', 'workingday', 'season', 'weekday'] as double[][]
        def y = df['registered'] as double[]
        def ml = Underdog.ml()

        // train test split
        def (
            xTrain,
            xTest,
            yTrain,
            yTest
        ) = ml.utils.trainTestSplit(X, y)

        // --8<-- [start:lasso_regression]
        def model = ml.regression.lasso(xTrain, yTrain, alpha: 20)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrain, yTrain).round(2)
        def scoreTest = model.score(xTest, yTest).round(2)

        print("train: ${scoreTrain}, test: ${scoreTest}")
        // --8<-- [end:lasso_regression]

        then:
        (0.60..0.70).containsWithinBounds(scoreTrain)
        (-1.05..-1.00).containsWithinBounds(scoreTest)
    }

    def "regularization & normalization: Lasso (MinMaxScaler)"() {
        setup:
        def df = loadData()

        when:
        def X = df['temp'] as double[][]
        def y = df['registered'] as double[]
        def ml = Underdog.ml()

        // --8<-- [start:lasso_regression_min_max]
        // train test split
        def xScaled = ml.features.minMaxScaler(X).apply(X)

        def (
                xTrain,
                xTest,
                yTrain,
                yTest
        ) = ml.utils.trainTestSplit(xScaled, y)

        // creating and training model (LASSO w/ minMax scaler)
        def model = ml.regression.lasso(xTrain, yTrain, alpha: 20)

        def scoreTrain = model.score(xTrain, yTrain).round(2)
        def scoreTest = model.score(xTest, yTest).round(2)

        print("train: ${scoreTrain}, test: ${scoreTest}")
        // --8<-- [end:lasso_regression_min_max]
        then:
        (0.45..0.50).containsWithinBounds(scoreTrain)
        (-1.23..-1.20).containsWithinBounds(scoreTest)
    }

    def "linear regression: OLS PCA"() {
        setup:
        def df = loadData()

        when:
        // extracting features and labels
        def X = df['atemp', 'temp', 'workingday', 'season', 'weekday'] as double[][]
        def y = df['registered'] as double[]

        def ml = Underdog.ml()

        // --8<-- [start:pca]
        // normalizing before compression
        def xStandard = ml.features.standardizeScaler(X).apply(X)

        // reducing from 5 features to 3
        def pcaScaled = PCA.fit(xStandard).getProjection(3).apply(xStandard)

        // train test split (more data for training)
        def (
                xTrain,
                xTest,
                yTrain,
                yTest
        ) = ml.utils.trainTestSplit(pcaScaled, y)

        // creating and training model
        def model = ml.regression.ols(xTrain, yTrain)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrain, yTrain).round(2)
        def scoreTest = model.score(xTest, yTest).round(2)

        print("train: ${scoreTrain}, test: ${scoreTest}")
        // --8<-- [end:pca]

        then:
        (0.60..0.70).containsWithinBounds(scoreTrain)
        (-1.05..-1.03).containsWithinBounds(scoreTest)
    }
}