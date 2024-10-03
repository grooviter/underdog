package com.github.grooviter.underdog.impl

import com.github.grooviter.underdog.Criteria
import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.Series
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.NullCheck
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam
import org.apache.commons.math3.stat.correlation.KendallsCorrelation
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation
import org.codehaus.groovy.runtime.DefaultGroovyMethods
import tech.tablesaw.api.BooleanColumn
import tech.tablesaw.api.ColumnType
import tech.tablesaw.api.DoubleColumn
import tech.tablesaw.api.IntColumn
import tech.tablesaw.api.NumericColumn
import tech.tablesaw.api.Row
import tech.tablesaw.api.StringColumn
import tech.tablesaw.columns.Column
import tech.tablesaw.columns.numbers.DoubleColumnType

import java.math.MathContext
import java.math.RoundingMode
import java.util.function.Function

import static com.github.grooviter.underdog.Series.TypeCorrelation.KENDALL
import static com.github.grooviter.underdog.Series.TypeCorrelation.PEARSON
import static com.github.grooviter.underdog.Series.TypeCorrelation.SPEARMAN

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
    def <P, O> Series call(Class<P> clazz, Class<O> output, @ClosureParams(value = FirstParam.FirstGenericType, options='P') Closure func) {
        Column newCol = switch(output) {
            case Integer -> ColumnType.INTEGER.create("random")
        }
        (0..<this.size()).each { newCol.appendMissing() }
        Function<P, O> fn = i -> func(i)
        return new TSSeries(this.column.mapInto(fn, newCol))
    }

    @Override
    Series categorize() {
        IntColumn categoryCol = IntColumn.create(this.column.name())

        column.each { categoryCol.appendMissing() }

        if (column instanceof StringColumn) {
            Map<String, Integer> index = column
                .unique()
                .indexed()
                .collectEntries { k, v -> [(v): k] }
            column.mapInto((String next) -> index[next], categoryCol)
        }

        if (column instanceof BooleanColumn) {
            column.mapInto((Boolean next) -> next ? 1 : 0, categoryCol)
        }

        return new TSSeries(categoryCol)
    }

    @Override
    float corr(Series other) {
        return this.corr(other, null, null)
    }

    @Override
    @NamedVariant
    float corr(
        @NamedParam(required = true) Series other,
        @NamedParam(required = false) TypeCorrelation method = PEARSON,
        @NamedParam(required = false) Integer observations) {

        def (alignedX, alignedY) = [this as Double[], other as Double[]]
            .transpose()
            .<List<Double>>findAll(Object::every)
            .inject([[], []]) { agg,  next ->
                agg[0] << next[0]
                agg[1] << next[1]
                agg
            } as List<Double[]>

        double[] x = (observations ? alignedX[0..<observations] : alignedX) as double[]
        double[] y = (observations ? alignedY[0..<observations] : alignedY) as double[]

        return switch(method) {
            case PEARSON -> new PearsonsCorrelation().correlation(x, y)
            case KENDALL -> new KendallsCorrelation().correlation(x, y)
            case SPEARMAN -> new SpearmansCorrelation().correlation(x, y)
            default -> new PearsonsCorrelation().correlation(x, y)
        }
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
    Criteria isNotEqualTo(String value) {
        assert column instanceof StringColumn, ""
        return new TSCriteria(column.isNotEqualTo(value))
    }

    @Override
    Criteria isNotEqualTo(Number value) {
        assert column instanceof NumericColumn, ""
        return new TSCriteria(column.isNotEqualTo(value.toDouble()))
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
    Series minus(Series series) {
        Column left = this.implementation as Column
        Column right = series.implementation as Column

        if (left instanceof NumericColumn && right instanceof NumericColumn) {
            return new TSSeries(left.subtract(right))
        }

        if (left instanceof StringColumn && right instanceof StringColumn) {
            int col1Size = left.size();
            int col2Size = right.size();

            for (int r = 0; r < col1Size; ++r) {
                left.set(r, left.getString(r) - right.getString(r))
            }
        }

        return new TSSeries(left)
    }

    @Override
    Series minus(Object value) {
        return new TSSeries(column.map(val -> val - value))
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

        if (clazz.isArray() || List.isAssignableFrom(clazz)) {
            return DefaultGroovyMethods.asType(convertToListWithNulls(column), clazz as Class<Object>)
        }

        return DefaultGroovyMethods.asType(column, clazz as Class<Object>)
    }

    private static List convertToListWithNulls(Column column) {
        return column
            .toList()
            .indexed()
            .collect { Integer index, Object v -> column.isMissing(index) ? null : v }
    }

    @Override
    Series dropna() {
        return new TSSeries(column.removeMissing())
    }
}