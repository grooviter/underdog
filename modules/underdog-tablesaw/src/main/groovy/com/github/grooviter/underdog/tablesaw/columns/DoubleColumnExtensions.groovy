package com.github.grooviter.underdog.tablesaw.columns

import tech.tablesaw.api.DoubleColumn
import tech.tablesaw.selection.Selection

class DoubleColumnExtensions {
    static Selection inList(DoubleColumn numberColumn, Collection<Number> numbers) {
        return numberColumn.isIn(numbers as double[])
    }

    static Selection inList(DoubleColumn numberColumn, IntRange numbers) {
        return numberColumn.isIn(numbers*.doubleValue() as double[])
    }
}
