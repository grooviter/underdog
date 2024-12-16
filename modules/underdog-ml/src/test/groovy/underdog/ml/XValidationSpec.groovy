package underdog.ml

import spock.lang.Ignore

@Ignore('some cross validations fail randomly')
class XValidationSpec extends BaseSpec {
    def 'cross validation [#tag]'() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = data

        when:
        def validation = ML.crossValidation.classification(xTrain, yTrain, trainer)

        then:
        validation.avg.accuracy > rate

        where:
        tag             | data                                       | trainer                                  | rate
        'logistic'      | multiClassificationTrainTestSplit()        | ML.classification::logisticRegression | 0.80
        'knn'           | multiClassificationTrainTestSplit()        | ML.classification::knn                | 0.80
        'lda'           | multiClassificationTrainTestSplit()        | ML.classification::lda                | 0.80
        'decision-tree' | multiClassificationTrainTestSplit()        | ML.classification::decisionTree       | 0.80
        'random-forest' | binaryClassificationTrainTestSplit([0, 1]) | ML.classification::randomForest       | 0.80
        'svc'           | binaryClassificationTrainTestSplit()       | ML.classification::svc                | 0.20
    }
}
