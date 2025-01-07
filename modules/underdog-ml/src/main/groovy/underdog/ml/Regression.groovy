package underdog.ml

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import smile.data.formula.Formula
import smile.math.kernel.MercerKernel
import smile.regression.LASSO
import smile.regression.LinearModel
import smile.regression.OLS
import smile.regression.RandomForest
import smile.regression.RidgeRegression
import smile.regression.SVM

/**
 * Regression analysis. In statistics, regression analysis includes any techniques for modeling and analyzing several
 * variables, when the focus is on the relationship between a dependent variable and one or more independent variables.
 * Most commonly, regression analysis estimates the conditional expectation of the dependent variable given the
 * independent variables. Therefore, the estimation target is a function of the independent variables called the
 * regression function. Regression analysis is widely used for prediction and forecasting, where its use has
 * substantial overlap with the field of machine learning.
 *
 * @since 0.1.0
 */
class Regression {
    private static final Formula FORMULA_Y = Formula.lhs("y")

    /**
     * Ridge Regression. Coefficient estimates for multiple linear regression models rely on the independence
     * of the model terms. When terms are correlated and the columns of the design matrix X have an approximate
     * linear dependence, the matrix X'X becomes close to singular. As a result, the least-squares estimate
     * becomes highly sensitive to random errors in the observed response Y, producing a large variance.
     *
     * @param X the features
     * @param y the labels
     * @param alpha the shrinkage/regularization parameter. Large lambda means more shrinkage. Choosing an appropriate value of lambda is important, and also difficult. Its length may be 1 so that its value is applied to all variables
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
     * Lasso (least absolute shrinkage and selection operator) regression. The Lasso is a shrinkage and selection
     * method for linear regression. It minimizes the usual sum of squared errors, with a bound on the sum of the
     * absolute values of the coefficients (i.e. L1-regularized). It has connections to soft-thresholding of wavelet
     * coefficients, forward stage-wise regression, and boosting methods.
     *
     * @param X the features
     * @param y the labels
     * @param alpha the shrinkage/regularization parameter
     * @param toleration the tolerance to stop iterations (relative target duality gap)
     * @param maxIterations the maximum number of IPM (Newton) iterations
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
     * Ordinary least squares. In linear regression, the model specification is that the dependent variable is a
     * linear combination of the parameters (but need not be linear in the independent variables).
     * The residual is the difference between the value of the dependent variable predicted by the model,
     * and the true value of the dependent variable. Ordinary least squares obtains parameter estimates that minimize
     * the sum of squared residuals, SSE (also denoted RSS).
     *
     * @param X the features
     * @param y the labels
     * @param method the fitting method ("svd" or "qr") (default "qr")
     * @param stderr if true, compute the standard errors of the estimate of parameters (default true)
     * @param recursive if true, the return model supports recursive least squares (default true)
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

    /**
     * Epsilon support vector regression. Like SVMs for classification, the model produced by SVR depends only on a
     * subset of the training data, because the cost function ignores any training data close to the model prediction
     * (within a threshold ε).
     *
     * @param X the features
     * @param y the labels
     * @param kernel the kernel function (default MercelKernel.of('linear'))
     * @param eps the parameter of epsilon-insensitive hinge loss. There is no penalty associated with samples which
     * are predicted within distance epsilon from the actual value. Decreasing epsilon forces closer fitting to the
     * calibration/training data (default 1.0)
     * @param C the soft margin penalty parameter (default 1.0)
     * @param tolerance the tolerance of convergence test (default 1E-3)
     * @return the model in an instance of {@link smile.regression.Regression}
     * @since 0.1.0
     */
    @NamedVariant
    smile.regression.Regression svm(
            double[][] X,
            double[] y,
            @NamedParam(required = false) MercerKernel<double[]> kernel = MercerKernel.of("linear"),
            @NamedParam(required = false) double eps = 1.0,
            @NamedParam(required = false) double C = 1.0,
            @NamedParam(required = false) double tolerance = 1E-3
    ){
        return (smile.regression.Regression) SVM.<double[]>fit(X, y, kernel, eps, C, tolerance)
    }

    /**
     * Random forest for regression. Random forest is an ensemble method that consists of many regression trees and
     * outputs the average of individual trees. The method combines bagging idea and the random selection of features.
     *
     *
     * @param nTrees - the number of trees.
     * @param mtry - the number of input variables to be used to determine the decision at a node of the tree. p/3 generally give good performance, where p is the number of variables.
     * @param maxDepth - the maximum depth of the tree.
     * @param maxNodes - the maximum number of leaf nodes in the tree.
     * @param nodeSize - the number of instances in a node below which the tree will not split, nodeSize = 5 generally gives good results.
     * @param subSample - the sampling rate for training tree. 1.0 means sampling with replacement. < 1.0 means sampling without replacement.
     * @return the model in an instance of {@link RandomForest}
     * @since 0.1.0
     */
    @NamedVariant
    RandomForest randomForest(
        double[][] X,
        double[] y,
        @NamedParam(required = false) int nTrees = 500,
        @NamedParam(required = false) int mtry = 0,
        @NamedParam(required = false) int maxDepth = 20,
        @NamedParam(required = false) int maxNodes = (X.length / 5).intValue(),
        @NamedParam(required = false) int nodeSize = 5,
        @NamedParam(required = false) double subSample = 1.0
    ) {
        return RandomForest.fit(
            FORMULA_Y,
            Utils.createDataFrameFrom(X, y),
            nTrees,
            mtry,
            maxDepth,
            maxNodes,
            nodeSize,
            subSample)
    }
}
