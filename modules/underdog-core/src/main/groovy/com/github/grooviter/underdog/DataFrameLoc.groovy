package com.github.grooviter.underdog

import com.github.grooviter.underdog.impl.ast.ASTDriven
import groovy.transform.NamedVariant

/**
 * @since 0.1.0
 */
interface DataFrameLoc {

    /**
     * Access a single value for a row/column label pair.
     *
     * @since 0.1.0
     */
    Series getAt(String column)

    /**
     * Access a single value for a row/column label pair.
     *
     * @since 0.1.0
     */
    DataFrame getAt(String[] columns)

    /**
     * @since 0.1.0
     */
    DataFrame getAt(List<String> columns)

    /**
     * @since 0.1.0
     */
    @ASTDriven
    @NamedVariant
    default DataFrame getAt(Boolean selection, List columns) {
        throw new RuntimeException("")
    }
}