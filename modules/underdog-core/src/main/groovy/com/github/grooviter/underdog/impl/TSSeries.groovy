package com.github.grooviter.underdog.impl

import com.github.grooviter.underdog.Criteria
import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.Series
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam
import tech.tablesaw.api.ColumnType
import tech.tablesaw.api.DateTimeColumn
import tech.tablesaw.api.DoubleColumn
import tech.tablesaw.api.NumericColumn
import tech.tablesaw.api.StringColumn
import tech.tablesaw.columns.Column
import tech.tablesaw.selection.Selection

import java.math.MathContext
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class TSSeries implements Series {
    private final Column column

    TSSeries(Column column){
        this.column = column
    }

    @Override
    Object getImplementation() {
        return this.column
    }

    @Override
    Object getAt(Integer index) {
        return this.column.get(index)
    }

    @Override
    Series getAt(IntRange indexRange) {
        return new TSSeries(this.column.subset(indexRange as int[]))
    }

    @Override
    Series getIloc() {
        return this
    }

    @Override
    <P> Series call(Class<P> clazz, @ClosureParams(value = FirstParam.FirstGenericType, options='P') Closure func) {
        return new TSSeries(column.map(p -> func(p)))
    }

    @Override
    Series plus(Number o) {
        return new TSSeries(this.column.map(n -> n + o))
    }

    @Override
    Series plus(String st) {
        return new TSSeries(this.column.map(n -> n + st))
    }

    @Override
    Series plus(Series series) {
        Column toAdd = series.implementation as Column
        return new TSSeries(this.column.append(toAdd))
    }

    @Override
    Long size() {
        return this.column.size()
    }

    @Override
    Series unique() {
        return new TSSeries(this.column.unique())
    }

    @Override
    @NamedVariant
    Double mean(
        @NamedParam(required = false) boolean skipNa,
        @NamedParam(required = false) int precision) {
        if (column instanceof DoubleColumn) {
            NumericColumn meanCol = skipNa ? column.removeMissing() as NumericColumn : column
            return new BigDecimal(meanCol.mean(), new MathContext(precision ?: 7, RoundingMode.HALF_EVEN))
        }
        throw new RuntimeException("")
    }

    @Override
    Double mean() {
        return this.mean(skipNa:  false)
    }

    @Override
    Criteria isGreaterThan(double value) {
        NumericColumn column = this.column as NumericColumn
        return new TSCriteria(column.isGreaterThan(value))
    }

    @Override
    Criteria isLessThan(double value) {
        NumericColumn column = this.column as NumericColumn
        return new TSCriteria(column.isLessThan(value))
    }

    @Override
    Criteria isEqualTo(double number) {
        if (column instanceof NumericColumn) {
            return new TSCriteria(column.isEqualTo(number))
        }
        throw new RuntimeException("")
    }

    @Override
    Criteria isEqualTo(String value) {
        if (column instanceof StringColumn) {
            return new TSCriteria(column.isEqualTo(value))
        }
        throw new RuntimeException("")
    }

    @Override
    Series multiply(Number number) {
        NumericColumn column = this.column as NumericColumn
        return new TSSeries(column * number)
    }

    @Override
    List<Integer> toIntegerList() {
        NumericColumn column = this.column as NumericColumn
        return column.asIntColumn().toList()
    }

    @Override
    DataFrame describe() {
        return new TSDataFrame(this.column.summary())
    }

    @Override
    String toString() {
        return this.implementation.toString()
    }
}