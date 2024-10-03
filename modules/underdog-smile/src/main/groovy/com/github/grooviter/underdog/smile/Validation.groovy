package com.github.grooviter.underdog.smile

import smile.validation.metric.ConfusionMatrix
import smile.validation.metric.R2

class Validation {
    double scoreR2(double[] truth, double[] prediction) {
        return new R2().score(truth, prediction)
    }

    double scoreR2(int[] truth, int[] prediction) {
        return new R2().score(truth as double[], prediction as double[])
    }

    ConfusionMatrix confusionMatrix(int[] truth, int[] prediction) {
        return ConfusionMatrix.of(truth, prediction)
    }
}
