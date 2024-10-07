package com.github.grooviter.underdog.impl

import com.github.grooviter.underdog.Columnar
import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.DataFrameAggregation
import com.github.grooviter.underdog.DataFrameIloc
import com.github.grooviter.underdog.DataFrameLoc
import com.github.grooviter.underdog.Series
import com.github.grooviter.underdog.TypeApplyByRow
import com.github.grooviter.underdog.TypeApplyResult
import com.github.grooviter.underdog.TypeAxis
import com.github.grooviter.underdog.TypeJoin
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import org.codehaus.groovy.runtime.DefaultGroovyMethods
import tech.tablesaw.aggregate.AggregateFunctions
import tech.tablesaw.api.ColumnType
import tech.tablesaw.api.DoubleColumn
import tech.tablesaw.api.Row
import tech.tablesaw.api.Table
import tech.tablesaw.columns.Column
import tech.tablesaw.joining.DataFrameJoiner
import tech.tablesaw.selection.Selection

import java.util.function.Function

/**
 * @since 0.1.0
 */
class TSDataFrame implements DataFrame {
    private Table table

    TSDataFrame(Table table){
        this.table = table
    }

    static DataFrame from(String dataFrameName, Collection<Map<String, ?>> collection) {
        Map<String,?> sample = collection.find()
        List<String> colNames = sample.keySet().toList()
        ColumnType[] colTypes = new CollectionTypeDetector()
                .detectFromMapOfValues(sample)
                .<Class, ColumnType>collect((Class clazz) -> resolveFrom(clazz.simpleName))

        List<Column> columns = [colNames, colTypes].transpose()
                .collect { String name, ColumnType type -> type.create(name) }

        Map<String, Column> columnByName = columns.collectEntries { [(it.name()): it] }

        collection.each { Map<String,?> row ->
            columnByName.each { String k, Column v ->
                v.append(row[k])
            }
        }

        return new TSDataFrame(Table.create(dataFrameName, columns))
    }

    static DataFrame from(String dataFrameName, Map<String,List> fromMap) {
        List<String> columnNames = fromMap.keySet().toList()
        ColumnType[] columnTypes = new CollectionTypeDetector()
            .detectFromMapOfLists(fromMap)
            .<Class, ColumnType>collect((Class clazz) -> resolveFrom(clazz.simpleName))

        List<Column> columns = [columnNames, columnTypes].transpose()
                .collect { String name, ColumnType type -> type
                    .create(name)
                    .tap { Column column -> fromMap[name].each { column.append(it) } }
                }

        return new TSDataFrame(Table.create(dataFrameName, columns))
    }

    private static ColumnType resolveFrom(String clazzName) {
        return switch(clazzName.toUpperCase()) {
            case 'SHORT'                  -> ColumnType.SHORT
            case 'INTEGER'                -> ColumnType.INTEGER
            case ['LONG', 'BIGINTEGER']   -> ColumnType.LONG
            case 'FLOAT'                  -> ColumnType.FLOAT
            case 'BOOLEAN'                -> ColumnType.BOOLEAN
            case 'STRING'                 -> ColumnType.STRING
            case ['DOUBLE', 'BIGDECIMAL'] -> ColumnType.DOUBLE
            case 'LOCALDATE'              -> ColumnType.LOCAL_DATE
            case 'LOCALTIME'              -> ColumnType.LOCAL_TIME
            case 'LOCALDATETIME'          -> ColumnType.LOCAL_DATE_TIME
            case 'INSTANT'                -> ColumnType.INSTANT
            default                       -> ColumnType.STRING
        }
    }

    @Override
    DataFrame getT() {
        this.table = this.table.transpose()
        return this
    }

    @Override
    Object getImplementation() {
        return this.table
    }

    @Override
    DataFrameIloc getIloc() {
        return new TSDataFrameIloc(this.table)
    }

    DataFrameLoc getLoc() {
        return new TSDataFrameLoc(this.table)
    }

    @Override
    List<String> getColumns() {
        return this.table.columnNames()
    }

    @Override
    DataFrame head() {
        return this.head(10)
    }

    @Override
    DataFrame head(int firstNRows) {
        return new TSDataFrame(this.table.rows(0..<firstNRows as int[]))
    }

    @Override
    DataFrame describe() {
        return new TSDataFrame(this.table.summary())
    }

    @Override
    Boolean isEmpty() {
        return this.table.empty
    }

    @Override
    Long size() {
        return this.table.rowCount()
    }

    @Override
    Series getAt(String column) {
        return this.loc[column]
    }

    @Override
    DataFrame getAt(String[] columns) {
        return new TSDataFrame(this.table.selectColumns(columns))
    }

    @Override
    DataFrame getAt(List<String> columns) {
        return new TSDataFrame(this.table.selectColumns(columns as String[]))
    }

    @Override
    DataFrame abs() {
        return null
    }

    @Override
    @NamedVariant
    DataFrame add(
            DataFrame other,
            TypeAxis axis,
            Integer level,
            BigDecimal fill) {
        return null
    }

