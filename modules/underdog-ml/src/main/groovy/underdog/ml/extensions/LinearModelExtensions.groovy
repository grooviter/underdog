package underdog.ml.extensions

import smile.regression.LinearModel
import underdog.ml.ML

class LinearModelExtensions {
    static double score(LinearModel model, double[][] X, double[] y) {
        return ML.metrics.r2Score(y, model.predict(X))
    }
}
