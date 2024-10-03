package com.github.grooviter.underdog.smile

import smile.regression.LinearModel

class RegressionExtensions {
    static double[] predict(LinearModel linearModel, double[][] X) {
        return X.collect { linearModel.predict(it) } as double[]
    }
}
