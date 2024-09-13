package com.github.grooviter.underdog.tablesaw

import groovy.transform.NamedParam
import groovy.transform.NamedParams
import groovy.transform.NamedVariant
import tech.tablesaw.api.ColumnType
import tech.tablesaw.api.IntColumn
import tech.tablesaw.api.NumberColumn
import tech.tablesaw.api.StringColumn
import tech.tablesaw.api.Table
import tech.tablesaw.columns.Column

class ListExtensions {

    static StringColumn asType(List<String> values, Class<StringColumn> clazz) {
        return StringColumn.create("", values)
    }

    @NamedVariant
    static <T extends Column> Table toTable(
        List <T> columns,
        @NamedParam(required = true) String tableName,
        @NamedParam(required = true) List<String> colNames) {
        def colsAndNames = [colNames, columns].transpose()
            .collect { String name, StringColumn colDef ->
                return StringColumn.create(name).append(colDef)
            }

        return Table.create(tableName, colsAndNames)
    }
}
