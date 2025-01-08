package underdog.ml

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import smile.data.transform.Transform
import smile.feature.extraction.ProbabilisticPCA
import smile.feature.transform.MaxAbsScaler
import smile.feature.transform.Standardizer
import smile.nlp.dictionary.EnglishStopWords
import smile.nlp.dictionary.StopWords
import smile.nlp.stemmer.PorterStemmer
import smile.nlp.stemmer.Stemmer
import smile.nlp.tokenizer.SimpleTokenizer
import smile.nlp.tokenizer.Tokenizer

import static smile.math.MathEx.factorial

/**
 * Class responsible for feature extraction and normalization utils
 *
 * @since 0.1.0
 */
class Features {

    private static final Integer POLYNOMIAL_DEFAULT_DEGREE = 2

    /**
     * Standardizes numeric feature to 0 mean and unit variance. Standardization makes an assumption that the
     * data follows a Gaussian distribution and are also not robust when outliers present
     *
     * @param X the array to standardize
     * @return an instance of {@link Transform}
     * @since 0.1.0
     */
    Transform standardizeScaler(double[][] X) {
        return Standardizer.fit(Utils.createDataFrameFrom(X))
    }

    /**
     * Scales each feature by its maximum absolute value
     *
     * @param X data
     * @return an instance of {@link Transform}
     * @since 0.1.0
     */
    Transform  minMaxScaler(double[][] X) {
        return MaxAbsScaler.fit(Utils.createDataFrameFrom(X))
    }

    /**
     * Principal component analysis. PCA is an orthogonal linear transformation that transforms a number of possibly
     * correlated variables into a smaller number of uncorrelated variables called principal components.
     *
     * @param X data to reduce the dimensionality from
     * @param nComponents the number of components to reduce the dimensionality of X
     * @return an instance of {@link Transform}
     * @since 0.1.0
     */
    @NamedVariant
    Transform pca(double[][] X, @NamedParam(required = false) int nComponents = 2) {
        return ProbabilisticPCA.fit(X, nComponents)
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
     * Generates all monomials up to degree 2. This gives us the so called Vandermonde matrix with n_samples rows and
     * degree + 1 columns. Intuitively, this matrix can be interpreted as a matrix of pseudo features
     * (the points raised to some power). The matrix is akin to (but different from) the matrix induced by a
     * polynomial kernel.
     *
     * @param X source features
     * @return all monomials up to degree 2
     * @since 0.1.0
     */
    <N extends Number,L extends List<N>, LL extends List<L>> double[][] polynomialFeatures(double[][] X) {
        return this.polynomialFeatures(X.collect() as LL)
    }

    /**
     * Generates all monomials up to degree 2. This gives us the so called Vandermonde matrix with n_samples rows and
     * degree + 1 columns. Intuitively, this matrix can be interpreted as a matrix of pseudo features
     * (the points raised to some power). The matrix is akin to (but different from) the matrix induced by a
     * polynomial kernel.
     *
     * @param X source features
     * @return all monomials up to degree 2
     * @since 0.1.0
     */
    <N extends Number,L extends List<N>, LL extends List<L>> double[][] polynomialFeatures(int[][] X) {
        return this.polynomialFeatures(X.collect() as LL)
    }

    /**
     * Generates all monomials up to degree 2. This gives us the so called Vandermonde matrix with n_samples rows and
     * degree + 1 columns. Intuitively, this matrix can be interpreted as a matrix of pseudo features
     * (the points raised to some power). The matrix is akin to (but different from) the matrix induced by a
     * polynomial kernel.
     *
     * @param X source features
     * @return all monomials up to degree 2
     * @since 0.1.0
     * @link https://stackoverflow.com/questions/51906274/cannot-understand-with-sklearns-polynomialfeatures
     * @link https://stackoverflow.com/questions/64820999/what-is-the-formula-behind-scikit-learn-polynomialfeatures
     * @link https://scikit-learn.org/1.5/modules/preprocessing.html#polynomial-features
     * @link https://github.com/scikit-learn/scikit-learn/blob/6e9039160/sklearn/preprocessing/_polynomial.py
     */
    <N extends Number,L extends List<N>, LL extends List<L>> double[][] polynomialFeatures(LL X) {
        def generatedFeatures =  X.collect { row ->
            L combinations = ([row] * POLYNOMIAL_DEFAULT_DEGREE)
                .combinations()
                .sort()
                .<L>findAll { ns -> ns.head() <= ns.last() }
                .collect { ns -> ns.head() * ns.last() } as L
            return [1, *row, *combinations] as L
        }
        checkPolynomialLength(X.find().size(), POLYNOMIAL_DEFAULT_DEGREE, generatedFeatures[0].size())
        return generatedFeatures as double[][]
    }

    /**
     *
     * Checks the expected length of the polynomial. Throws a {@link RuntimeException} in case
     * expected length is not met. This implementation follows indications at
     * https://stackoverflow.com/a/51906400 with the formula:
     *
     * N(n,d) = C(n+d, d)
     *
     * Where n is the number of features, d is the degree of the polynomial, C is the
     * binomial coefficient (combination).
     *
     * @param n number of features
     * @param d degree of the polynomial
     * @param actualLength size to be compared with expected polynomial length
     */
    private static void checkPolynomialLength(int n, int d, int actualLength) {
        def C = (factorial(n + d) / (factorial(n) * factorial(2))).round()
        assert C == actualLength, "Polynomial doesn't have the expected length"
    }
}
