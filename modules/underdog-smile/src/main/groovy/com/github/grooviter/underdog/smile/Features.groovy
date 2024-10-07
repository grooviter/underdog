package com.github.grooviter.underdog.smile

import smile.data.transform.InvertibleColumnTransform
import smile.feature.transform.Scaler
import smile.feature.transform.Standardizer

class Features {

    /**
     * Standardizes numeric feature to 0 mean and unit variance. Standardization makes an assumption that the
     * data follows a Gaussian distribution and are also not robust when outliers present
     *
     * @param X the array to standardize
     * @return an instance of {@link InvertibleColumnTransform}
     * @since 0.1.0
     */
    InvertibleColumnTransform standardizeScaler(double[][] X) {
        return Standardizer.fit(Utils.createDataFrameFrom(X))
    }

    InvertibleColumnTransform minMaxScaler(double[][] X) {
        return Scaler.fit(Utils.createDataFrameFrom(X))
    }
}
