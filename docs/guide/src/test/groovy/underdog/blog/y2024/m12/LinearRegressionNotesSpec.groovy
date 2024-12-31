package underdog.blog.y2024.m12

import underdog.DataFrame

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

// https://www.kaggle.com/datasets/ironwolf437/laptop-price-dataset/data
class LinearRegressionNotesSpec extends Specification {

    static final String FILE_PATH = "src/test/resources/data/blog/2024/12/laptop_price.csv"

    def "correlation"() {
        when:
        def df = Underdog.df()
            .read_csv(FILE_PATH, maxCharsPerColumn: 5000)
            .dropConstantSeries()
            .dropNonNumericalColumns()
            .dropna()

        // --8<-- [start:correlation_matrix]
        def plot = Underdog
            .plots()
            .correlationMatrix(df)

        plot.show()
        // --8<-- [end:correlation_matrix]

        then:
        plot
    }

    private static DataFrame loadData() {
        // --8<-- [start:load_data]
        def data = Underdog.df()
            .read_csv(FILE_PATH)
            .dropConstantSeries()
            .dropNonNumericalColumns()
            .dropna()
            .renameSeries(mapper: [
                'RAM (GB)': 'ram',
                'Price (Euro)': 'price',
                'CPU_Frequency (GHz)': 'cpu',
                'Weight (kg)': 'weight'
            ])
        // --8<-- [end:load_data]
        return data
    }

