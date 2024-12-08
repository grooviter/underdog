package underdog.impl

import tech.tablesaw.api.NumericColumn
import tech.tablesaw.columns.strings.StringColumnType
import underdog.Columnar
import underdog.DataFrame
import underdog.DataFrameAggregation
import underdog.DataFrameIloc
import underdog.DataFrameLoc
import underdog.Series
import underdog.Shape
import underdog.TypeApplyByRow
import underdog.TypeApplyResult
import underdog.TypeAxis
import underdog.TypeJoin
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

    static DataFrame from(String name){
        return new TSDataFrame(Table.create(name))
    }

    static DataFrame from(String dataFrameName, Collection<Map<String, ?>> collection) {
        Map<String,?> sample = collection.find()
        List<String> colNames = sample.keySet().toList()
        ColumnType[] colTypes = new CollectionTypeDetector().detectFromMapOfValues(sample)

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

    static DataFrame from(String dataFrameName, Map<String,Iterable> fromMap) {
        List<String> columnNames = fromMap.keySet().toList()
        ColumnType[] columnTypes = new CollectionTypeDetector().detectFromMapOfLists(fromMap)

        List<Column> columns = [columnNames, columnTypes].transpose()
                .collect { String name, ColumnType type -> type
                    .create(name)
                    .tap { Column column ->
                        fromMap[name].each { column.append(it) }
                    }
                }

        return new TSDataFrame(Table.create(dataFrameName, columns))
    }

    @Override
    DataFrame getT() {
        return new TSDataFrame(this.table.transpose())
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
    DataFrame first() {
        return new TSDataFrame(this.table.first(1))
    }

    @Override
    DataFrame first(int rows) {
        return new TSDataFrame(this.table.first(rows))
    }

    @Override
    DataFrame last() {
        return new TSDataFrame(this.table.last(1))
    }

    @Override
    DataFrame last(int rows) {
        return new TSDataFrame(this.table.last(rows))
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
        assert this.table.columns().every { it instanceof NumericColumn }, "abs() can't only be applied to numeric series"
        this.columns.each {
            this[it] = this[it](Number, Number){ it.abs() }
        }
        return this
    }

    @Override
    @NamedVariant
    DataFrame add(
            DataFrame other,
            @NamedParam(required = false) boolean inPlace = false,
            @NamedParam(required = false) TypeAxis axis = TypeAxis.rows,
            @NamedParam(required = false) Object fill = null,
            @NamedParam(required = false) String index = null) {
        Table thisTable = inPlace ? this.table : this.table.copy()
        Table otherTable = other.implementation as Table

        if (axis == TypeAxis.columns){
            return new TSDataFrame(thisTable.concat(otherTable))
        }


        if (index) {
            thisTable.each {
                def currentIndex = it.getObject(index)
                thisTable.columnNames()
                        .findAll {it != index }
                        .each { currentColumn ->
                            def thisValue = it.getObject(currentColumn) ?: fill
                            def otherTablePointer = otherTable.where(otherTable.column(index).isEqualTo(currentIndex))
                            def otherValue = otherTablePointer.size() > 0
                                    ? otherTablePointer.row(0).getObject(currentColumn)
                                    : fill
                            thisTable.column(currentColumn).set(it.rowNumber, (thisValue + otherValue) as Object)
                        }
            }
        } else {
            def thisTableSize =thisTable.size()
            def otherTableSize = otherTable.size()

            if (thisTableSize < otherTableSize) {
                (thisTableSize..<otherTableSize).each {
                    thisTable.appendRow()
                }
            }

            otherTable
                    .columns()
                    .findAll { it.name() in thisTable.columnNames() }
                    .each { Column otherColumn ->
                        otherColumn.eachWithIndex { Object otherValue = fill, i ->
                            def thisColumn = thisTable.column(otherColumn.name())
                            def thisValue = thisColumn.get(i) ?: fill
                            thisColumn.set(i, otherValue + thisValue as Object)
                        }
                    }
        }

        otherTable
            .columns()
            .findAll { it.name() !in thisTable.columnNames() }
            .each { Column newColumn ->
                Column columnToAdd = newColumn.copy()
                if (newColumn.size() < thisTable.size()) {
                    (columnToAdd.size()..<thisTable.size()).each {
                        columnToAdd.appendMissing()
                    }
                }
                thisTable.addColumns(columnToAdd)
            }

        return new TSDataFrame(thisTable)
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

        List list = this.toList()

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
        return new TSDataFrame(this.table.removeColumns(labels))
    }

    @Override
    DataFrame dropna() {
        return new TSDataFrame(this.table.dropRowsWithMissingValues())
    }

    String getName() {
        return this.table.name()
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
            @NamedParam boolean allowDuplicateColumns,
            @NamedParam boolean copy) {

        TSDataFrameJoinInfo joinInfo = TSDataFrameJoinInfo.builder()
            .left(this)
            .right(right)
            .leftOn(left_on)
            .rightOn(right_on)
            .on(on)
            .how(how)
            .allowDuplicateColumns(allowDuplicateColumns)
            .build()

        return join(joinInfo)
    }

    private static DataFrame join(TSDataFrameJoinInfo info) {
        DataFrameJoiner joiner = info.leftTable().joinOn(info.leftKeys())

        Table merged = switch(info.how){
            case TypeJoin.OUTER -> joiner
                    .fullOuter(info.rightTable(), info.allowDuplicateColumns, false, info.rightKeys())

            case [TypeJoin.INNER, TypeJoin.LEFT_INNER] -> joiner
                    .inner(info.rightTable(), info.allowDuplicateColumns, false, info.rightKeys())

            case TypeJoin.RIGHT_INNER -> info
                    .rightTable()
                    .joinOn(info.rightKeys())
                    .inner(info.leftTable(), info.allowDuplicateColumns, false, info.leftKeys())

            case TypeJoin.LEFT_OUTER -> joiner
                    .leftOuter(info.rightTable(), info.allowDuplicateColumns, false, info.rightKeys())

            case TypeJoin.RIGHT_OUTER -> joiner
                    .rightOuter(info.rightTable(), info.allowDuplicateColumns, false, info.rightKeys())

            default -> joiner.inner(info.rightTable(), info.rightKeys())
        }

        return new TSDataFrame(merged)
    }

    @Override
    @NamedVariant
    Series min(TypeAxis axisType) {
        return null
min    }

    @Override
    DataFrame minus(Series series) {
        return new TSDataFrame(this.table.removeColumns(series.implementation as Column))
    }

    @Override
    @NamedVariant
    Series max(TypeAxis axisType) {
        return null
    }

    @Override
    DataFrame nlargest(Integer n) {
        return new TSDataFrame(this.table.first(n))
    }

    @Override
    @NamedVariant
    DataFrame nlargest(Integer n, @NamedParam(required = false) List<String> columns) {
        return new TSDataFrame(this.table.sortDescendingOn(columns as String[]).first(n))
    }

    @Override
    @NamedVariant
    DataFrame renameSeries(
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
    DataFrame renameSeries(
        @ClosureParams(
            value = FromString,
            options = ['java.lang.Integer,java.lang.String']) Closure<String> function) {
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
    void putAt(String colName, List values) {
        if (values.size() <= 0) {
            return
        }

        ColumnType columnType = ColumnType.valueOf(values.find().class.simpleName.toUpperCase())
        Column column = columnType.create(colName)
        values.each(column::append)
        putAt(colName, new TSSeries(column))
    }

    @Override
    void putAt(String colName, Object object) {
        ColumnType columnType = object
            ? ColumnType.valueOf(object?.class?.simpleName?.toUpperCase())
            : ColumnType.valueOf('STRING')

        Column column = columnType.create(colName)
        this.size().times {
            if (object) {
                column.append(object)
            } else {
                column.appendMissing()
            }
        }
        putAt(colName, new TSSeries(column))
    }

    @Override
    Shape shape() {
        return new Shape(this.table.rowCount(), this.table.columnCount())
    }

    @Override
    DataFrameAggregation agg(Map<String, ?> aggFn) {
        List<DataFrameAggregation.SeriesInfo> seriesInfoList = aggFn.collect { String colName, Object func ->
            if (func instanceof List) {
                return new DataFrameAggregation.SeriesInfo(colName, func)
            }
            return new DataFrameAggregation.SeriesInfo(colName, [func.toString()])
        }

        return  new TSDataFrameAggregation(new DataFrameAggregation.AggregationInfo(seriesInfoList), this.table)
    }

    @Override
    DataFrame rename(String name) {
        return new TSDataFrame(this.table.setName(name))
    }

    @Override
    DataFrame schema() {
        return new TSDataFrame(this.table.structure())
    }

    @Override
    <U> List<U> toList() {
        return table.collect { Row row ->
            row.columnNames().collect { String colName ->
                table.column(colName).get(row.rowNumber)
            }
        } as List<U>
    }

    @Override
    @NamedVariant
    DataFrame xTabCounts(@NamedParam String labels, @NamedParam String values) {
        return new TSDataFrame(this.table.xTabCounts(labels, values))
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

        return new TSDataFrame(this.table)
    }
}
