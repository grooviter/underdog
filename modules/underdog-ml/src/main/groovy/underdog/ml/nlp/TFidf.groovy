package underdog.ml.nlp

import smile.math.MathEx

class TFidf {
    static double[][] tfidf(double[][] corpus) {
        def n = corpus.length
        def df = new int[corpus[0].length]

        corpus.each { bag ->
            df.indices.each { i ->
                if (bag[i] > 0) {
                    df[i] = (df[i] + 1)
                }
            }
        }

        return corpus.collect { bag -> _tfidf(bag, n, df) }
    }

    private static double[] _tfidf(double[] bag, int n, int[] df) {
        def maxtf = bag.max()
        def features = new double[bag.length]

        for (int i : features.indices) {
            features[i] = _tfidf(bag[i], maxtf, n, df[i])
        }

        MathEx.unitize(features)

        return features
    }

    private static double _tfidf(double tf, double maxtf, int n, int df) {
        return (tf / maxtf) * Math.log((1.0 + n) / (1.0 + df))
    }
}
