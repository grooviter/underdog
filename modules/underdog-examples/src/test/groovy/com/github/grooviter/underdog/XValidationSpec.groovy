package com.github.grooviter.underdog

import com.github.grooviter.underdog.smile.Smile

class XValidationSpec extends BaseSpec {
    def 'cross validation [#tag]'() {
        setup:
        def (xTrain, xTest, yTrain, yTest) = multiClassificationTrainTestSplit()

        when:
        def validation = Smile.crossValidation
                .classification(xTrain, yTrain, k: 3, rounds: 10, Smile.classification::knn)

        then:
        validation.avg.accuracy > 0.90

//        where:
//        tag             | data                                 | trainer                                  | rate
//        'decision-tree' | multiClassificationTrainTestSplit()  | Smile.classification::decisionTree       | 0.90
//        'svc'           | binaryClassificationTrainTestSplit() | Smile.classification::svc                | 0.20
//        'logistic'      | multiClassificationTrainTestSplit() | Smile.classification::logisticRegression | 0.20
//        'knn'           | binaryClassificationTrainTestSplit() | Smile.classification::knn                | 0.20
    }
}
