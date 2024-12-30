package underdog.ml

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import smile.data.transform.InvertibleColumnTransform
import smile.feature.extraction.ProbabilisticPCA
import smile.feature.transform.MaxAbsScaler
import smile.feature.transform.Scaler
import smile.feature.transform.Standardizer
import smile.nlp.dictionary.EnglishStopWords
import smile.nlp.dictionary.StopWords
import smile.nlp.stemmer.PorterStemmer
import smile.nlp.stemmer.Stemmer
import smile.nlp.tokenizer.SimpleTokenizer
import smile.nlp.tokenizer.Tokenizer

/**
 * Class responsible for feature extraction and normalization utils
 *
 * @since 0.1.0
 */
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

    /**
     * @param X
     * @return
     * @since 0.1.0
     */
    InvertibleColumnTransform minMaxScaler(double[][] X) {
        return MaxAbsScaler.fit(Utils.createDataFrameFrom(X))
    }

    /**
     * Returns an instance of type {@link CountVectorizer} which is capable of converting a collection of
     * text documents to a matrix of token counts.
     *
     * @param tokenizer
     * @param stemmer
     * @param stopWords
     * @param maxFeatures
     * @return {@link CountVectorizer}
     * @since 0.1.0
     */
    @NamedVariant
    CountVectorizer countVectorizer(
            Tokenizer tokenizer = new SimpleTokenizer(true),
            Stemmer stemmer = new PorterStemmer(),
            StopWords stopWords = EnglishStopWords.DEFAULT,
            int maxFeatures = 10
    ) {
        return new CountVectorizer(tokenizer, stemmer, stopWords, maxFeatures)
    }

    /**
     * Principal component analysis. PCA is an orthogonal linear transformation that transforms a number of possibly
     * correlated variables into a smaller number of uncorrelated variables called principal components.
     *
     * @param X data to reduce the dimensionality from
     * @param nComponents the number of components to reduce the dimensionality of X
     * @return a {@link ProbabilisticPCA}
     * @since 0.1.0
     */
    @NamedVariant
    ProbabilisticPCA pca(double[][] X, @NamedParam(required = false) int nComponents = 2) {
        return ProbabilisticPCA.fit(X, nComponents)
    }

    /**
     * Generates all monomials up to degree. This gives us the so called Vandermonde matrix with n_samples rows and
     * degree + 1 columns. Intuitively, this matrix can be interpreted as a matrix of pseudo features
     * (the points raised to some power). The matrix is akin to (but different from) the matrix induced by a
     * polynomial kernel.
     *
     * @param X
     * @param degree
     * @since 0.1.0
     */
    @NamedVariant
    double[][] polynomialFeatures(double[][] X, @NamedParam(required = false) int degree = 2) {
        def xs = X.collect().collectNested { double[] ns ->
            (1..degree).collect { Integer currentDegree ->
                ns.collect { Double n -> n**currentDegree }
            }.sum()
        }

        return xs as double[][]
    }
}