    @NamedVariant
    DataFrame pivot(String x, String y, String value, String fnName){
        return new TSDataFrame(table.pivot(y, x, value, TSDataFrameUtils.resolveFnByName(fnName)))
    }

    @Override
    DataFrame plus(Number number) {
        return null
    }

    @Override
    @NamedVariant
    DataFrame addPrefix(String prefix, TypeAxis axis) {
        for (Column column : this.table.columns()) {
            column.name = "${prefix}${column.name()}"
        }
        return this
    }

    @Override
    @NamedVariant
    DataFrame addSuffix(@NamedParam(required = true) String suffix, @NamedParam(required = false) TypeAxis axis) {
        return null
    }

    @Override
    // @NamedVariant
    DataFrame agg(Function fn, String fnName, List<String> namedFns, Map colFunc, TypeAxis axis) {
        return null
    }

    @Override
    DataFrame agg(String fnName, TypeAxis axis) {
        return null
    }

    @Override
    DataFrame agg(List<String> namedFns, TypeAxis axis) {
        return null
    }

    @Override
    DataFrame agg(Map colFunc, TypeAxis axis) {
        return null
    }

    @Override
    @NamedVariant
    <T extends Columnar> Tuple2<DataFrame, T> align(
            @NamedParam(required = true) T other,
            @NamedParam(required = false) TypeJoin joinType,
            @NamedParam(required = false) TypeAxis axis,
            @NamedParam(required = false) int level,
            @NamedParam(required = false) Boolean copy) {
        return null
    }

    @Override
    @NamedVariant
    boolean all(TypeAxis axisType, Boolean boolOnly, Boolean skipNa) {
        return false
    }

    @Override
    @NamedVariant
    boolean any(TypeAxis axisType, Boolean boolOnly, Boolean skipNa) {
        return false
    }

    Object asType(Class clazz) {
        if (clazz instanceof DataFrame) {
            return this
        }

        if (clazz.isArray()) {
            return switch(clazz) {
                case int[][]    -> table.as().intMatrix()
                case double[][] -> table.as().doubleMatrix()
                case float[][]  -> table.as().floatMatrix()
                default         -> table.as().doubleMatrix()
            }
        }

        List list = table.collect { Row row ->
            row.columnNames().collect{ String colName ->
                table.column(colName).get(row.rowNumber)
            }
        }

        if (this.size() == 1) {
            return DefaultGroovyMethods.asType(list.find(), clazz)
        }

        return DefaultGroovyMethods.asType(list, clazz)
    }

    @Override
    @NamedVariant
    DataFrame apply(
            Function fn,
            TypeAxis axisType,
            boolean raw,
            TypeApplyResult resultType,
            TypeApplyByRow byRow) {
        return null
    }

    @Override
    DataFrame apply(Closure fn) {
        return null
    }

    @Override
    @NamedVariant
    DataFrame asfreq(String freqExpression, boolean normalize) {
        return null
    }

    @Override
    @NamedVariant
    DataFrame drop(List<String> labels, String levelName, String levelIndex, TypeAxis axisType) {
        return null
    }

    @Override
    DataFrame drop(String... labels) {
        this.table = this.table.removeColumns(labels)
        return this
    }

    @Override
    DataFrame dropna() {
        return new TSDataFrame(this.table.dropRowsWithMissingValues())
    }

    @Override
    @NamedVariant
    DataFrame mean(
            @NamedParam(required = true) TypeAxis axis,
            @NamedParam(required = true) String index,
            @NamedParam(required = false) List<String> cols) {

        if (axis == TypeAxis.rows) {
            return this.meanForRows(index, cols)
        }

        return this.meanForColumns(index, cols)
    }

    private DataFrame meanForRows(String index, List<String> cols) {
        DoubleColumn meanCol = DoubleColumn.create("$index [Mean]")
        List<String> colsToSummarize = cols ?: (this.table.columnNames() - index)

        for (Row row : this.table){
            meanCol.append(colsToSummarize.collect(row::getNumber).average() as double)
        }

        Table meanTable = Table.create(this.table.name())
                .addColumns(this.table.column(index))
                .addColumns(meanCol)

        String[] sortedColumns = [index] + (meanTable.columnNames().sort() - index)
        return new TSDataFrame(meanTable.selectColumns(sortedColumns))
    }

    private DataFrame meanForColumns(String index, List<String> cols) {
        List<String> colsToSummarize = cols ?: (this.table.columnNames() - index)

        Table meanTable = this.table
            .summarize(colsToSummarize, AggregateFunctions.mean)
            .by(index)

        String[] sortedColumns = [index] + (meanTable.columnNames().sort() - index)
        return new TSDataFrame(meanTable.selectColumns(sortedColumns))
    }

    @Override
    @NamedVariant
    DataFrame dropna(@NamedParam(required = false) String by, @NamedParam(required = false) List<String> byColumns) {
        List<String> columns = [[by], byColumns].collectMany { it?:[] }.grep()
        List<Selection> selectionList = columns.collect { this.table.column(it).isMissing() }
        Table filtered = selectionList.inject(table) { agg, val -> agg.dropWhere(val) }
        return new TSDataFrame(filtered)
    }



