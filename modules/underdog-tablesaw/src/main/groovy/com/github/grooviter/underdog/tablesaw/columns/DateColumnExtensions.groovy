package com.github.grooviter.underdog.tablesaw.columns

import tech.tablesaw.api.DateColumn
import tech.tablesaw.api.DateTimeColumn
import tech.tablesaw.selection.Selection

import java.time.LocalDate
import java.time.LocalDateTime

class DateColumnExtensions {

    static <U> Selection isGreaterThan(DateColumn source, LocalDate localDate) {
        return source.isAfter(localDate)
    }

    static <U> Selection isGreaterThan(DateTimeColumn source, LocalDateTime localDateTime) {
        return source.isAfter(localDateTime)
    }

    static <U> Selection isGreaterThan(DateTimeColumn source, LocalDate localDate) {
        return source.isAfter(localDate)
    }

    static <U> Selection isGreaterThanOrEqualTo(DateColumn source, LocalDate localDate) {
        return source.isOnOrAfter(localDate)
    }

    static <U> Selection isLessThan(DateColumn source, LocalDate localDate) {
        return source.isBefore(localDate)
    }

    static <U> Selection isLessThanOrEqualTo(DateColumn source, LocalDate localDate) {
        return source.isOnOrBefore(localDate)
    }
}
