package com.github.grooviter.underdog

import com.github.grooviter.underdog.smile.Smile
import spock.lang.Specification

class SurvivalSpec extends Specification {
    static String SURVIVAL_CSV = "src/test/resources/com/github/grooviter/underdog/tablesaw/haberman.data"

    DataFrame loadDataFrame() {
        return Underdog
            .read_csv(path: SURVIVAL_CSV, allowedDuplicatedNames: true)
            .rename(columns: ['age', 'year', 'aux_nodes', 'status'])
            .dropna()
    }

    def "check dataframe"() {
        setup:
        def df = loadDataFrame()

        and:
        def X = df[df.columns - 'status'] as double[][]
        def y = df['status'](Integer, Integer) { it == 1 ? 1 : -1 } as int[]

        and:
        def (
            xTrain,
            xTest,
            yTrain,
            yTest
        ) = Smile.utils.trainTestSplit(X, y)

        when:
        def classifier = Smile.classification.svc(xTrain, yTrain)
        def prediction = classifier.predict(xTest)

        then:
        Smile.metrics.accuracy(yTest, prediction) > 0.72
    }
}