    @Override
    @NamedVariant
    DataFrame merge(
            DataFrame right,
            @NamedParam TypeJoin how = TypeJoin.INNER,
            @NamedParam List<String> on,
            @NamedParam List<String> left_on,
            @NamedParam List<String> right_on,
            @NamedParam boolean left_index,
            @NamedParam boolean right_index,
            @NamedParam boolean sort,
            @NamedParam List<String> suffixes,
            @NamedParam boolean copy) {

        TSDataFrameJoinInfo joinInfo = TSDataFrameJoinInfo.builder()
            .left(this)
            .right(right)
            .leftOn(left_on)
            .rightOn(right_on)
            .on(on)
            .how(how)
            .build()

        return join(joinInfo)
    }

    private static DataFrame join(TSDataFrameJoinInfo info) {
        DataFrameJoiner joiner = info.leftTable().joinOn(info.leftKeys())

        Table merged = switch(info.how){
            case TypeJoin.OUTER -> joiner
                    .fullOuter(info.rightTable(), false, false, info.rightKeys())

            case [TypeJoin.INNER, TypeJoin.LEFT_INNER] -> joiner
                    .inner(info.rightTable(), info.rightKeys())

            case TypeJoin.RIGHT_INNER -> info
                    .rightTable()
                    .joinOn(info.rightKeys())
                    .inner(info.leftTable(), info.leftKeys())

            case TypeJoin.LEFT_OUTER -> joiner
                    .leftOuter(info.rightTable(), info.rightKeys())

            case TypeJoin.RIGHT_OUTER -> joiner
                    .rightOuter(info.rightTable(), info.rightKeys())

            default -> joiner.inner(info.rightTable(), info.rightKeys())
        }

        return new TSDataFrame(merged)
    }

    @Override
    @NamedVariant
    Series min(TypeAxis axisType) {
        return null
    }

    @Override
    @NamedVariant
    Series max(TypeAxis axisType) {
        return null
    }

    @Override
    @NamedVariant
    DataFrame nlargest(@NamedParam(required = true) Integer n, @NamedParam(required = true) List<String> columns) {
        return new TSDataFrame(this.table.sortDescendingOn(columns as String[]).first(n))
    }

    @Override
    @NamedVariant
    DataFrame rename(
            @NamedParam(required = false) Map<String, String> mapper,
            @NamedParam(required = false) Function<String, String> fn,
            @NamedParam(required = false) List<String> columns,
            @NamedParam(required = false) boolean copy) {

        if (columns) {
            return this.renameByList(columns)
        }

        if (mapper){
            return this.renameByMapper(mapper)
        }

        if (fn) {
            return this.renameByFn(fn)
        }

        return this
    }

    @Override
    DataFrame rename(@ClosureParams(value = FromString, options = ['java.lang.Integer,java.lang.String']) Closure<String> function) {
        this.table.columns().eachWithIndex { col, ix ->
            col.setName(function(ix, col.name()))
        }
        return this
    }

    private DataFrame renameByList(List<String> columns) {
        if (columns && columns.size() == this.table.columnCount()) {
            [columns, this.table.columns()].transpose().each { String newName, Column column ->
                column.name = newName
            }
        }
        return this
    }

    private DataFrame renameByMapper(Map<String, String> mapper) {
        if (mapper){
            mapper.each { k, v ->
                this.table.column(k).name = v
            }
        }
        return this
    }

    private DataFrame renameByFn(Function<String, String> fn) {
        if (fn) {
            this.table.columns().each { Column col ->
                col.name = fn.apply(col.name())
            }
        }
        return this
    }

    @Override
    void putAt(String colName, Series value) {
        Column assigned = fromSeries(value).copy()

        if (colName in this.table.columnNames() && this.table.column(colName).type() == assigned.type()){
            this.table.column(colName).clear()
            this.table.column(colName).append(assigned)
        } else {
            Column newColumn = assigned.type().create(colName)

            if (colName in this.table.columnNames()){
                this.table.removeColumns(colName)
            }

            this.table.addColumns(newColumn.append(assigned))
        }
    }

    @Override
    DataFrameAggregation agg(Map<String, ?> aggFn) {
        List<DataFrameAggregation.SeriesInfo> seriesInfoList = aggFn.collect { String colName, Object func ->
            new DataFrameAggregation.SeriesInfo(colName, [func.toString()])
        }

        return  new TSDataFrameAggregation(new DataFrameAggregation.AggregationInfo(seriesInfoList), this.table)
    }

    private static Column fromSeries(Series value) {
        TSSeries series = value as TSSeries
        return series.implementation as Column
    }

    @Override
    @NamedVariant
    DataFrame sort_values(
        @NamedParam(required = false) boolean skipNa,
        @NamedParam(required = true) Object by
    ){
        if (skipNa) {
            this.table = this.table.dropRowsWithMissingValues()
        }

        if (by instanceof List) {
            this.table = this.table.sortOn(by as String[])
        }

        if (by instanceof String){
            this.table = this.table.sortOn(by)
        }

        return this
    }
}
