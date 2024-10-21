package com.github.grooviter.underdog.examples

import com.github.grooviter.underdog.Underdog
import com.github.grooviter.underdog.smile.Smile
import spock.lang.Specification

class FirewallDetectionSpec extends Specification {
    static String CSV_PATH = "src/test/resources/com/github/grooviter/underdog/examples/firewall.csv"
    static List<String> COLUMNS = [
        'source',
        'destination',
        'nat_source',
        'nat_destination',
        'action',
        'b_total',
        'b_sent',
        'b_received',
        'packets',
        'time',
        'p_sent',
        'p_received'
    ]

    def loadData() {
        return Underdog.read_csv(CSV_PATH).rename(columns: COLUMNS).dropna()
    }

    def "classify firewall allowed vs not allowed rules"() {
        setup: "loading data"
        def df = loadData()

        and: "creating a binary classification allowed vs not allowed"
        df['allowed'] = df['action'](String, Integer){ it == 'allow' ? 1 : 0 }

        and: "droping action column"
        df = df.drop('action')

        and: "getting features and labels"
        def X = df['nat_source', 'nat_destination', 'time'] as double[][]
        def y = df['allowed'] as int[]

        and: "getting train and test datasets"
        def (xTrain, xTest, yTrain, yTest) = Smile.utils.trainTestSplit(X, y)

        and: "normalizing the scale of all features with min-max scaler"
        def minMaxScaler = Smile.features.minMaxScaler(xTrain)
        def xTrainScaled = minMaxScaler.apply(xTrain)
        def xTestScaled = minMaxScaler.apply(xTest)

        when: "creating a logistic regression classification model"
        def model = Smile.classification.logisticRegression(xTrainScaled, yTrain)
        def prediction = model.predict(xTestScaled)

        then: "we've got more than 85% of R2 score"
        Smile.metrics.accuracy(yTest, prediction) > 0.96
    }
}
