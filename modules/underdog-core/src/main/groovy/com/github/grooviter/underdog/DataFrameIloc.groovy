package com.github.grooviter.underdog

/**
 * @since 0.1.0
 */
interface DataFrameIloc {
    /**
     * @since 0.1.0
     */
    DataFrame getAt(IntRange index)

    /**
     * Access a single value for a row/column label pair.
     *
     * @since 0.1.0
     */
    DataFrame getAt(Integer[] indexes)

    /**
     * @since 0.1.0
     */
    DataFrame getAt(IntRange index, IntRange colIndex)

    /**
     * @since 0.1.0
     */
    DataFrame getAt(Integer index, IntRange colIndex)
}