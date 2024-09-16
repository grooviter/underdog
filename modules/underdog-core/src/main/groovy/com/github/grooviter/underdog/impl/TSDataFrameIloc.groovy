package com.github.grooviter.underdog.impl

import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.DataFrameIloc
import groovy.transform.TupleConstructor
import tech.tablesaw.api.Table

@TupleConstructor
class TSDataFrameIloc implements DataFrameIloc {
    Table table
    @Override
    DataFrame getAt(IntRange index) {
        this.table = this.table.rows(index as int[])
        return new TSDataFrame(this.table)
    }

    @Override
    DataFrame getAt(Integer[] indexes) {
        this.table = this.table.rows(indexes as int[])
        return new TSDataFrame(this.table)
    }

    @Override
    DataFrame getAt(IntRange index, IntRange colIndex) {
        this.table = this.table.selectColumns(colIndex as int[]).rows(index as int[])
        return new TSDataFrame(this.table)
    }

    @Override
    DataFrame getAt(Integer index, IntRange colIndex) {
        if (!(index.abs() in (0..this.table.rowCount()))) {
            throw new RuntimeException("index $index is out of bounds")
        }

        int indexToUse = index
        if (index < 0) {
            indexToUse = this.table.rowCount() + index
        }
        this.table = this.table.selectColumns(colIndex as int[]).rows(indexToUse)
        return new TSDataFrame(this.table)
    }
}
