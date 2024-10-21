package com.github.grooviter.underdog.examples

import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.Underdog
import com.github.grooviter.underdog.smile.Smile
import spock.lang.Specification

class SurvivalSpec extends Specification {
    static String SURVIVAL_CSV = "src/test/resources/com/github/grooviter/underdog/examples/haberman.data"

    DataFrame loadDataFrame() {
        return Underdog
            .read_csv(SURVIVAL_CSV, allowedDuplicatedNames: true)
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
        ) = Smile.utils.trainTestSplit(X, y, shuffle: true, random_state: 10)

        when:
        def classifier = Smile.classification.svc(xTrain, yTrain)
        def prediction = classifier.predict(xTest)

        then:
        Smile.metrics.accuracy(yTest, prediction) > 0.50
    }
}