    def "pair plot"() {
        when:
        def df = loadData()

        // --8<-- [start:pair_plot]
        def plot = Underdog
            .plots()
            .scatterMatrix(df['ram', 'price'])

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
        def X = df.loc[__, ['ram']] as double[][]
        def y = df['price'] as double[]

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
                        xTrain, // x1, x2,...xn
                        yTrain  // y1, y2,...yn
                    ) // [[x1,y1], [x2,y2],...[xn, yn]]
                )
            }
            // REGRESSION LINE
            series(LineSeries) {
                data(
                    toData(
                        sortX(xTest),
                        model.predict(sortX(xTest))
                    )
                )
            }
        }

        plt.show()
        // --8<-- [end:linear_regression_plot]
        Plots.show(plt, theme: 'dark')
        then:
        (0.95..0.99).containsWithinBounds(scoreTrain)
        (0.95..0.99).containsWithinBounds(scoreTest)
    }

    def "linear regression: OLS polynomial features"() {
        setup:
        def day = loadData()

        when:
        // extracting features and labels
        def X = day.loc[__, ['ram']] as double[][]
        def y = day['price'] as double[]
        def ml = Underdog.ml()

        // --8<-- [start:linear_regression_polynomial]
        // transforming X adding new generated features
        def xPoly = ml.features.polynomialFeatures(X, degree: 3)

        // train test split (more data for training)
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(xPoly, y)

        // creating and training model
        def model = ml.regression.ols(xTrain, yTrain)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrain, yTrain).round(2)
        def scoreTest = model.score(xTest, yTest).round(2)

        print("train: ${scoreTrain}, test: ${scoreTest}")
        // --8<-- [end:linear_regression_polynomial]

        // --8<-- [start:linear_regression_polynomial_plot]
        // color for each degree
        def colors = [0: 'orange', 1: 'green', 2: 'red']

        // getting every degree X values
        def xPointsByDegree = (0..<3).collect { degree ->
            xTrain.collect { feature -> feature[degree] }
        }

        // building chart
        def plt = Options.create {
            xAxis { show(true) }
            yAxis { show(true) }
            // SCATTER PLOT
            series(ScatterSeries){
                // using only degree 0 to be able to compare with
                // previous NON polynomial chart
                data(toData(xPointsByDegree[0], yTrain))
            }
            // REGRESSION LINES
            xPointsByDegree.eachWithIndex { degreeX, index ->
                series(LineSeries) {
                    data(toData(degreeX, model.predict(xTrain)))
                    itemStyle {
                        color(colors[index])
                    }
                }
            }
        }

        // showing chart
        plt.show()
        // --8<-- [end:linear_regression_polynomial_plot]
        Plots.show(plt, theme: 'dark')

        then:
        (0.35..0.40).containsWithinBounds(scoreTrain)
        (0.20..0.25).containsWithinBounds(scoreTest)
    }

    def "linear regression: OLS PCA"() {
        setup:
        def day = loadData()

        when:
        // extracting features and labels
        def X = day as double[][]
        def y = day['price'] as double[]

        def pca = PCA.fit(X).getProjection(3)
        def xScaled = pca.apply(X)

        def ml = Underdog.ml()

        // train test split (more data for training)
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(xScaled, y)

        // creating and training model
        def model = ml.regression.ols(xTrain, yTrain)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrain, yTrain)
        def scoreTest = model.score(xTest, yTest)

        print("train: ${scoreTrain.round(2)}, test: ${scoreTest.round(2)}")

        then:
        (0.25..0.27).containsWithinBounds(scoreTrain)
        (0.12..0.13).containsWithinBounds(scoreTest)
    }

    def "regularization & normalization: Ridge"() {
        setup:
        def day = loadData()

        when:
        def X = day.loc[__, ['ram']] as double[][]
        def y = day['price'] as double[]
        def ml = Underdog.ml()

        // train test split (more data for training)
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(X, y)

        // --8<-- [start:ridge_regression]
        // creating and training model (RIDGE)
        def model = ml.regression.ridge(xTrain, yTrain, alpha: 20)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrain, yTrain)
        def scoreTest = model.score(xTest, yTest)

        print("train: ${scoreTrain.round(2)}, test: ${scoreTest.round(2)}")
        // --8<-- [end:ridge_regression]

        then:
        (0.30..0.40).containsWithinBounds(scoreTrain)
        (0.1..0.2).containsWithinBounds(scoreTest)
    }

    def "regularization & normalization: Ridge (MinMaxScaler)"() {
        setup:
        def day = loadData()

        when:
        def X = day.loc[__, ['ram']] as double[][]
        def y = day['price'] as double[]
        def ml = Underdog.ml()

        // train test split (more data for training)
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(X, y)

        // --8<-- [start:ridge_regression_min_max]
        // scaling xTrain and xTest
        def scaler = ml.features.minMaxScaler(xTrain)

        def xTrainScaled = scaler.apply(xTrain)
        def xTestScaled = scaler.apply(xTest)

        // creating and training model (RIDGE w/ minMax scaler)
        def model = ml.regression.ridge(xTrainScaled, yTrain, alpha: 20)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrainScaled, yTrain)
        def scoreTest = model.score(xTestScaled, yTest)

        print("train: ${scoreTrain.round(2)}, test: ${scoreTest.round(2)}")
        // --8<-- [end:ridge_regression_min_max]

        then:
        true
    }

    def "regularization & normalization: Lasso"() {
        setup:
        def day = loadData()

        when:
        def X = day.loc[__, ['ram']] as double[][]
        def y = day['price'] as double[]
        def ml = Underdog.ml()

        // train test split
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(X, y)

        // --8<-- [start:lasso_regression]
        def model = ml.regression.lasso(xTrain, yTrain, alpha: 20)

        // predicting and getting r2_score for training and test sets
        def scoreTrain = model.score(xTrain, yTrain)
        def scoreTest = model.score(xTest, yTest)

        print("train: ${scoreTrain.round(2)}, test: ${scoreTest.round(2)}")
        // --8<-- [end:lasso_regression]

        then:
        true
    }

    def "regularization & normalization: Lasso (MinMaxScaler)"() {
        setup:
        def day = loadData()

        when:
        def X = day.loc[__, ['ram']] as double[][]
        def y = day['price'] as double[]
        def ml = Underdog.ml()

        // train test split
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(X, y)

        // --8<-- [start:lasso_regression_min_max]
        // scaling xTrain and xTest
        def scaler = ml.features.minMaxScaler(X)

        def xTrainScaled = scaler.apply(xTrain)
        def xTestScaled = scaler.apply(xTest)

        // creating and training model (LASSO w/ minMax scaler)
        def model = ml.regression.lasso(xTrainScaled, yTrain, alpha: 20)

        def scoreTrain = model.score(xTrainScaled, yTrain)
        def scoreTest = model.score(xTestScaled, yTest)

        print("train: ${scoreTrain.round(2)}, test: ${scoreTest.round(2)}")
        // --8<-- [end:lasso_regression_min_max]
        then:
        true
    }

    def "feature extraction: Lasso"() {
        setup:
        def laptops = loadData()

        when:
        // --8<-- [start:lasso_feature_extraction]
        def features = laptops.columns - "price"

        def X = laptops[features] as double[][]
        def y = laptops['price'] as double[]

        // creating lasso model
        def model = Underdog.ml().regression.lasso(X, y, alpha: 20)

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
        bestFeatures == ['cpu', 'ram', 'weight']
    }

    def "linear regression: best features"() {
        def day = loadData()

        when:
        def X = day['cpu', 'ram', 'weight'] as double[][]
        def y = day['price'] as double[]

        def ml = Underdog.ml()

        and:
        def (xTrain, xTest, yTrain, yTest) = ml.utils.trainTestSplit(X, y, train_size: 0.2)

        and:
        def model = ml.regression.ols(xTrain, yTrain)
        def scoreTest = model.score(xTest, yTest)

        then:
        (0.10..0.20).containsWithinBounds(scoreTest)
    }
}