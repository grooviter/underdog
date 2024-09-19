package com.github.grooviter.underdog.impl

import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.DataFrameIloc
import com.github.grooviter.underdog.Wildcard
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
    DataFrame getAt(Wildcard wildcard, IntRange colIndex) {
        int[] rowIndexes = calculateColumnIndex(table, colIndex)
        Table table = switch (wildcard){
            case Wildcard.ALL   -> this.table.selectColumns(rowIndexes)
            case Wildcard.FIRST -> this.table.rows(0)
            case Wildcard.LAST  -> this.table.rows(this.table.rowCount() - 1)
            default             -> this.table.selectColumns(rowIndexes)
        }
        return new TSDataFrame(table)
    }

    @Override
    DataFrame getAt(Wildcard wildcard, List<Integer> colIndex) {
        int[] rowIndexes = calculateColumnIndex(table, colIndex)
        Table table = switch (wildcard){
            case Wildcard.ALL   -> this.table.selectColumns(rowIndexes)
            case Wildcard.FIRST -> this.table.rows(0)
            case Wildcard.LAST  -> this.table.rows(this.table.rowCount() - 1)
            default             -> this.table.selectColumns(rowIndexes)
        }
        return new TSDataFrame(table)
    }

    private static int[] calculateColumnIndex(Table table, List<Integer> colIndex) {
        return table.columnNames()[colIndex].collect { table.columnIndex(it) }
    }

    @Override
    DataFrame getAt(Integer[] rowIndex, Integer[] colIndex) {
        if (!rowIndex) {
            return new TSDataFrame(this.table.selectColumns(colIndex as int[]))
        }
        return new TSDataFrame(this.table.rows(rowIndex as int[]).selectColumns(colIndex as int[]))
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
