package com.github.grooviter.underdog

import com.github.grooviter.underdog.smile.Smile
import smile.validation.metric.Accuracy
import spock.lang.Specification
import com.github.grooviter.underdog.Underdog as ud

class MarketHousesRegressionSpec extends Specification {
    static String PRICES_CSV = "src/test/resources/com/github/grooviter/underdog/tablesaw/idealistaBCN.csv"
    static List<String> FEATURES = ['habs', 'm2', 'floor']
    static List<String> FEATURES_TO_CATEGORIZE = ['elevator', 'exterior', 'neigh', 'type']
    static List<String> ALL_FEATURES = [*FEATURES, *FEATURES_TO_CATEGORIZE]
    static String TARGET = 'price'

    DataFrame loadCSV() {
        return ud.read_csv(path: PRICES_CSV)
    }

    DataFrame loadDataFrame() {
        def df = loadCSV()
            .loc[__, [*FEATURES, *FEATURES_TO_CATEGORIZE, TARGET]]
            .dropna()

        for (String featureToCategorize : FEATURES_TO_CATEGORIZE) {
            df[featureToCategorize] = df[featureToCategorize].categorize()
        }

        return df
    }

    def "Question 0: check csv data loading works"() {
        expect:
        loadCSV().size() == 1290
    }

    def "Question 1: choose features"() {
        when:
        def df = loadDataFrame()

        then:
        df.size() == 939

        and:
        df.columns.size() == 8
    }

    def "Question 2: use logistic regression"() {
        setup: "getting dataframe"
        def df = loadDataFrame()

        and: "creating target and features datasets"
        def X = df[ALL_FEATURES] as double[][]
        def y = df[TARGET] as int[]

        and: "splitting dataset between train and test datasets"
        def (xTrain, xTest, yTrain, yTest) = Smile.utils.trainTestSplit(X, y)

        when: "creating the model from training datasets"
        def model = Smile.classification.logisticRegression(xTrain, yTrain, C: 1.0)

        and: "using prediction against test dataset"
        def prediction = model.predict(xTest)

        then: "the prediction accuracy is really bad because is between 0.01 and 0.02"
        (0.01..0.03).containsWithinBounds(Accuracy.of(yTest, prediction))
    }

    def "Question 3: use Ridge regression"() {
        setup: "getting dataframe"
        def df = loadDataFrame()

        and: "creating target and features datasets"
        def X = df[ALL_FEATURES] as double[][]
        def y = df[TARGET] as double[]

        and: "splitting dataset between train and test datasets"
        def (xTrain, xTest, yTrain, yTest) = Smile.utils.trainTestSplit(X, y)

        when: "creating the model from training datasets"
        def model = Smile.regression.ridge(xTrain, yTrain, alpha: 0.0057)

        and: "using prediction against test dataset"
        def prediction = model.predict(xTest)

        and: "getting r2-score"
        def score = Smile.validation.scoreR2(yTest, prediction)

        then: "the prediction accuracy is really bad because is between 0.01 and 0.02"
        (0.79..0.80).containsWithinBounds(score)
    }

    def "Question 4: use Lasso regression"() {
        setup: "getting dataframe"
        def df = loadDataFrame()

        and: "creating target and features datasets"
        def X = df[ALL_FEATURES] as double[][]
        def y = df[TARGET] as double[]

        and: "splitting dataset between train and test datasets"
        def (xTrain, xTest, yTrain, yTest) = Smile.utils.trainTestSplit(X, y)

        when: "creating the model from training datasets"
        def model = Smile.regression.lasso(xTrain, yTrain, alpha: 2.0, maxIterations: 10_000)

        and: "using prediction against test dataset"
        def prediction = model.predict(xTest)

        and: "getting r2-score"
        def score = Smile.validation.scoreR2(yTest, prediction)

        then: "the prediction accuracy is really bad because is between 0.01 and 0.02"
        (0.79..0.80).containsWithinBounds(score)
    }

    def "Question 4: use OLS regression"() {
        setup: "getting dataframe"
        def df = loadDataFrame()

        and: "creating target and features datasets"
        def X = df[ALL_FEATURES] as double[][]
        def y = df[TARGET] as double[]

        and: "splitting dataset between train and test datasets"
        def (xTrain, xTest, yTrain, yTest) = Smile.utils.trainTestSplit(X, y)

        when: "creating the model from training datasets"
        def model = Smile.regression.ols(xTrain, yTrain, method: method, stderr: stderr, recursive: recursive)

        and: "using prediction against test dataset"
        def prediction = model.predict(xTest)

        and: "getting r2-score"
        def score = Smile.validation.scoreR2(yTest, prediction)

        then: "the prediction accuracy is really bad because is between 0.01 and 0.02"
        (0.79..0.80).containsWithinBounds(score)

        where:
        method | stderr | recursive
        'qr'   | true   | true
        'qr'   | false  | true
        'qr'   | true   | false
        'svd'  | true   | true
        'svd'  | false  | true
        'svd'  | true   | false
    }
}
