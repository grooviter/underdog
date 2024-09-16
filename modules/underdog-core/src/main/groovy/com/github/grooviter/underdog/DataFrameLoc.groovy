package com.github.grooviter.underdog

import com.github.grooviter.underdog.impl.ast.ASTDriven

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
    default DataFrame getAt(Boolean selection) {
        throw new RuntimeException("")
    }

    /**
     * @since 0.1.0
     */
    default DataFrame getAt(Criteria criteria) {
        return criteria.apply(this)
    }

    /**
     * @since 0.1.0
     */
    @ASTDriven
    default DataFrame getAt(Boolean selection, List columns) {
        throw new RuntimeException("")
    }
}