package com.github.grooviter.underdog.tablesaw.columns

import tech.tablesaw.api.IntColumn
import tech.tablesaw.selection.Selection

class IntColumnExtensions {
    static Selection inList(IntColumn numberColumn, Collection<Number> numbers) {
        return numberColumn.isIn(numbers as int[])
    }

    static Selection inList(IntColumn numberColumn, IntRange numbers) {
        return numberColumn.isIn(numbers.collect() as int[])
    }
}
