package com.github.grooviter.underdog.smile

import java.math.RoundingMode

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
        int[] truth = [1, 1, 1, 1, 0, 1, 0, 1]
        int[] prediction = [1, 1, 1, 0, 0, 1, 0, 1]

        when:
        def result = Smile.metrics.binaryClassificationReport(truth, prediction)

        then:
        result.class_0['accuracy'] == 1.0
        result.class_1['accuracy'] == 0.8333333333333334

        and:
        result.class_0['count'] == 2
        result.class_1['count'] == 6
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
        result.healthy['accuracy'] == 1.0
        result.no_healthy['accuracy'] == 0.8333333333333334

        and:
        result.healthy['count'] == 2
        result.no_healthy['count'] == 6
    }
}
