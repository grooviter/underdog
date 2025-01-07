package underdog.ml.extensions

import smile.regression.LinearModel
import smile.regression.Regression
import underdog.ml.ML

class LinearModelExtensions {
    static double score(LinearModel model, double[][] X, double[] y) {
        return ML.metrics.r2Score(y, model.predict(X))
    }

    static double score(Regression model, double[][] X, double[] y) {
        return ML.metrics.r2Score(y, model.predict(X))
    }
}
