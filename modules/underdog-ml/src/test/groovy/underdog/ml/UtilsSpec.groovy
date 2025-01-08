package underdog.ml

import spock.lang.Specification
import underdog.Underdog

class UtilsSpec extends Specification {
    def "train test split: making sure order is respected"() {
        setup:
        def data = Underdog.df().read_csv("src/test/resources/data/food.csv", sep: ';')
        def X = data.loc[__, ['CARBS', 'FAT']] as double[][]
        def y = data['TRAFFICLIGHT VALUE'] as double[]

        def (
            xTrain,
            xTest,
            yTrain,
            yTest
        ) = Underdog.ml().utils.trainTestSplit(X, y, shuffle: false)

        expect:
        X == (xTrain.collect() + xTest.collect()) as double[][]

        and:
        y == (yTrain.collect() + yTest.collect()) as double[]
    }
}
