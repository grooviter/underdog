package com.github.grooviter.underdog.impl

import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.DataFrameIloc
import groovy.transform.TupleConstructor
import tech.tablesaw.api.Table

@TupleConstructor
class TablesawDataFrameIloc implements DataFrameIloc {
    Table table
    @Override
    DataFrame getAt(IntRange index) {
        this.table = this.table.rows(index as int[])
        return new TablesawDataFrame(this.table)
    }

    @Override
    DataFrame getAt(Integer[] indexes) {
        this.table = this.table.rows(indexes as int[])
        return new TablesawDataFrame(this.table)
    }

    @Override
    DataFrame getAt(IntRange index, IntRange colIndex) {
        this.table = this.table.rows(index as int[]).selectColumns(colIndex as int[])
        return new TablesawDataFrame(this.table)
    }

    @Override
    DataFrame getAt(Integer index, IntRange colIndex) {
        this.table = this.table.rows(0).selectColumns(colIndex as int[])
        return new TablesawDataFrame(this.table)
    }
}
