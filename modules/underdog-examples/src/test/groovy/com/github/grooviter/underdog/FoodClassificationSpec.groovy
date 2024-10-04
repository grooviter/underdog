package com.github.grooviter.underdog

import com.github.grooviter.underdog.smile.Smile
import smile.classification.KNN
import smile.validation.CrossValidation
import spock.lang.Specification
import com.github.grooviter.underdog.Underdog as ud

class FoodClassificationSpec extends Specification {
    static FOOD_CSV = "src/test/resources/com/github/grooviter/underdog/tablesaw/food.csv"

    static List<String> COLUMNS_OF_INTEREST = [
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

    static List<String> FEATURE_COLUMNS = ['CARBS', 'SUGAR', 'PROTEINS', 'FAT', 'SALT', 'FIBER']
    static String LABEL_COLUMN = 'TRAFFICLIGHT VALUE'

    DataFrame getFood() {

        return ud.read_csv(path: FOOD_CSV, sep: ';')
    }

    DataFrame choosingFeatures() {
        return food.loc[__, COLUMNS_OF_INTEREST].dropna()
    }

    Tuple2<DataFrame, Series> getFeaturesAndLabels() {
        def df = choosingFeatures()
        def feats = df[FEATURE_COLUMNS]
        def label = df[LABEL_COLUMN]
        return [feats, label]
    }

    def "Step 0: check initial dataframe"() {
        setup:
        def df = food

        expect:
        df.size() == 3197

        and:
        df.columns.size() == 20
    }

    def "Step 1: fill missing values with 0"() {
        when:
        def df = choosingFeatures()

        then:
        df.columns == COLUMNS_OF_INTEREST

        and:
        df.size() < food.size()
    }

    def "Step 2: picking the right features and labels"() {
        when:
        def (features, labels) = featuresAndLabels

        and:
        def X = features as double[][]
        def y = labels as double[]

        then:
        features.size() == labels.size()

        and: "all features have the same size"
        X.collect { it.length }.unique().size() == 1

        and: "and features size is the same as labels"
        X.length == y.length

        and:
        features.columns == FEATURE_COLUMNS
    }

    def "Step 3: checking k=4 for training"() {
        setup:
        def (features, labels) = featuresAndLabels

        and:
        def xs = features as double[][]
        def ys = labels as int[]

        when:
        def classifier = CrossValidation.classification(
            10,
            5,
            xs,
            ys,
            (x, y) -> KNN.fit(x, y))
        then:
        classifier.avg.accuracy > 0.86
    }

    def "Step 3: testing prediction"() {
        setup:
        def (features, labels) = featuresAndLabels

        and:
        def X = features as double[][]
        def y = labels as int[]

        def (xTrain, xTest, yTrain, yTest) = Smile.utils.trainTestSplit(
            X,
            y,
            shuffle: true)

        and: "select the sensitivity with better average accuracy to train data"
        def (sensitivity, _) = bestKNNAccuracyAvg(xTrain, yTrain, 3..5)
        def knn = Smile.classification.knn(xTrain, yTrain, k: sensitivity)

        when:
        def prediction = knn.predict(xTest)
        def predictionScore = Smile.metrics.R2Score(yTest, prediction)

        then: "using r-squared to see that the prediction is"
        (0.58..0.59).containsWithinBounds(predictionScore)
    }

    private static Tuple2<Integer, Double> bestKNNAccuracyAvg(double[][] xs, int[] ys, IntRange sensitivities) {
        List<Tuple2<Integer, Double>> results = sensitivities.collect { Integer sensitivity ->
            def classifier = CrossValidation.classification(
                    10,
                    sensitivity,
                    xs,
                    ys,
                    (x, y) -> KNN.fit(x, y))

            return [sensitivity, classifier.avg.accuracy] as Tuple2<Integer, Double>
        }

        return results.max { it.last() } as Tuple2<Integer, Double>
    }
}
