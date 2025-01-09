package underdog.blog.y2024.m12

import underdog.DataFrame

import java.time.LocalDate

// --8<-- [start:import_json]
import static groovy.json.JsonOutput.toJson
import static groovy.json.JsonOutput.prettyPrint
// --8<-- [end:import_json]
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
        // features (X) and labels (y)
        def X = df['temp'] as double[][]
        def y = df['registered'] as double[]

        def ml = Underdog.ml()

        // train test split (0.75 training, 0.25 test)
        def (
            xTrain,
            xTest,
            yTrain,
            yTest
        ) = ml.utils.trainTestSplit(X, y)

        // model creation and training
        def model = ml.regression.ols(xTrain, yTrain)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrain, yTrain).round(6)
        def scoreTest = model.score(xTest, yTest).round(6)

        print("train: ${scoreTrain}, test: ${scoreTest}")
        // --8<-- [end:linear_regression]

        // --8<-- [start:linear_regression_plot]
        def plt = Options.create {
            title {
                text('Linear Regression (Least Squares - No Polynomial)')
                left('center')
                top('5%')
            }
            xAxis {
                name('temp')
                nameGap(25)
                nameLocation('center')
                show(true)
            }
            yAxis {
                name('registered')
                nameGap(50)
                nameLocation('center')
                show(true)
            }
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
        (0.30..0.40).containsWithinBounds(scoreTrain)
        (0.20..0.25).containsWithinBounds(scoreTest)
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

        // train test split (more data for training)
        def (
            xTrain,
            xTest,
            yTrain,
            yTest
        ) = ml.utils.trainTestSplit(xPoly, y)

        // creating and training model
        def model = ml.regression.ols(xTrain, yTrain)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrain, yTrain).round(6)
        def scoreTest = model.score(xTest, yTest).round(6)

        print("train: ${scoreTrain}, test: ${scoreTest}")
        // --8<-- [end:linear_regression_polynomial]

        // --8<-- [start:linear_regression_polynomial_plot]
        // building chart
        def plt = Options.create {
            title {
                text('Linear Regression (Least Squares - Polynomial)')
                left('center')
                top('5%')
            }
            xAxis {
                name('temp')
                nameGap(25)
                nameLocation('center')
                show(true)
            }
            yAxis {
                name('registered')
                nameGap(50)
                nameLocation('center')
                show(true)
            }
            // SCATTER PLOT
            series(ScatterSeries){
                data(toData(X, y))
            }
            // REGRESSION LINES
            def colors = ['gray', 'green', 'brown'].indexed()

            (0..<xTest.shape().cols).each {feature ->
                series(LineSeries) {
                    smooth(true)
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
        (0.35..0.36).containsWithinBounds(scoreTrain)
        (0.29..0.30).containsWithinBounds(scoreTest)
    }

    def "regularization & normalization: baseline"() {
        setup:
        def df = loadData()

        when:
        // --8<-- [start:baseline]
        def X = df['atemp', 'temp', 'workingday', 'season', 'weekday'] as double[][]
        def y = df['registered'] as double[]
        def ml = Underdog.ml()

        // train test split
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(X, y)

        // creating and training model
        def model = ml.regression.ols(xTrain, yTrain)

        // getting scores
        def scoreTrain = model.score(xTrain, yTrain).round(6)
        def scoreTest = model.score(xTest, yTest).round(6)

        print("train: ${scoreTrain}, test: ${scoreTest}")
        // --8<-- [end:baseline]

        then:
        (0.46..0.47).containsWithinBounds(scoreTrain)
        (0.31..0.32).containsWithinBounds(scoreTest)
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
        def model = ml.regression.ridge(xTrain, yTrain, alpha: 40)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrain, yTrain).round(6)
        def scoreTest = model.score(xTest, yTest).round(6)

        print("train: ${scoreTrain}, test: ${scoreTest}")
        // --8<-- [end:ridge_regression]

        then:
//        (0.45..0.50).containsWithinBounds(scoreTrain)
        (0.30..0.35).containsWithinBounds(scoreTest)
    }

    def "regularization & normalization: Ridge (MinMaxScaler)"() {
        setup:
        def df = loadData()

        when:
        def X = df['atemp', 'temp', 'workingday', 'season', 'weekday'] as double[][]
        def y = df['registered'] as double[]
        def ml = Underdog.ml()

        // --8<-- [start:ridge_regression_min_max]
        def (
            xTrain,
            xTest,
            yTrain,
            yTest
        ) = ml.utils.trainTestSplit(X, y)

        // NORMALIZATION: training scaler with training set
        def minMaxTransformation = ml.features.minMaxScaler(xTrain)

        // NORMALIZATION: transforming training set
        def xTrainScaled = minMaxTransformation.apply(xTrain)

        // NORMALIZATION: transforming testing set
        def xTestScaled = minMaxTransformation.apply(xTest)

        // creating and training model
        def model = ml.regression.ridge(xTrainScaled, yTrain, alpha: 40)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrainScaled, yTrain).round(6)
        def scoreTest = model.score(xTestScaled, yTest).round(6)

        print("train: ${scoreTrain}, test: ${scoreTest}")
        // --8<-- [end:ridge_regression_min_max]

        then:
        (0.45..0.46).containsWithinBounds(scoreTrain)
        (0.32..0.33).containsWithinBounds(scoreTest)
    }

    def "feature extraction: Lasso"() {
        setup:
        def df = loadData()

        when:
        // --8<-- [start:lasso_feature_extraction]
        // taking all available features but the one for labelling
        def featureNames = df.columns - "registered"

        def X = df[featureNames] as double[][]
        def y = df['registered'] as double[]

        // creating and training model
        def model = Underdog.ml().regression.lasso(X, y)

        // extracting coefficients
        def coefficients = model.coefficients()
        def featNamesAndCoefficients = [featureNames, coefficients].transpose().collectEntries()

        println(prettyPrint(toJson(featNamesAndCoefficients)))
        // --8<-- [end:lasso_feature_extraction]

        // --8<-- [start:best_features]
        def bestFeatures = featNamesAndCoefficients
            .findAll { it.value > 0 } // filtering all features w/ coefficient > 0
            .sort { -it.value } // sorting by coefficient (desc)
            *.key as List<String> // getting feature names

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
        (0.35..0.40).containsWithinBounds(scoreTest)
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
        def model = ml.regression.lasso(xTrain, yTrain, alpha: 40)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrain, yTrain).round(6)
        def scoreTest = model.score(xTest, yTest).round(6)

        print("train: ${scoreTrain}, test: ${scoreTest}")
        // --8<-- [end:lasso_regression]

        then:
        (0.45..0.50).containsWithinBounds(scoreTrain)
        (0.30..0.35).containsWithinBounds(scoreTest)
    }

    def "regularization & normalization: Lasso (MinMaxScaler)"() {
        setup:
        def df = loadData()

        when:
        def X = df['atemp', 'temp', 'workingday', 'season', 'weekday'] as double[][]
        def y = df['registered'] as double[]
        def ml = Underdog.ml()

        // --8<-- [start:lasso_regression_min_max]
        // train test split
        def (
                xTrain,
                xTest,
                yTrain,
                yTest
        ) = ml.utils.trainTestSplit(X, y)

        // normalization
        def minMaxTransformation = ml.features.minMaxScaler(xTrain)
        def xTrainScaled = minMaxTransformation.apply(xTrain)
        def xTestScaled = minMaxTransformation.apply(xTest)

        // creating and training model
        def model = ml.regression.lasso(xTrainScaled, yTrain, alpha: 40)

        def scoreTrain = model.score(xTrainScaled, yTrain).round(6)
        def scoreTest = model.score(xTestScaled, yTest).round(6)

        print("train: ${scoreTrain}, test: ${scoreTest}")
        // --8<-- [end:lasso_regression_min_max]
        then:
        (0.46..0.47).containsWithinBounds(scoreTrain)
        (0.31..0.32).containsWithinBounds(scoreTest)
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
        // train test split
        def (
            xTrain,
            xTest,
            yTrain,
            yTest
        ) = ml.utils.trainTestSplit(X, y)

        // normalization
        def normalization = ml.features.standardizeScaler(xTrain)
        //      |
        //      v
        def xTrainScaled = normalization.apply(xTrain)
        def xTestScaled = normalization.apply(xTest)

        // compression
        def compression = ml.features.pca(xTrainScaled, 4)
        //      |
        //      v
        def xTrainScaledReduced = compression.apply(xTrainScaled)
        def xTestScaledReduced = compression.apply(xTestScaled)

        // creating and training model
        def model = ml.regression.ols(xTrainScaledReduced, yTrain)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrainScaledReduced, yTrain).round(6)
        def scoreTest = model.score(xTestScaledReduced, yTest).round(6)

        print("train: ${scoreTrain}, test: ${scoreTest}")
        // --8<-- [end:pca]

        then:
        (0.46..0.47).containsWithinBounds(scoreTrain)
        (0.31..0.32).containsWithinBounds(scoreTest)
    }
}