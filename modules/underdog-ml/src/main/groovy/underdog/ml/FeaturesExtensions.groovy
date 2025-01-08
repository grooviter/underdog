package underdog.ml

import smile.data.transform.Transform

class FeaturesExtensions {
    static double[][] apply(Transform transform, double[][] X) {
        return transform.apply(Utils.createDataFrameFrom(X)).toArray()
    }
}
