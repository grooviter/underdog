package com.github.grooviter.underdog.impl

import com.github.grooviter.underdog.Criteria
import com.github.grooviter.underdog.Series
import tech.tablesaw.api.DateTimeColumn
import tech.tablesaw.api.DoubleColumn
import tech.tablesaw.api.NumericColumn
import tech.tablesaw.columns.Column
import tech.tablesaw.selection.Selection

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class TSSeries implements Series {
    private final Column column

    TSSeries(Column column){
        this.column = column
    }

    @Override
    Object getImplementation() {
        return this.column
    }

    @Override
    Object getAt(Integer index) {
        return this.column.get(index)
    }

    @Override
    Series getIloc() {
        return this
    }

    @Override
    Series plus(Number o) {
        return new TSSeries(this.column.map(n -> n + o))
    }

    @Override
    Series plus(String st) {
        return new TSSeries(this.column.map(n -> n + st))
    }

    @Override
    Series plus(Series series) {
        Column toAdd = series.implementation as Column
        return new TSSeries(this.column.append(toAdd))
    }

    @Override
    Long size() {
        return this.column.size()
    }

    @Override
    Criteria isGreaterThan(double value) {
        NumericColumn column = this.column as NumericColumn
        return new TSCriteria(column.isGreaterThan(value))
    }

    @Override
    Criteria isLessThan(double value) {
        NumericColumn column = this.column as NumericColumn
        return new TSCriteria(column.isLessThan(value))
    }

    @Override
    Series multiply(Number number) {
        NumericColumn column = this.column as NumericColumn
        return new TSSeries(column * number)
    }

    @Override
    List<Integer> toIntegerList() {
        NumericColumn column = this.column as NumericColumn
        return column.asIntColumn().toList()
    }

    @Override
    String toString() {
        return this.implementation.toString()
    }
}