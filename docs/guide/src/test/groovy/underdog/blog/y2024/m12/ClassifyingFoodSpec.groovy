package underdog.blog.y2024.m12

import spock.lang.Specification
// --8<-- [start:imports]
import underdog.Underdog
import underdog.plots.Plots

// --8<-- [end:imports]
class ClassifyingFoodSpec extends Specification {

    def "classifying food"() {
        setup:
        // --8<-- [start:read_csv]
        def food = Underdog.df()
            .read_csv("src/test/resources/data/blog/2024/12/classifying_food.csv", sep: ';')
            .fillna(0)

        println(food.head())
        // --8<-- [end:read_csv]

        when:
        // --8<-- [start:narrowing_dataframe]
        def COLUMNS_OF_INTEREST = [
            "TRAFFICLIGHT VALUE",
            "CARBS",
            "SUGAR",
            "ENERGY",
            "PROTEINS",
            "SATURATED FAT",
            "FAT",
            "SODIUM",
            "FIBER",
            "SALT",
        ]

        def df = food.copy()
            .loc[__, COLUMNS_OF_INTEREST]
            .dropna()

        println(df.head())
        // --8<-- [end:narrowing_dataframe]

        and:
        // --8<-- [start:train_test_split]
        def feats = df['CARBS', 'SUGAR', 'PROTEINS', 'FAT', 'SALT', 'FIBER'] as double[][]
        def label = df['TRAFFICLIGHT VALUE'] as int[]

        def (X_train, X_test, y_train, y_test) = Underdog.ml()
                .utils
                .trainTestSplit(feats, label, random_state: 0)
        // --8<-- [end:train_test_split]

        and:
        // --8<-- [start:scatter_matrix]
        def plot = Underdog.plots()
            .scatterMatrix(
                X_train,
                labels: ["CARBS", "SUGAR", "PROTEINS", "FAT", "SALT", "FIBER"],
                histogramBins: 15)

        plot.show()
        // --8<-- [end:scatter_matrix]
        Plots.show(plot, theme: "dark")

        and:
        // --8<-- [start:knn_predictions]
        def ml = Underdog.ml()
        def knn = ml.classification.knn(X_train, y_train, k: 5)
        def predictions = knn.predict(X_test)
        def score = ml.metrics.r2Score(y_test, predictions)
        // --8<-- [end:knn_predictions]

        and:
        // --8<-- [start:food_samples_predictions]
        // def sample      = [CARBS, SUGAR, PROTEINS, FAT, SALT, FIBER]
        def tuna_olive_oil = [0, 0, 20, 33, 0.88, 0]    // expected 3
        def beer_one_liter = [3.4, 0.1, 0.3, 0, 0, 0]   // expected 2
        def coke           = [10.6, 10.6, 0, 0, 0, 0]   // expected 3
        def croissants     = [46, 4.5, 8.7, 26, 1.3, 0] // expected 3

        def sample_predictions = knn.predict([
            tuna_olive_oil,
            beer_one_liter,
            coke,
            croissants] as double[][])
        // --8<-- [end:food_samples_predictions]

        then:
        // --8<-- [start:food_samples_predictions_expectations]
        sample_predictions == [3, 2, 3, 3]
        // --8<-- [end:food_samples_predictions_expectations]
    }
}
