package com.github.grooviter.underdog.smile.nlp

import smile.nlp.dictionary.StopWords

class DefaultStopWords implements StopWords {
    private final List<String> dict

    DefaultStopWords(String filter) {
        dict = filter.split(",").toUnique()
    }

    @Override
    boolean contains(String s) {
        dict.contains(s)
    }

    @Override
    int size() {
        dict.size()
    }

    @Override
    Iterator<String> iterator() {
        dict.iterator()
    }
}
