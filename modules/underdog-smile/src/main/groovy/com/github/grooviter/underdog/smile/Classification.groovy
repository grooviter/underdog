package com.github.grooviter.underdog.smile

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import smile.base.cart.SplitRule
import smile.classification.Classifier
import smile.classification.DecisionTree
import smile.classification.KNN
import smile.classification.LDA
import smile.classification.LogisticRegression
import smile.classification.RandomForest
import smile.classification.SVM
import smile.data.formula.Formula

import static java.lang.Math.floor
import static java.lang.Math.sqrt

/**
 * This class includes access to all available classifiers
 *
 * @since 0.1.0
 */
class Classification {
    private static final Formula FORMULA_Y = Formula.lhs("y")

    /**
     * K-nearest neighbor classifier. The k-nearest neighbor algorithm (k-NN) is a method for classifying objects
     * by a majority vote of its neighbors, with the object being assigned to the class most common amongst its k
     * nearest neighbors (k is a positive integer, typically small). k-NN is a type of instance-based learning,
     * or lazy learning where the function is only approximated locally and all computation is deferred until
     * classification.
     *
     * @param X features matrix
     * @param y labels matrix
     * @param k k-nearest neighbors
     * @return {@link KNN} the model to use for classification
     * @see smile.classification.KNN
     * @see <a href="https://scikit-learn.org/stable/modules/generated/sklearn.neighbors.KNeighborsClassifier.html#sklearn.neighbors.KNeighborsClassifier">Scikit Learn</a>
     * @see <a href="https://en.wikipedia.org/wiki/K-nearest_neighbors_algorithm">Wikipedia</a>
     * @since 0.1.0
     */
    @NamedVariant
    Classifier<double[]> knn(double[][] X, int[] y, @NamedParam(required = false) int k = 1) {
        return KNN.fit(X, y, k)
    }

    /**
     * Logistic regression (logit model) is a generalized linear model used for binomial regression.
     * Logistic regression applies maximum likelihood estimation after transforming the dependent into a logit variable.
     * A logit is the natural log of the odds of the dependent equaling a certain value or not
     * (usually 1 in binary logistic models, the highest value in multinomial models).
     * In this way, logistic regression estimates the odds of a certain event (value) occurring.
     *
     * @param X features matrix
     * @param y labels matrix
     * @param C controls de amount of regularization. A value > 0 gives a "regularized" estimate of linear weights
     * which often has superior generalization performance, especially when the dimensionality is high.
     * @param toleration the tolerance to stop iterations
     * @param maxIterations the maximum number of iterations
     * @return {@link LogisticRegression} the model to use for classification
     * @see LogisticRegression
     * @see <a href="https://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LogisticRegression.html">Scikit Learn</a>
     * @see <a href="https://en.wikipedia.org/wiki/Logistic_regression">Wikipedia</a>
     * @since 0.1.0
     */
    @NamedVariant
    Classifier<double[]> logisticRegression(
        double[][] X,
        int[] y,
        @NamedParam(required = false) double C = 1.0,
        @NamedParam(required = false) double toleration = 1e-4,
        @NamedParam(required = false) int maxIterations = 100) {
        return LogisticRegression.fit(X, y, C, toleration, maxIterations)
    }

    /**
     * Support vector machines for classification. The basic support vector machine is a binary linear classifier
     * which chooses the hyperplane that represents the largest separation, or margin, between the two classes.
     * If such a hyperplane exists, it is known as the maximum-margin hyperplane and the linear classifier it defines
     * is known as a maximum margin classifier.
     *
     * @param X features matrix
     * @param y labels matrix
     * @param C the soft margin penalty parameter.
     * @param toleration the tolerance of convergence test
     * @return {@link Classifier} the model to use for classification
     * @see SVM
     * @see <a href="https://scikit-learn.org/stable/modules/generated/sklearn.svm.SVC.html">Scikit Learn</a>
     * @see <a href="https://en.wikipedia.org/wiki/Support_vector_machine">Wikipedia</a>
     * @since 0.1.0
     */
    @NamedVariant
    Classifier<double[]> svc(
        double[][] X,
        int[] y,
        @NamedParam(required = false) double C = 1.0,
        @NamedParam(required = false) double toleration = 1e-4) {
        return SVM.fit(X, y, C, toleration)
    }

