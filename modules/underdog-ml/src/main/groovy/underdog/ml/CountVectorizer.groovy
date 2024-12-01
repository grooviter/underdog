package underdog.ml

import groovy.transform.TupleConstructor
import smile.nlp.dictionary.EnglishPunctuations
import smile.nlp.dictionary.StopWords
import smile.nlp.normalizer.SimpleNormalizer
import smile.nlp.stemmer.Stemmer
import smile.nlp.tokenizer.SimpleSentenceSplitter
import smile.nlp.tokenizer.Tokenizer

/**
 * Convert a collection of text documents to a matrix of token counts.
 *
 * @since 0.1.0
 */
@TupleConstructor(excludes = ['keywords'])
class CountVectorizer {
    List<String> keywords
    Tokenizer tokenizer
    Stemmer stemmer
    StopWords stopWords
    int maxFeatures

    /**
     * Learn the vocabulary dictionary and return document-term matrix.
     * This is equivalent to fit followed by transform.
     *
     * @param sentences
     * @return
     * @since 0.1.0
     */
    double[][] fitTransform(String[] sentences){
        this.keywords = sentences
            .join("\n")
            .keywords(this.maxFeatures)*.words
            .flatten() as List<String>

        return this.transform(sentences)
    }

    /**
     * Transform documents to document-term matrix. Extract token counts out of raw text documents using the
     * vocabulary fitted with fit or the one provided to the constructor
     *
     * @param sentences
     * @return
     * @since 0.1.0
     */
    double[][] transform(String[] sentences) {
        return sentences
            .collect { sentence -> createBagOfWords(sentence) }
            .collect { bag ->
                this.keywords.collect {feature ->
                    bag[feature] ?: 0
                }
            } as double[][]
    }

    private Map<String, Integer> createBagOfWords(String text) {
        def normalized = SimpleNormalizer.instance.normalize(text)
        def sentences = SimpleSentenceSplitter.instance.split(normalized)
        return sentences
            .collectMany { words(it)  as List<String> }
            .collect { stemmer.stem(it).toLowerCase() }
            .groupBy { it }
            .collectEntries { k, v -> [(k): v.size()] }
            .withDefault { 0 }
    }

    private String[] words(String text) {
        return tokenizer
            .split(text)
            .findAll { word ->
                !(stopWords.contains(word.toLowerCase()) || EnglishPunctuations.instance.contains(word))
            }
    }
}
