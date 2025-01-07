package underdog.ml

import smile.data.Tuple
import smile.regression.LinearModel

class RegressionExtensions {
    static double[] predict(LinearModel linearModel, double[][] X) {
        return linearModel.predict(Utils.createDataFrameFrom(X))
    }

    static double[] predict(smile.regression.Regression<Tuple> regression, double[][] X) {
        return regression.predict(Utils.createDataFrameFrom(X))
    }
}