    /**
     * Decision tree. A classification/regression tree can be learned by splitting the training set into subsets
     * based on an attribute value test. This process is repeated on each derived subset in a recursive manner
     * called recursive partitioning. The recursion is completed when the subset at a node all has the same
     * value of the target variable, or when splitting no longer adds value to the predictions.
     *
     * @param X features matrix
     * @param y labels matrix
     * @param splitRule the splitting rule
     * @param maxDepth the maximum depth of the tree
     * @param maxNodes the maximum number of leaf nodes in the tree
     * @param nodeSize the minimum size of leaf nodes
     * @return {@link DecisionTree}
     * @see DecisionTree
     * @see <a href="https://scikit-learn.org/stable/modules/tree.html">Scikit Learn</a>
     * @see <a href="https://en.wikipedia.org/wiki/Decision_tree">Wikipedia</a>
     * @since 0.1.0
     */
    @NamedVariant
    DecisionTree decisionTree(
        double[][] X,
        int[] y,
        @NamedParam(required = false) String splitRule = 'GINI',
        @NamedParam(required = false) int maxDepth = 20,
        @NamedParam(required = false) int maxNodes = (X.length / 5).intValue(),
        @NamedParam(required = false) int nodeSize = 5) {
        return DecisionTree.fit(FORMULA_Y, Utils.createDataFrameFrom(X, y), SplitRule.valueOf(splitRule), maxDepth, maxNodes, nodeSize)
    }

    /**
     * Random forest is an ensemble classifier that consists of many decision trees and outputs the majority vote
     * of individual trees
     *
     * @param X features matrix
     * @param y labels matrix
     * @param nTrees the number of trees. (default: 10)
     * @Param mtry  the number of input variables to be used to determine the decision at a node of the tree.
     * floor(sqrt(p)) generally gives good performance, where p is the number of variables
     * @param splitRule the splitting rule. (default: 'GINI')
     * @param maxDepth the maximum depth of the tree (default: 20)
     * @param maxNodes the maximum number of leaf nodes in the tree (default: (X.length / 5).intValue())
     * @param nodeSize the minimum size of leaf nodes (default: 5)
     * @since 0.1.0
     */
    @NamedVariant
    RandomForest randomForest(
            double[][] X,
            int[] y,
            @NamedParam(required = false) int nTrees = 10,
            @NamedParam(required = false) int mTry = X?.length > 0 ? floor(sqrt(X[0].length)).toInteger() : 1,
            @NamedParam(required = false) String splitRule = 'GINI',
            @NamedParam(required = false) int maxDepth = 20,
            @NamedParam(required = false) int maxNodes = X?.length > 0 ? (X.length / 5).intValue() : 1,
            @NamedParam(required = false) int nodeSize = 5,
            @NamedParam(required = false) double subSample = 1.0) {
        return RandomForest.fit(
                FORMULA_Y,
                Utils.createDataFrameFrom(X, y),
                nTrees,
                mTry,
                SplitRule.valueOf(splitRule),
                maxDepth,
                maxNodes,
                nodeSize,
                subSample)
    }

    /**
     * Linear discriminant analysis. LDA is based on the Bayes decision theory and assumes that the conditional
     * probability density functions are normally distributed.
     * LDA also makes the simplifying homoscedastic assumption (i.e. that the class covariances are identical)
     * and that the covariances have full rank. With these assumptions, the discriminant function of an input being
     * in a class is purely a function of this linear combination of independent variables.
     *
     * @param X features matrix
     * @param y labels matrix
     * @param tol  a tolerance to decide if a covariance matrix is singular; it will reject variables
     * whose variance is less than tol2 (default: 1.0E-4)
     * @see LDA
     * @see <a href="https://scikit-learn.org/stable/modules/generated/sklearn.discriminant_analysis.LinearDiscriminantAnalysis.html>Scikit Learn</a>
     * @see <a href="https://en.wikipedia.org/wiki/Linear_discriminant_analysis">Wikipedia</a>
     * @since 0.1.0
     */
    @NamedVariant
    Classifier<double[]> lda(
        double[][] X,
        int[] y,
        @NamedParam(required = false) double[] priori,
        @NamedParam(required = false) double tot = 1.0E-4) {
        return LDA.fit(X, y, priori ?: null, tot)
    }
}
