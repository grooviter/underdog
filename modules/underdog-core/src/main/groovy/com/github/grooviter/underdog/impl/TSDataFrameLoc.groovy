package com.github.grooviter.underdog.impl

import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.DataFrameLoc
import com.github.grooviter.underdog.Series
import com.github.grooviter.underdog.impl.ast.ASTDriven
import groovy.transform.NamedVariant
import groovy.transform.TupleConstructor
import tech.tablesaw.api.Table

@TupleConstructor
class TSDataFrameLoc implements DataFrameLoc {
    Table table
    @Override
    DataFrame getAt(List<String> columns) {
        this.table = this.table.selectColumns(columns as String[])
        return new TSDataFrame(this.table)
    }

    @ASTDriven
    @NamedVariant
    DataFrame getAt(Boolean selection) {
        throw new RuntimeException("")
    }

    @Override
    Series getAt(String columnName) {
        return new TSSeries(this.table.column(columnName))
    }
}
