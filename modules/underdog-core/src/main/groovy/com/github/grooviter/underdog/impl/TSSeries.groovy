package com.github.grooviter.underdog.impl

import com.github.grooviter.underdog.Criteria
import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.Series
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.NullCheck
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam
import org.codehaus.groovy.runtime.DefaultGroovyMethods
import tech.tablesaw.api.ColumnType
import tech.tablesaw.api.DoubleColumn
import tech.tablesaw.api.NumericColumn
import tech.tablesaw.api.StringColumn
import tech.tablesaw.columns.Column
import tech.tablesaw.columns.numbers.DoubleColumnType
import tech.tablesaw.columns.strings.StringColumnType

import java.math.MathContext
import java.math.RoundingMode

class TSSeries implements Series {
    private final Column column

    @NullCheck
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
    @NullCheck
    Series plus(Series series) {
        return new TSSeries(this.column.append(series.implementation as Column))
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
    Double mean(@NamedParam(required = false) boolean skipNa, @NamedParam(required = false) int precision) {
        assert column instanceof DoubleColumn, "Can't calculate the mean of a Series of type ${column.type()}"
        NumericColumn meanCol = skipNa ? column.removeMissing() : column
        return new BigDecimal(meanCol.mean(), new MathContext(precision ?: 7, RoundingMode.HALF_EVEN))
    }

    @Override
    Double mean() {
        return this.mean(skipNa:  false)
    }

    @Override
    Series div(Series series) {
        Column seriesColumn = series.implementation as Column

        assert column instanceof NumericColumn, "Can't divide a non numeric column"
        assert seriesColumn instanceof NumericColumn, "Can't divide a numeric column by a non numeric column"

        return new TSSeries(column.divide(seriesColumn))
    }

    @Override
    Criteria isGreaterThan(Number value) {
        assert column instanceof NumericColumn, "Can't compare value ${number} against a non numeric column"
        return new TSCriteria(column.isGreaterThan(value.toDouble()))
    }

    @Override
    Criteria isLessThan(Number value) {
        assert column instanceof NumericColumn, "Can't compare value ${number} against a non numeric column"
        return new TSCriteria(column.isLessThan(value.toDouble()))
    }

    @Override
    Criteria isEqualTo(Number number) {
        assert column instanceof NumericColumn, "Can't compare value ${number} against a non numeric column"
        return new TSCriteria(column.isEqualTo(number.toDouble()))
    }

    @Override
    Criteria isEqualTo(String value) {
        assert column instanceof StringColumn, "Can't compare a string ${value} against a non string column"
        return new TSCriteria(column.isEqualTo(value))
    }

    @Override
    Series multiply(Number number) {
        if (column instanceof NumericColumn){
            return new TSSeries(column * number)
        }

        if (column instanceof StringColumn) {
            return new TSSeries(column.map(st -> st * number.toInteger()))
        }

        throw new RuntimeException("can't multiply ${number} by a series of type ${column.type()}")
    }

    @Override
    List<Integer> toIntegerList() {
        if (column instanceof NumericColumn){
            return column.asIntColumn().toList()
        }

        if (column instanceof StringColumn) {
            return column
                .map(st -> st.isNumber() ? st : StringColumnType.missingValueIndicator())
                .asDoubleColumn()
                .asIntColumn()
                .toList()
        }

        throw new RuntimeException("Can't convert series of type ${column.type()} to list of integers")
    }

    @Override
    @NamedVariant
    Series toNumeric(String errors) {
        if (column instanceof NumericColumn) {
            return this
        }

        if (column instanceof StringColumn) {
            DoubleColumn newCol = DoubleColumn.create(column.name())
            for (String val : column) {
                newCol.append(val.isNumber()
                    ? val.toDouble()
                    : DoubleColumnType.missingValueIndicator())
            }
            return new TSSeries(newCol)
        }

        throw new RuntimeException("Can't convert series of type ${column.type()} to numeric")
    }

    @Override
    DataFrame describe() {
        return new TSDataFrame(this.column.summary())
    }

    @Override
    String toString() {
        return column.asList().toString()
    }

    Object asType(Class clazz){
        if (Series.isAssignableFrom(clazz)) {
            return this
        }

        if (clazz.isArray()) {
            return DefaultGroovyMethods.asType(column.toList(), clazz as Class<Object>)
        }

        return DefaultGroovyMethods.asType(column, clazz as Class<Object>)
    }
}