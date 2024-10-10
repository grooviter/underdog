package com.github.grooviter.underdog.smile

import groovy.transform.NamedVariant
import smile.data.transform.InvertibleColumnTransform
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
        return Scaler.fit(Utils.createDataFrameFrom(X))
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
}
