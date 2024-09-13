package com.github.grooviter.underdog.tablesaw

import com.github.grooviter.underdog.tablesaw.test.BaseSpec
import com.github.grooviter.underdog.tablesaw.test.NumericAware
import com.github.grooviter.underdog.tablesaw.test.TextAware
import tech.tablesaw.api.DoubleColumn
import tech.tablesaw.api.IntColumn
import tech.tablesaw.api.Table
import tech.tablesaw.api.TextColumn
import tech.tablesaw.columns.Column
import tech.tablesaw.selection.Selection

class TableExtensionsSpec extends BaseSpec implements NumericAware, TextAware {
    void 'column rows by loc(row0...rowN, colName)'() {
        when:
        Column idColumn = foodTable.loc(0..2, "ID")

        then:
        idColumn.name() == 'ID'

        and:
        idColumn.size() == 3
    }

    void 'column rows by loc(colName)'() {
        when:
        Table table = foodTable.loc("ID", "CARBS")

        then:
        table.columnNames() == ["ID", "CARBS"]

        and:
        table.size() == foodTable.size()
    }

    void 'column rows by iloc(row0...rowN, colIndex)'() {
        when:
        Column nameColumn = foodTable.iloc(0..2, 1)

        then:
        nameColumn.name() == 'NAME'

        and:
        nameColumn.size() == 3
    }

    void 'table rows by loc(row0...rowN, [colName0, colName1, colNameN])'() {
        when:
        Table partial = foodTable.loc(0..2, ["ID", "NAME"])

        then:
        partial.columnNames() == ['ID', 'NAME']

        and:
        partial.size() == 3
    }

    void 'table rows by iloc(row0...rowN, [colIndex0, colIndex1, colIndexN])'() {
        when:
        Table partial = foodTable.iloc(0..2, [0, 2])

        then:
        partial.columnNames() == ['ID', 'BRAND ID']

        and:
        partial.size() == 3
    }

    void 'table rows by [row0...rowN] operator'() {
        when:
        Table partial = foodTable[0..2]

        then:
        partial.columnNames().size() == 20

        and:
        partial.size() == 3
    }

    void 'table rows by [row0...rowN, [colName0, colName1, colName2] operator'() {
        when:
        Table partial = foodTable[0..2, ["ID", "NAME"]]

        then:
        partial.columnNames() == ['ID', 'NAME']

        and:
        partial.size() == 3
    }

    void 'table rows by [row0..rowN, [colIndex0, colIndex1, colIndexN]] operator'() {
        when:
        Table partial = foodTable[0..2, [1]]

        then:
        partial.columnNames() == ['NAME']

        and:
        partial.size() == 3
    }

    void 'column rows by [row0...rowN, colName] operator'() {
        when:
        Column idColumn = foodTable[0..2, "ID"]

        then:
        idColumn.name() == 'ID'

        and:
        idColumn.size() == 3
    }

    void 'table rows by [row0..rowN, col...col] operator'() {
        when:
        Table partial = foodTable[0..2, 0..1]

        then:
        partial.columnNames() == ['ID', 'NAME']

        and:
        partial.size() == 3
    }

    void 'column rows by [row0...rowN, colIndex] operator'() {
        when:
        Column partial = foodTable[0..2, 1]

        then:
        partial.name() == 'NAME'

        and:
        partial.size() == 3
    }

    void 'add column by table[colName] = column operator'() {
        given:
        Table partial = foodTable.loc(0..3, ["NAME"])
        Integer initColSize = partial.columnCount()

        when:
        partial['NAME_AGAIN'] = partial["NAME"]

        then:
        partial['NAME_AGAIN'].size() == partial['NAME'].size()

        and:
        partial.columnCount() == initColSize + 1
    }

    void 'add same column twice'() {
        given:
        Table partial = foodTable.loc(0..3, ["NAME"])
        Integer initColSize = partial.columnCount()

        when:
        partial['NAME_AGAIN'] = partial["NAME"]
        partial['NAME_AGAIN'] = partial["NAME"]

        then:
        partial['NAME_AGAIN'].size() == partial['NAME'].size()

        and:
        partial.columnCount() == initColSize + 1
    }

    void 'remove column by table[colName] - column operator'() {
        given:
        Table partial = foodTable.loc(0..3, ["ID", "NAME"])
        Integer initColSize = partial.columnCount()

        when: "removing a column from a table"
        Table result = partial - partial["NAME"]

        then: "it produces the expected result"
        result.columnCount() == initColSize - 1

        when: "doing it again"
        result = partial - partial["NAME"]

        then: "it produces always the same result"
        result.columnCount() == initColSize - 1
    }

    void "taking all rows by table[_, List<String>]"() {
        when:
        List<String> colNames = ['ID', 'NAME']
        Table partial = foodTable[__, colNames]

        then:
        partial.rowCount() == 3197

        and:
        partial.columnCount() == 2
    }

    void "taking all rows by table[_, ['ID', 'NAME']]"() {
        when:
        Table partial = foodTable[__, ['ID', 'NAME']]

        then:
        partial.rowCount() == 3197

        and:
        partial.columnCount() == 2
    }

