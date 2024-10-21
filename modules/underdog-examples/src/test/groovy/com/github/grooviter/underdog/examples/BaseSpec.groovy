package com.github.grooviter.underdog.examples

import com.github.grooviter.underdog.Underdog
import com.github.grooviter.underdog.smile.Smile
import spock.lang.Specification

class BaseSpec extends Specification {
    Tuple4<double[][], double[][], int[], int[]> binaryClassificationTrainTestSplit() {
        def df = Underdog
            .read_csv("src/test/resources/com/github/grooviter/underdog/examples/food.csv", sep: ";")
            .dropna()

        df['y'] = df['TRAFFICLIGHT VALUE'](Integer, Integer){it == 3 ? 1 : -1 }

        def X = df[['CARBS', 'SUGAR', 'PROTEINS', 'FAT', 'SALT', 'FIBER']] as double[][]
        def y = df['y'] as int[]

        return Smile.utils.trainTestSplit(X, y)
    }

    Tuple4<double[][], double[][], int[], int[]> multiClassificationTrainTestSplit() {
        def df = Underdog
                .read_csv("src/test/resources/com/github/grooviter/underdog/examples/food.csv", sep: ";")
                .dropna()

        df['y'] = df['TRAFFICLIGHT VALUE']

        def X = df[['CARBS', 'SUGAR', 'PROTEINS', 'FAT', 'SALT', 'FIBER']] as double[][]
        def y = df['y'] as int[]

        return Smile.utils.trainTestSplit(X, y)
    }
}
