package com.github.grooviter.underdog.smile

class ValidationSpec extends BaseSpec {
    def "confusion matrix"() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = multiClassificationTrainTestSplit()

        when:
        def model = Smile.classification.randomForest(xTrain, yTrain)
        def prediction = model.predict(xTest)

        and:
        def result = Smile.validation.confusionMatrix(yTest, prediction)

        then:
        result.matrix.length == 4
    }
}
