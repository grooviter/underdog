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
        return new TSDataFrame(this.table.rows(index as int[]))
    }

    @Override
    DataFrame getAt(Integer[] indexes) {
        return new TSDataFrame(this.table.rows(indexes as int[]))
    }

    @Override
    DataFrame getAt(Integer index, List<Integer> indexes) {
        assert isInRowBounds(index), rowIndexIsOutOfBoundMessage(index?.toString())
        assert isInColumnBounds(indexes), colIndexIsOutOfBoundsMessage(indexes?.toString())

        int[] rowIndexes = resolveRowIndexes(index)
        int[] colIndexes = resolveColumnIndexes(indexes)

        return new TSDataFrame(this.table.selectColumns(colIndexes).rows(rowIndexes))
    }

    @Override
    DataFrame getAt(IntRange index, IntRange colIndex) {
        return new TSDataFrame(this.table.selectColumns(colIndex as int[]).rows(index as int[]))
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
        assert isInRowBounds(index), rowIndexIsOutOfBoundMessage(index?.toString())
        assert isInColumnBounds(colIndex), colIndexIsOutOfBoundsMessage(colIndex?.toString())

        int[] colIndexes = resolveColumnIndexes(colIndex)
        int[] rowIndexes = resolveRowIndexes(index)

        return new TSDataFrame(this.table.selectColumns(colIndexes).rows(rowIndexes))
    }

    private static String rowIndexIsOutOfBoundMessage(String index) {
        return "row index $index is out of bounds"
    }

    private static String colIndexIsOutOfBoundsMessage(String index) {
        return "column index $index is out of bounds"
    }

    private int[] resolveRowIndexes(Integer rowIndex) {
        return [rowIndex >= 0 ? rowIndex : this.table.rowCount() + rowIndex] as int[]
    }

    private int[] resolveColumnIndexes(List colIndex) {
        return this.table.columnNames()[colIndex].collect { table.columnIndex(it) } as int[]
    }

    private boolean isInColumnBounds(List indexes) {
        return this.table.columns().size() >= indexes.size()
    }

    private boolean isInRowBounds(Integer index) {
        return index.abs() in (0..this.table.rowCount())
    }
}
