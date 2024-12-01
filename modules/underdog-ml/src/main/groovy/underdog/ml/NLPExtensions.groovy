package underdog.ml

import underdog.ml.nlp.DefaultStopWords
import underdog.ml.nlp.TFidf
import smile.nlp.Corpus
import smile.nlp.NGram
import smile.nlp.SimpleCorpus
import smile.nlp.Text
import smile.nlp.dictionary.EnglishPunctuations
import smile.nlp.dictionary.EnglishStopWords
import smile.nlp.keyword.CooccurrenceKeywords
import smile.nlp.normalizer.SimpleNormalizer
import smile.nlp.pos.HMMPOSTagger
import smile.nlp.pos.PennTreebankPOS
import smile.nlp.stemmer.PorterStemmer
import smile.nlp.stemmer.Stemmer
import smile.nlp.tokenizer.SimpleSentenceSplitter
import smile.nlp.tokenizer.SimpleTokenizer

/**
 * Extensions to use for NLP processes
 *
 * @since 0.1.0
 */
class NLPExtensions {

    /** Creates an in-memory text corpus.
     *
     * @param text a set of text.
     * @return a {@link Corpus}
     * @since 0.1.0
     */
    static Corpus corpus(List<String> documents) {
        def corpus = new SimpleCorpus()
        documents.each { corpus.add(new Text(it)) }
        return corpus
    }

    /**
     * Normalizes Unicode text.
     *
     * - Apply Unicode normalization form NFKC.
     * - Strip, trim, normalize, and compress whitespace.
     * - Remove control and formatting characters.
     * - Normalize double and single quotes.
     *
     * @param text
     * @return a normalized text
     * @since 0.1.0
     */
    static String normalize(String text){
        return SimpleNormalizer.instance.normalize(text)
    }

    /**
     * Splits English text into sentences. Given an English text, it returns a list of strings, where each element
     * is an English sentence. By default, it treats occurrences of '.', '?' and '!' as sentence delimiters, but does
     * its best to determine when an occurrence of '.' does not have this role (e.g. in abbreviations, URLs, numbers,
     * etc.).
     *
     * This tokenizer assumes that the text has already been segmented into paragraphs. Any carriage returns will be
     * replaced by whitespace.
     *
     * @param text text to get the sentences from
     * @return an array of sentences
     * @since 0.1.0
     */
    static String[] sentences(String text) {
        return SimpleSentenceSplitter.instance.split(text)
    }

    /** Tokenizes English sentences with some differences from
     * TreebankWordTokenizer, notably on handling not-contractions. If a period
     * serves as both the end of sentence and a part of abbreviation, e.g. etc. at
     * the end of sentence, it will generate tokens of "etc." and "." while
     * TreebankWordTokenizer will generate "etc" and ".".
     *
     * Most punctuation is split from adjoining words. Verb contractions and the
     * Anglo-Saxon genitive of nouns are split into their component morphemes,
     * and each morpheme is tagged separately.
     *
     * This tokenizer assumes that the text has already been segmented into
     * sentences. Any periods -- apart from those at the end of a string or before
     * newline -- are assumed to be part of the word they are attached to (e.g. for
     * abbreviations, etc), and are not separately tokenized.
     *
     * If the parameter filter is not "none", the method will also filter
     * out stop words and punctuations. There is no definite list of stop
     * words which all tools incorporate. The valid values of the parameter
     * filter include
     *   - "none": no filtering
     *   - "default": the default English stop word list
     *   - "comprehensive": a more comprehensive English stop word list
     *   - "google": the stop words list used by Google search engine
     *   - "mysql": the stop words list used by MySQL FullText feature
     *   - custom stop word list: comma separated stop word list
     *
     * @param text the text to get the words from
     * @param filterName type of filter to apply to words none|default|comprehensive|mysql|google (default is "default")
     * @return an array of {@link String} with the extracted words
     * @since 0.1.0
     */
    static String[] words(String text, String filterName = "default") {
        def tokens = new SimpleTokenizer(true).split(text) as String[]

        if (filterName == "none") {
            return tokens
        }

        def stopWords = switch(filterName) {
            case "default" -> EnglishStopWords.DEFAULT
            case "comprehensive" -> EnglishStopWords.COMPREHENSIVE
            case "google" -> EnglishStopWords.GOOGLE
            case "mysql" -> EnglishStopWords.MYSQL
            default -> new DefaultStopWords(filterName)
        }

        return tokens.findAll { word ->
            !(stopWords.contains(word.toLowerCase()) || EnglishPunctuations.instance.contains(word))
        }
    }

    /**
     * Returns the bag of words. The bag-of-words model is a simple
     * representation of text as the bag of its words, disregarding
     * grammar and word order but keeping multiplicity.
     *
     * @param text the text to get the bag of words from
     * @param filterName
     * @param stemmer stemmer to transform a word into its root form.
     * @return a {@link Map}<String,Integer> with the words and their frequency
     * @since 0.1.0
     */
    static Map<String, Integer> bag(
            String text,
            String filterName = "default",
            Stemmer stemmer = new PorterStemmer()) {
        return text
            .normalize()
            .sentences()
            .collectMany { it.words(filterName)  as List<String> }
            .collect { stemmer.stem(it).toLowerCase() }
            .groupBy { it }
            .collectEntries { k, v -> [(k): v.size()] }
            .withDefault { 0 }
    }

    /**
     * Returns an instance of type {@link CountVectorizer} which is capable of converting a collection of
     * text documents to a matrix of token counts.
     *
     * @param sentences list of sentences to be vectorized
     * @return a list of vectors
     * @since 0.1.0
     */
    static double[][] vectorized(String[] sentences) {
        return ML.features.countVectorizer().fitTransform(sentences)
    }

    /**
     * Returns the (word, part-of-speech) pairs.
     * The text should be a single sentence.
     *
     * @param text the text to extract the tags from
     * @return an array of tuples of type <{@link String}, {@link PennTreebankPOS}>
     * @since 0.1.0
     */
    static Tuple2<String, PennTreebankPOS>[] postags(String text) {
        def words = text.words("none")
        return [words, HMMPOSTagger.default.tag(words)].transpose()
    }

    /** Keyword extraction from a single document using word co-occurrence
     * statistical information.
     *
     * @param text
     * @param k the number of top keywords to return.
     * @return the top keywords.
     * @since 0.1.0
     */
    static NGram[] keywords(String text, int k = 10) {
        return CooccurrenceKeywords.of(text, k)
    }

    /**
     * Adjusts an already vectorized corpus with an TFID
     * The tf-idf weight (term frequency-inverse document frequency) is a weight often used in information retrieval
     * and text mining
     *
     * @param corpus a vectorized corpus matrix
     * @return a weighted vectorized matrix
     * @since 0.1.0
     */
    static double[][] tfidf(double[][] corpus) {
        return TFidf.tfidf(corpus)
    }
}
