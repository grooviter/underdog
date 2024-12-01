package underdog.ml

import spock.lang.Specification

class NLPExtensionsSpec extends Specification {

    def "small text vectorization"() {
        setup:
        def df = new File("src/test/resources/data/corpus.txt")
            .readLines()
            .inject([type: [], sentence: []]){ agg, next ->
                def (type, sentence) = next.split(":")
                agg.type << type.toLowerCase()
                agg.sentence << sentence
                agg
            }.toDF("corpus")

        and: "convert type to numeric"
        df['type_id'] = df['type'].categorize()

        and: "getting features and targets"
        def X = df['sentence'].toStringArray().vectorized()
        def y = df['type_id'] as int[]

        and: "splitting train/test datasets"
        def (xTrain, xTest, yTrain, yTest) = ML.utils.trainTestSplit(X, y)

        when: "using model to predict types"
        def model = ML.classification.logisticRegression(xTrain, yTrain)
        def prediction = model.predict(xTest)

        then: "we should get a good accuracy"
        ML.metrics.accuracy(yTest, prediction) > 0.30
    }
}
