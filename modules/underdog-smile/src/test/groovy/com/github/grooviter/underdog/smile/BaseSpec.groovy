package com.github.grooviter.underdog.smile

import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.Underdog
import spock.lang.Specification

class BaseSpec extends Specification {
    DataFrame loadFoodDataFrame() {
        return Underdog
                .read_csv("src/test/resources/data/food.csv", sep: ";")
                .dropna()
    }

    Tuple4<double[][], double[][], int[], int[]> binaryClassificationTrainTestSplit(List<Integer> classes = [1, -1]) {
        def (on, off) = classes
        def df = Underdog
            .read_csv("src/test/resources/data/food.csv", sep: ";")
            .dropna()

        df['y'] = df['TRAFFICLIGHT VALUE'](Integer, Integer){it == 3 ? on : off }

        def X = df[['CARBS', 'SUGAR', 'PROTEINS', 'FAT', 'SALT', 'FIBER']] as double[][]
        def y = df['y'] as int[]

        return Smile.utils.trainTestSplit(X, y)
    }

    Tuple4<double[][], double[][], int[], int[]> multiClassificationTrainTestSplit() {
        def df = Underdog
                .read_csv("src/test/resources/data/food.csv", sep: ";")
                .dropna()

        df['y'] = df['TRAFFICLIGHT VALUE']

        def X = df[['CARBS', 'SUGAR', 'PROTEINS', 'FAT', 'SALT', 'FIBER']] as double[][]
        def y = df['y'] as int[]

        return Smile.utils.trainTestSplit(X, y)
    }
}
