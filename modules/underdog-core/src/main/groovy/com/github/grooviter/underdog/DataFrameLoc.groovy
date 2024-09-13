package com.github.grooviter.underdog

import com.github.grooviter.underdog.impl.ast.ASTDriven
import groovy.transform.NamedVariant

interface DataFrameLoc {

    /**
     * Access a single value for a row/column label pair.
     * */
    Series getAt(String column)

    /**
     * Access a single value for a row/column label pair.
     * */
    DataFrame getAt(String[] columns)

    @ASTDriven
    @NamedVariant
    default DataFrame getAt(Boolean selection, List columns) {
        throw new RuntimeException("")
    }
}