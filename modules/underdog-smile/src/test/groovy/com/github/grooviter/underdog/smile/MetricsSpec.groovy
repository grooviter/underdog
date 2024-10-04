package com.github.grooviter.underdog.smile

class MetricsSpec extends BaseSpec {
    def "confusion matrix"() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = multiClassificationTrainTestSplit()

        when:
        def model = Smile.classification.randomForest(xTrain, yTrain)
        def prediction = model.predict(xTest)

        and:
        def result = Smile.metrics.confusionMatrix(yTest, prediction)

        then:
        result.matrix.length == 4
    }

    def "classification report"() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = binaryClassificationTrainTestSplit([1, 0])
        def model = Smile.classification.logisticRegression(xTrain, yTrain)
        def prediction = model.predict(xTest)

        when:
        def result = Smile.metrics.binaryClassificationReport(yTest, prediction)

        then:
        result.positive['accuracy'].toBigDecimal().round(2) == 0.90
        result.negative['accuracy'].toBigDecimal().round(2) == 0.93

        and:
        result.positive['count'] == 205
        result.negative['count'] == 302
        result.totals['count'] == 507
    }

    def "classification report with target names"() {
        setup:
        int[] truth = [1, 1, 1, 1, 0, 1, 0, 1]
        int[] prediction = [1, 1, 1, 0, 0, 1, 0, 1]

        when:
        def result = Smile.metrics.binaryClassificationReport(
                truth,
                prediction,
                targetNames: ['healthy', 'no_healthy'])

        then:
        result.positive['accuracy'] == 1.0
        result.negative['accuracy'] == 0.8333333333333334

        and:
        result.positive['count'] == 2
        result.negative['count'] == 6
        result.totals['count'] == 8
    }
}
