package underdog.ml

import smile.data.transform.InvertibleColumnTransform

class FeaturesExtensions {

    static double[][] apply(InvertibleColumnTransform transform, double[][] X){
        return transform.apply(Utils.createDataFrameFrom(X)).toArray()
    }
}
