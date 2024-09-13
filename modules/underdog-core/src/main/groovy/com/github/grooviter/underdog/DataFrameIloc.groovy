package com.github.grooviter.underdog

interface DataFrameIloc {
    DataFrame getAt(IntRange index)

    /**
     * Access a single value for a row/column label pair.
     * */
    DataFrame getAt(Integer[] indexes)

    DataFrame getAt(IntRange index, IntRange colIndex)

    DataFrame getAt(Integer index, IntRange colIndex)
}