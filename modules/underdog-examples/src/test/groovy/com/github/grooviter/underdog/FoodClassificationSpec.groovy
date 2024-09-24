package com.github.grooviter.underdog

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import org.codehaus.groovy.runtime.DefaultGroovyMethods
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
        def x = features as double[][]
        def y = labels as double[]

        then:
        features.size() == labels.size()

        and: "all features have the same size"
        x.collect { it.length }.unique().size() == 1

        and: "and features size is the same as labels"
        x.collect { it.length }.unique().find() == y.length

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
            4,
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
        def tuna_olive_oil = [0, 0, 20, 33, 0.88, 0]    // expected 3
        def beer_one_liter = [3.4, 0.1, 0.3, 0, 0, 0]   // expected 2
        def coke           = [10.6, 10.6, 0, 0, 0, 0]   // expected 3
        def croissants     = [46, 4.5, 8.7, 26, 1.3, 0] // expected 3

        and:
        def xs = features as double[][]
        def ys = labels as int[]

        def (xTrain, _1, yTrain, _2) = trainTestSplit(
                features: xs,
                labels: ys,
                random_state: 0,
                shuffle: false,
                train_size: 0.5)

        def knn = KNN.fit(xTrain, yTrain, 4)

        when:
        def prediction = knn.predict([tuna_olive_oil, beer_one_liter, coke, croissants] as double[][])

        then:
        prediction == [3,2,3,3] as int[]
    }

    @NamedVariant
    private static Tuple4<double[][], double[][], int[], int[]> trainTestSplit(
            double[][] features,
            int[] labels,
            long random_state,
            boolean shuffle,
            double train_size = 0.5) {

        def samplesLength = features.length
        def indexes = (0..<samplesLength)

        if (shuffle) {
            indexes = indexes.shuffled(new Random(random_state ?: 0))
        }

        def trainSize = (samplesLength * train_size).toInteger()
        def testSize = samplesLength - trainSize

        def (double[][] xTrain, double[][] xTest) = DefaultGroovyMethods.chop(features, trainSize, testSize)
        def (int[] yTrain, int[] yTest) = DefaultGroovyMethods.chop(labels as List<Integer>, trainSize, testSize)

        return [xTrain, xTest, yTrain, yTest]
    }
}
