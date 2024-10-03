package com.github.grooviter.underdog.smile

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import smile.data.formula.Formula
import smile.regression.LASSO
import smile.regression.LinearModel
import smile.regression.OLS
import smile.regression.RidgeRegression

/**
 * @since 0.1.0
 */
class Regression {
    private static final Formula FORMULA_Y = Formula.lhs("y")

    /**
     * @param X
     * @param y
     * @param alpha
     * @return {@link LinearModel}
     * @since 0.1.0
     */
    @NamedVariant
    LinearModel ridge(
        double[][] X,
        double[] y,
        @NamedParam(required = false) double alpha = 1.0) {
        return RidgeRegression.fit(FORMULA_Y, Utils.createDataFrameFrom(X, y), alpha)
    }

    /**
     * @param X
     * @param y
     * @param alpha
     * @param toleration
     * @param maxIterations
     * @return {@link LinearModel}
     * @since 0.1.0
     */
    @NamedVariant
    LinearModel lasso(
            double[][] X,
            double[] y,
            @NamedParam(required = false) double alpha = 1.0,
            @NamedParam(required = false) double toleration = 1e-4,
            @NamedParam(required = false) int maxIterations = 1_000) {
        return LASSO.fit(FORMULA_Y, Utils.createDataFrameFrom(X, y), alpha, toleration, maxIterations)
    }

    /**
     * @param X
     * @param y
     * @param method
     * @param stderr
     * @param recursive
     * @return {@link LinearModel}
     * @since 0.1.0
     */
    @NamedVariant
    LinearModel ols(
            double[][] X,
            double[] y,
            @NamedParam(required = false) String method = 'qr',
            @NamedParam(required = false) boolean stderr = true,
            @NamedParam(required = false) boolean recursive = true) {
        return OLS.fit(FORMULA_Y, Utils.createDataFrameFrom(X, y), method, stderr, recursive)
    }
}
