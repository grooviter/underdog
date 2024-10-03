package com.github.grooviter.underdog.smile

class XValidationSpec extends BaseSpec {
    def 'cross validation [#tag]'() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = data

        when:
        def validation = Smile.crossValidation.classification(xTrain, yTrain, trainer)

        then:
        validation.avg.accuracy > rate

        where:
        tag             | data                                 | trainer                                  | rate
        'logistic'      | multiClassificationTrainTestSplit()  | Smile.classification::logisticRegression | 0.80
        'knn'           | multiClassificationTrainTestSplit()  | Smile.classification::knn                | 0.80
        'lda'           | multiClassificationTrainTestSplit()  | Smile.classification::lda                | 0.80
        'decision-tree' | multiClassificationTrainTestSplit()  | Smile.classification::decisionTree       | 0.80
        'random-forest' | multiClassificationTrainTestSplit()  | Smile.classification::randomForest       | 0.80
        'svc'           | binaryClassificationTrainTestSplit() | Smile.classification::svc                | 0.20
    }
}
