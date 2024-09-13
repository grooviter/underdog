package com.github.grooviter.underdog.tablesaw.columns

import tech.tablesaw.api.StringColumn
import tech.tablesaw.selection.Selection

class StringColumnExtensions {
    static Selection matches(StringColumn column, String regex) {
        return column.matchesRegex(regex)
    }

    static Selection equals(StringColumn column, String text) {
        return column.isEqualTo(text)
    }
}
