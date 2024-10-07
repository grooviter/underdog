package com.github.grooviter.underdog.smile

import smile.classification.Classifier
import smile.classification.DecisionTree
import smile.classification.GradientTreeBoost
import smile.classification.RandomForest
import smile.data.DataFrame
import smile.data.Tuple
import smile.data.type.StructType

/**
 * @since 0.1.0
 */
class ClassificationExtensions {

    /**
     * @param classifier
     * @param X
     * @return int[]
     * @since 0.1.0
     */
    static int[] predict(DecisionTree classifier, double[][] X) {
        return predictTuple(classifier, X, null)
    }

    /**
     * @param classifier
     * @param X
     * @return int[]
     * @since 0.1.0
     */
    static int[] predict(RandomForest classifier, double[][] X) {
        return predictTuple(classifier, X, null)
    }

    /**
     * @param classifier
     * @param X
     * @param posteriori
     * @return int[]
     * @since 0.1.0
     */
    static int[] predict(DecisionTree classifier, double[][] X, double[][] posteriori) {
        return predictTuple(classifier, X, posteriori)
    }

    /**
     * @param classifier
     * @param X
     * @param posteriori
     * @return int[]
     * @since 0.1.0
     */
    static int[] predict(RandomForest classifier, double[][] X, double[][] posteriori) {
        return predictTuple(classifier, X, posteriori)
    }

    static int[] predict(GradientTreeBoost classifier, double[][] X, double[][] posteriori) {
        return predictTuple(classifier, X, posteriori)
    }

    static int[] predict(GradientTreeBoost classifier, double[][] X) {
        return predictTuple(classifier, X, null)
    }

    static <M extends Classifier<Tuple>> int[] predictTuple(M classifier, double[][] X, double[][] posteriori) {
        if (X?.length <= 0) {
            return [] as int[]
        }

        String[] names = (0..X[0].length).collect { "X${it}"} as String[]
        StructType structType = DataFrame.of(X, names).schema()

        return X.collect {classifier.predict(Tuple.of(it, structType)) }
    }
}
