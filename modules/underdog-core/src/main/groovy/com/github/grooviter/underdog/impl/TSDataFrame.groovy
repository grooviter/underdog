package com.github.grooviter.underdog.impl

import com.github.grooviter.underdog.Columnar
import com.github.grooviter.underdog.Criteria
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
import tech.tablesaw.api.ColumnType
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
        @NamedParam TypeJoin how = TypeJoin.inner,
        @NamedParam List<String> on,
        @NamedParam List<String> left_on,
        @NamedParam List<String> right_on,
        @NamedParam boolean left_index,
        @NamedParam boolean right_index,
        @NamedParam boolean sort,
        @NamedParam List<String> suffixes,
        @NamedParam boolean copy) {

        Table rightTable = ((TSDataFrame) right).implementation as Table

        return switch(how){
            case TypeJoin.outer -> this // joiner.fullOuter(rightTable)
            case TypeJoin.inner -> innerJoin(on, left_on, right_on, this.table, rightTable)
            case TypeJoin.left  -> this // joiner.leftOuter(rightTable, right_on as String[])
            case TypeJoin.right -> this // joiner.rightOuter(rightTable, right_on as String[])
            default             -> innerJoin(on, left_on, right_on, this.table, rightTable)
        }
    }

    private static DataFrame innerJoin(List<String> on, List<String> left_on, List<String> right_on, Table left, Table right) {
        String[] leftKeys = [left_on, on].grep().find() as String[]
        String[] rightKeys = [right_on, on].grep().find() as String[]
        return new TSDataFrame(left.joinOn(leftKeys).inner(right, rightKeys))
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
    DataFrame rename(
            @NamedParam(required = false) Map<String, String> mapper,
            @NamedParam(required = false) Function<String, String> fn,
            @NamedParam(required = false) List<String> columns,
            @NamedParam(required = false) boolean copy) {

        if (columns) {
            this.renameByList(columns)
        }

        if (mapper){
            this.renameByMapper(mapper)
        }

        if (fn) {
            this.renameByFn(fn)
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
