package com.github.grooviter.underdog.tablesaw.columns

import com.github.grooviter.underdog.tablesaw.test.BaseSpec
import tech.tablesaw.api.NumberColumn
import tech.tablesaw.api.Table
import tech.tablesaw.columns.Column
import tech.tablesaw.selection.Selection

class NumberColumnExtensionsSpec extends BaseSpec {
    void 'query column with values using "in" syntax numeric range'() {
        when:
        Column ids = foodTable[foodTable['TRAFFICLIGHT VALUE'] in 0..2, "ID"]

        then:
        ids.size() == 923
    }

    void 'query column combining column values in range and another criteria'() {
        when:
        Selection where = foodTable['TRAFFICLIGHT VALUE'] in 1..2 & foodTable['CARBS'] <= 2
        Table rows = foodTable[where, ['ID', 'TRAFFICLIGHT VALUE']]

        then:
        rows.size() == 70
    }

    void 'query column using between operator'() {
        when:
        Selection where = foodTable['SUGAR', NumberColumn].between(0.2..0.4)
        Table rows = foodTable[where, ['ID', 'SUGAR']]

        then:
        rows.rowCount() == 53
    }
}
