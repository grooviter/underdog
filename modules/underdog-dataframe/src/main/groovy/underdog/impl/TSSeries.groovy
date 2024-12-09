package underdog.impl

import underdog.DataFrame
import underdog.Series
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
import tech.tablesaw.api.StringColumn
import tech.tablesaw.columns.Column
import tech.tablesaw.columns.numbers.DoubleColumnType

import java.util.function.Function

import static underdog.Series.TypeCorrelation.KENDALL
import static underdog.Series.TypeCorrelation.PEARSON
import static underdog.Series.TypeCorrelation.SPEARMAN

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
    String getName() {
        return this.column.name()
    }

    @Override
    Series rename(String newName) {
        return new TSSeries(this.column.setName(newName))
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
    <P, O> Series call(Class<P> clazz, Class<O> output, @ClosureParams(value = FirstParam.FirstGenericType, options='P') Closure func) {
        ColumnType newColType = switch(output) {
            case BigInteger -> ColumnType.LONG
            case BigDecimal -> ColumnType.DOUBLE
            case Date       -> ColumnType.LOCAL_DATE
            default -> ColumnType.valueOf(output.simpleName.toUpperCase())
        }
        Column newCol = newColType.create("random")
        (0..<this.size()).each { newCol.appendMissing() }
        Function<P, O> fn = i -> func(i)
        return new TSSeries(this.column.mapInto(fn, newCol))
    }

    @Override
    <P, O> Series call(Class<P> clazz, Class<O> output, Function<P, O> converter) {
        ColumnType newColType = switch(output) {
            case BigInteger -> ColumnType.LONG
            case BigDecimal -> ColumnType.DOUBLE
            default -> ColumnType.valueOf(output.simpleName.toUpperCase())
        }
        Column newCol = newColType.create("random")
        (0..<this.size()).each { newCol.appendMissing() }
        return new TSSeries(this.column.mapInto(converter, newCol))
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
    @NamedVariant
    float corr(
        @NamedParam(required = true) Series other,
        @NamedParam(required = false) TypeCorrelation method = PEARSON,
        @NamedParam(required = false) Integer observations = 0) {

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
    Long size() {
        return this.column.size()
    }

    @Override
    Series unique() {
        return new TSSeries(this.column.unique())
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
    String[] toStringArray() {
        return this.column.asStringColumn().asList() as String[]
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
            return DefaultGroovyMethods.asType(toList(), clazz as Class<Object>)
        }

        return DefaultGroovyMethods.asType(column, clazz as Class<Object>)
    }


    @Override
    Series dropna() {
        return new TSSeries(column.removeMissing())
    }

    @Override
    Series lag(int index) {
        return new TSSeries(this.column.lag(index))
    }

    @NamedVariant
    Series sort(@NamedParam(required = false) boolean descending = false) {
        if (descending) {
            this.column.sortDescending()
        } else {
            this.column.sortAscending()
        }

        return new TSSeries(this.column)
    }

    @Override
    Iterator iterator() {
        return this.column.iterator()
    }

    @Override
    <U> List<U> toList() {
        return column
            .toList()
            .indexed()
            .collect { Integer index, Object v -> column.isMissing(index) ? null : v } as List<U>
    }
}