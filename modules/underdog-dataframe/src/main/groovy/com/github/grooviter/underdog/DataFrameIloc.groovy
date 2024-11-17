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


    DataFrame getAt(Integer index, List<Integer> indexes)

    /**
     * @since 0.1.0
     */
    DataFrame getAt(IntRange index, IntRange colIndex)

    /**
     * @since 0.1.0
     */
    DataFrame getAt(Wildcard wildcard, IntRange colIndex)

    Series getAt(Wildcard wildcard, Integer col)

    /**
     * @since 0.1.0
     */
    DataFrame getAt(Wildcard wildcard, List<Integer> colIndex)

    /**
     * @since 0.1.0
     */
    DataFrame getAt(Integer[] rowIndex, Integer[] colIndex)

    /**
     * @since 0.1.0
     */
    DataFrame getAt(Integer index, IntRange colIndex)
}