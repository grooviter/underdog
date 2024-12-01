package underdog.ml

import smile.math.kernel.GaussianKernel
import smile.math.kernel.HyperbolicTangentKernel
import smile.math.kernel.LinearKernel
import smile.math.kernel.PearsonKernel
import smile.math.kernel.PolynomialKernel


class ClassificationSpec extends BaseSpec {
    def "knn"() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = binaryClassificationTrainTestSplit()

        when:
        def model = ML.classification.knn(xTrain, yTrain, k: 3)
        def prediction = model.predict(xTest)

        then:
        (0.80..0.85).containsWithinBounds(ML.metrics.accuracy(yTest, prediction))
    }

    def "logistic regression"() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = binaryClassificationTrainTestSplit()

        when:
        def model = ML.classification.logisticRegression(xTrain, yTrain, C: 2.0)
        def prediction = model.predict(xTest)

        then:
        (0.90..0.95).containsWithinBounds(ML.metrics.accuracy(yTest, prediction))
    }

    def "svc"() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = binaryClassificationTrainTestSplit()

        when:
        def model = ML.classification.svc(xTrain, yTrain, kernel: kernel, C: 0.01)
        def prediction = model.predict(xTest)

        then:
        range.containsWithinBounds(ML.metrics.accuracy(yTest, prediction))

        where:
        kernel                                | range
        new LinearKernel()                    | 0.80..0.90
        new PolynomialKernel(3)               | 0.40..0.90
        new GaussianKernel(0.1)               | 0.40..0.60
        new PearsonKernel(0.1, 0.0)           | 0.40..0.60
        new HyperbolicTangentKernel(0.1, 0.0) | 0.40..0.60
    }

    def "decision tree"() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = multiClassificationTrainTestSplit()

        when:
        def model = ML.classification.decisionTree(xTrain, yTrain, maxDepth: 4)
        def prediction = model.predict(xTest)

        then:
        (0.70..0.80).containsWithinBounds(ML.metrics.accuracy(yTest, prediction))
    }

    def "random forest"() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = multiClassificationTrainTestSplit()

        when:
        def model = ML.classification.randomForest(xTrain, yTrain, maxDepth: 4)
        def prediction = model.predict(xTest)

        then:
        (0.80..0.90).containsWithinBounds(ML.metrics.accuracy(yTest, prediction))
    }

    def "gradient boost"() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = binaryClassificationTrainTestSplit([0,1])

        when:
        def model = ML.classification.gradientBoost(xTrain, yTrain, maxDepth: 4)
        def prediction = model.predict(xTest)

        then:
        (0.85..0.98).containsWithinBounds(ML.metrics.accuracy(yTest, prediction))
    }

    def "multi-layer-perceptron"() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = binaryClassificationTrainTestSplit([0,1])

        when:
        def model = ML.classification.multilayerPerceptron(xTrain, yTrain)
        def prediction = model.predict(xTest)

        then:
        (0.85..0.90).containsWithinBounds(ML.metrics.accuracy(yTest, prediction))
    }
}
