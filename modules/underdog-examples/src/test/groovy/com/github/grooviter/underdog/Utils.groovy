package com.github.grooviter.underdog

import groovy.transform.NamedVariant
import org.codehaus.groovy.runtime.DefaultGroovyMethods

class Utils {
    @NamedVariant
    static Tuple4<double[][], double[][], int[], int[]> trainTestSplit(
            double[][] features,
            int[] labels,
            long random_state = 0,
            boolean shuffle = false,
            double train_size = 0.5) {

        def samplesLength = features.length
        def indexes = (0..<samplesLength).with {
            shuffle ? it.shuffled(new Random(random_state ?: 0)) : it
        }

        def trainSize = (samplesLength * train_size).toInteger()
        def testSize = samplesLength - trainSize
        def (double[][] xTrain, double[][] xTest) = DefaultGroovyMethods.chop(features, trainSize, testSize)
        def (int[] yTrain, int[] yTest) = DefaultGroovyMethods.chop(labels as List<Integer>, trainSize, testSize)

        return [xTrain, xTest, yTrain, yTest]
    }
}
