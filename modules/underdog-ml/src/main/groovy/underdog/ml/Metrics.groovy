package underdog.ml

import underdog.ml.xvalidation.BinaryClassificationReport
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import smile.validation.metric.Accuracy
import smile.validation.metric.ConfusionMatrix
import smile.validation.metric.MAD
import smile.validation.metric.MSE
import smile.validation.metric.Precision
import smile.validation.metric.FScore
import smile.validation.metric.R2
import smile.validation.metric.Recall

/**
 * Model validation metrics. A validation metric provides a quantitative measure of agreement between a predictive
 * model and physical observations.
 *
 * @since 0.1.0
 */
class Metrics {

    double accuracy(int[] truth, int[] prediction) {
        return Accuracy.of(truth, prediction)
    }

    /**
     * The F-score (or F-measure) considers both the precision and the recall of the test to compute the score.
     * The precision p is the number of correct positive results divided by the number of all positive results,
     * and the recall r is the number of correct positive results divided by the number of positive results that
     * should have been returned.
     *
     *  The general formula involves a positive real β so that F-score measures the effectiveness of retrieval
     *  with respect to a user who attaches β times as much importance to recall as precision.
     *
     * @param beta a positive value such that F-score measures the effectiveness of retrieval with respect to
     * a user who attaches β times as much importance to recall as precision
     * @param truth the ground truth
     * @param prediction the prediction
     * @return the f-score metric value
     * @since 0.1.0
     */
    double fScore(int beta, int[] truth, int[] prediction) {
        return FScore.of(beta, truth, prediction)
    }

    /**
     * The traditional or balanced F-score (F1 score) is the harmonic mean of precision and recall,
     * where an F1 score reaches its best value at 1 and worst at 0. Beta is 1
     *
     * @param truth the ground truth
     * @param prediction the prediction
     * @return the f-score metric value
     * @since 0.1.0
     */
    double f1Score(int[] truth, int[] prediction) {
        return FScore.F1.score(truth, prediction)
    }

    /**
     * The F-score, which weighs recall higher than precision. Beta is 2
     *
     * @param truth the ground truth
     * @param prediction the prediction
     * @return the f-score metric value
     * @since 0.1.0
     */
    double f2Score(int[] truth, int[] prediction) {
        return FScore.F2.score(truth, prediction)
    }

    /**
     * The F-score, which weighs recall lower than precision. Beta is 0.5
     *
     * @param truth the ground truth
     * @param prediction the prediction
     * @return the f-score metric value
     * @since 0.1.0
     */
    double f05Score(int[] truth, int[] prediction) {
        return FScore.F2.score(truth, prediction)
    }

    /**
     * R2 coefficient of determination measures how well the regression line approximates the real data points.
     * An R2 of 1.0 indicates that the regression line perfectly fits the data.
     *
     * @param truth the ground truth
     * @param prediction the prediction
     * @return the R2-score metric value
     * @since 0.1.0
     */
    double r2Score(double[] truth, double[] prediction) {
        return R2.of(truth, prediction)
    }

    /**
     * R2 coefficient of determination measures how well the regression line approximates the real data points.
     * An R2 of 1.0 indicates that the regression line perfectly fits the data.
     *
     * @param truth the ground truth
     * @param prediction the prediction
     * @return the R2-score metric value
     * @since 0.1.0
     */
    double r2Score(int[] truth, int[] prediction) {
        return R2.of(truth as double[], prediction as double[])
    }

    /**
     * Mean absolute error
     *
     * @param truth the ground truth
     * @param prediction the prediction
     * @return the score metric value
     * @since 0.1.0
     */
    double meanAbsoluteError(double[] truth, double[] prediction) {
        return MAD.of(truth, prediction)
    }

    /**
     * Mean squared error
     *
     * @param truth the ground truth
     * @param prediction the prediction
     * @return the score metric value
     * @since 0.1.0
     */
    double meanSquaredError(double[] truth, double[] prediction) {
        return MSE.of(truth, prediction)
    }

    /**
     * A confusion matrix is a table that is used to define the performance of a classification algorithm.
     * A confusion matrix visualizes and summarizes the performance of a classification algorithm
     *
     * @param truth the ground truth
     * @param prediction the prediction
     * @return an instance of {@link ConfusionMatrix}
     * @since 0.1.0
     */
    ConfusionMatrix confusionMatrix(int[] truth, int[] prediction) {
        return ConfusionMatrix.of(truth, prediction)
    }

    /**
     * Build a map showing the main classification metrics for positive and negative classes in a
     * binary classification. Labels should be either 1 (positive) or 0 (negative)
     *
     * @param truth the ground truth
     * @param prediction the prediction
     * @param targetNames optional list of label indices to include in the report
     * @return a map containing accuracy, precision, recall, F1 and count of each class
     * @see <a href="https://scikit-learn.org/1.5/modules/generated/sklearn.metrics.classification_report.html">Scikit Learn</a>
     * @since 0.1.0
     */
    @NamedVariant
    BinaryClassificationReport binaryClassificationReport(
            int[] truth,
            int[] prediction,
            @NamedParam(required = false) List<String> targetNames = []) {
        int[] classes = truth.toList().unique()
        Map<Integer, String> indexedTargetNames = targetNames.indexed()

        if (targetNames.size() !== 0 && classes.length !== targetNames.size()) {
            throw new RuntimeException(
                "targetNames size (${targetNames.size()}) is different from classes size ${classes.length}"
            )
        }

        Map<String, Map<String, Number>> metrics = classes.inject([:]) { Map<String, Map<String, Number>> agg, int targetValue ->
            Map<Integer, Integer> currentTruthIndexed = truth
                .indexed()
                .findAll { k, v -> v === targetValue }

            Map<Integer, Integer> currentPredictionIndexed = prediction
                .indexed()
                .findAll { k, v -> k in currentTruthIndexed.keySet() }

            int[] currentTruth = currentTruthIndexed
                .values()
                .collect(applyCurrentTruth(targetValue))

            int[] currentPrediction = currentPredictionIndexed
                .values()
                .collect(applyCurrentTruth(targetValue))

            String classKey = indexedTargetNames[targetValue] ?: "class_${targetValue}"

            agg[classKey] = [
                accuracy: Accuracy.of(currentTruth, currentPrediction),
                precision: Precision.of(currentTruth, currentPrediction),
                recall: Recall.of(currentTruth, currentPrediction),
                F1: FScore.F1.score(currentTruth, currentPrediction),
                count: currentTruth.size()
            ] as Map<String, Number>

            return agg
        }

        metrics['avg_total'] = [
            accuracy: extractAverage(metrics, 'accuracy'),
            precision: extractAverage(metrics, 'precision'),
            recall: extractAverage(metrics, 'recall'),
            F1: extractAverage(metrics, 'F1'),
            count: metrics.collect { k, v -> v['count'] }.sum(),
        ] as Map<String, Number>

        return new BinaryClassificationReport(
            metrics[metrics.keySet()[-2]],
            metrics[metrics.keySet()[0]],
            metrics['avg_total'],
            targetNames)
    }

    private static BigDecimal extractAverage(Map<String, Map<String, Double>> map, String property) {
        return map.collect { k, v -> v[property] }.average() as BigDecimal
    }

    private static Closure<Integer> applyCurrentTruth(int targetValue) {
        return (int val) -> val == targetValue ? 1 : 0
    }
}
