package underdog.ml

import smile.validation.metric.ConfusionMatrix

class MetricsExtensions {
    static List<Integer> shape(ConfusionMatrix confusionMatrix) {
        return confusionMatrix.matrix.with {[it.length, it[0].length] }
    }
}