    void 'taking all rows by table[_, List<Integer>]'() {
        when:
        List<Integer> colIndexes = [0, 1]
        Table partial = foodTable[__, colIndexes]

        then:
        partial.rowCount() == 3197

        and:
        partial.columnCount() == 2
    }

    void 'taking all rows by table[_, [0, 1]]'() {
        when:
        Table partial = foodTable[__, [0, 1]]

        then:
        partial.rowCount() == 3197

        and:
        partial.columnCount() == 2
    }

    void 'taking all rows by table[_, 0..1]'() {
        when:
        Table partial = foodTable[__, 0..1]

        then:
        partial.rowCount() == 3197

        and:
        partial.columnCount() == 2
    }

    void 'taking all rows by table[_, ref]'() {
        when:
        Table partial = ratesTable[__, ref]

        then:
        partial.rowCount() == 313

        and:
        partial.columnCount() == 2

        where:
        ref << [
            ['y1', 'y2'],
            (1..2) >>> 'y$it',
            (1..2).collect {"y$it" },
            3..4,
        ]
    }

    void 'taking all rows by table[_, inline-expr]'() {
        when:
        Table partial = ratesTable[__, (1..2) >>> 'y$it']

        then:
        partial.rowCount() == 313

        and:
        partial.columnNames() == ['y1', 'y2']
    }

    void 'table rows filtered by selection table = table[Selection]'() {
        when:
        Table partial = foodTable[foodTable['SUGAR', DoubleColumn].isCloseTo(3.5, 0.05)]

        then:
        partial.size() == 56
    }

    void 'table rows filtered by selection and columns table = table[Selection, [colName1, colNameN]'() {
        when:
        Table partial = foodTable[foodTable['SUGAR', DoubleColumn].isCloseTo(3.5, 0.05), ["ID", "NAME"]]

        then:
        partial.size() == 56
        partial.columnCount() == 2
    }

    void 'column rows filtered by selection and columns col = table[Selection, colName]'() {
        when:
        Column ids = foodTable[foodTable['SUGAR', DoubleColumn].isCloseTo(3.5, 0.05), "ID"]

        then:
        ids.size() == 56
    }

    void 'get typed column from table col = table[colName, colTypeClass]'() {
        when:
        DoubleColumn typedColumn = foodTable['SUGAR', DoubleColumn]
        Selection closeTo3andHalf = typedColumn.isCloseTo(3.5, 0.05)
        Column ids = foodTable[closeTo3andHalf, "ID"]

        then:
        ids.size() == 56
    }

    void 'drop NaN from all rows'() {
        when:
        Table withoutNaN = foodTable.dropna()

        then:
        foodTable.size() > withoutNaN.size()

        and:
        withoutNaN != foodTable
    }

    void 'renaming columns'() {
        given:
        Map<String,String> MAPPINGS = [
            'TRAFFICLIGHT VALUE': 'traffic_light',
            'PYRAMID VALUE': 'pyramid_val'
        ]

        and:
        Table renamedCols = foodTable.renameColumns(MAPPINGS)

        when:
        Table result = renamedCols[1..10, ['traffic_light', 'pyramid_val']].copy()

        then:
        result.rowCount()    == 10
        result.columnCount() == 2
    }


    void 'assign an array of double[] to a DoubleColumn'() {
        when:
        foodTable['SPEC'] = zerosAsDouble(foodTable.size())

        then:
        foodTable['SPEC'] instanceof DoubleColumn
        foodTable['SPEC'].size() == foodTable.size()
    }

    void 'assign an array of int[] to a IntColumn'() {
        when:
        foodTable['SPEC'] = zeros(foodTable.size())

        then:
        foodTable['SPEC'] instanceof IntColumn
        foodTable['SPEC'].size() == foodTable.size()
    }

    void 'assign an array of String[] to a TextColumn'() {
        when:
        foodTable['SPEC'] = word(foodTable.size())

        then:
        foodTable['SPEC'] instanceof TextColumn
        foodTable['SPEC'].size() == foodTable.size()
    }

    void 'converting table to numerical matrices'() {
        when:
        def matrix = foodTable.loc(0..10, ['SUGAR', 'CARBS']) as double[][]

        then:
        matrix instanceof double[][]

        when:
        matrix = foodTable.loc(0..10, ['BRAND ID', 'GROUP ID']) as int[][]

        then:
        matrix instanceof int[][]

        when:
        matrix = foodTable.loc(0..10, ['FIBER', 'SODIUM']) as float[][]

        then:
        matrix instanceof float[][]
    }

    void 'summing columns, table[_, [col1, col2]].sum()'() {
        given:
        def sample = ratesTable[__, ['y1', 'y2', 'y3']].copy() as Table

        when:
        sample['sum'] = sample.dropna().rowSum()

        then:
        sample['sum'].size() == ratesTable.size()

        and:
        sample['sum'][0] == (ratesTable['y1'] + ratesTable['y2'] + ratesTable['y3'])[0]
    }
}
