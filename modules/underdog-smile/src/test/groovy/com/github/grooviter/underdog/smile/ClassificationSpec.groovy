package com.github.grooviter.underdog.smile


class ClassificationSpec extends BaseSpec {
    def "knn"() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = binaryClassificationTrainTestSplit()

        when:
        def model = Smile.classification.knn(xTrain, yTrain, k: 3)
        def prediction = model.predict(xTest)

        then:
        (0.34..0.35).containsWithinBounds(Smile.validation.scoreR2(yTest, prediction))
    }

    def "logistic regression"() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = binaryClassificationTrainTestSplit()

        when:
        def model = Smile.classification.logisticRegression(xTrain, yTrain, C: 2.0)
        def prediction = model.predict(xTest)

        then:
        (0.65..0.66).containsWithinBounds(Smile.validation.scoreR2(yTest, prediction))
    }

    def "svc"() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = binaryClassificationTrainTestSplit()

        when:
        def model = Smile.classification.svc(xTrain, yTrain)
        def prediction = model.predict(xTest)

        then:
        (0.09..0.10).containsWithinBounds(Smile.validation.scoreR2(yTest, prediction))
    }

    def "decision tree"() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = multiClassificationTrainTestSplit()

        when:
        def model = Smile.classification.decisionTree(xTrain, yTrain, maxDepth: 4)
        def prediction = model.predict(xTest)

        then:
        (0.63..0.64).containsWithinBounds(Smile.validation.scoreR2(yTest, prediction))
    }

    def "random forest"() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = multiClassificationTrainTestSplit()

        when:
        def model = Smile.classification.randomForest(xTrain, yTrain, maxDepth: 4)
        def prediction = model.predict(xTest)

        then:
        (0.75..0.76).containsWithinBounds(Smile.validation.scoreR2(yTest, prediction))
    }
}
