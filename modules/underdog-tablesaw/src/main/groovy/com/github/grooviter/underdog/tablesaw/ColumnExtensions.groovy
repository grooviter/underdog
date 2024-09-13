package com.github.grooviter.underdog.tablesaw

import org.codehaus.groovy.runtime.DefaultGroovyMethods
import tech.tablesaw.api.DateColumn
import tech.tablesaw.api.DateTimeColumn
import tech.tablesaw.api.DoubleColumn
import tech.tablesaw.api.IntColumn
import tech.tablesaw.api.NumberColumn
import tech.tablesaw.api.NumericColumn
import tech.tablesaw.api.StringColumn
import tech.tablesaw.api.TextColumn
import tech.tablesaw.columns.Column
import tech.tablesaw.selection.Selection

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.function.Function

@SuppressWarnings('unused')
class ColumnExtensions {

    private static final String TEMPORAL_COLUMN_NAME = 'tmp'

    static <T> Column<T> minus(Column<T> source, Number number) {
        return source.map { it - number } as Column<T>
    }

    static <T> Column<T> plus(Column<T> source, Number number) {
        return source.map { it + number } as Column<T>
    }

    static DoubleColumn plus(DoubleColumn source, DoubleColumn target) {
        return DoubleColumn.create(TEMPORAL_COLUMN_NAME, sumLists(source, target) as double[])
    }

    static IntColumn plus(IntColumn source, IntColumn target) {
        return IntColumn.create(TEMPORAL_COLUMN_NAME, sumLists(source, target) as int[])
    }

    static DoubleColumn plus(NumericColumn source, NumericColumn target) {
        return DoubleColumn.create(TEMPORAL_COLUMN_NAME, sumLists(source, target) as double[])
    }

    static StringColumn plus(StringColumn source, Column target) {
        return StringColumn.create(TEMPORAL_COLUMN_NAME, sumLists(source, target) as String[])
    }

    static Selection matches(StringColumn source, String regex) {
        return source.matchesRegex(regex)
    }

    static Object asType(Column source, Class clazz) {
        switch (clazz) {
            case int[]:
            case double[]:
            case float[]:
            case String[]:
                return source.toList().asType(clazz)
            default:
                return DefaultGroovyMethods.asType(source, clazz)
        }
    }

    private static List sumLists(Column left, Column right) {
        return [left.toList(), right.toList()].transpose().collect { a , b -> a + b }
    }

    static <T> Column<T> div(Column<T> source, Number number) {
        return source.map { it / number } as Column<T>
    }

    static <T> Column<T> mod(Column<T> source, Number number) {
        return source.map { it % number } as Column<T>
    }

    static <T> Column<T> where(Column<T> source, Closure<Selection> fn) {
        return source.where(fn(source) as Selection)
    }

    static <U> IntColumn mapToInt(Column<U> column, Function<U, Integer> function) {
        return column.mapInto(function, IntColumn.create(TEMPORAL_COLUMN_NAME, column.size()))
    }

    static <U> DoubleColumn mapToDouble(Column<U> column, Function<U, Double> function) {
        return column.mapInto(function, DoubleColumn.create(TEMPORAL_COLUMN_NAME, column.size()))
    }

    static <U> TextColumn mapToText(Column<U> column, Function<U, String> function) {
        return column.mapInto(function, TextColumn.create(TEMPORAL_COLUMN_NAME, column.size()))
    }

    static <U> DateColumn mapToDate(Column<U> column, Function<U, LocalDate> function) {
        return column.mapInto(function, DateColumn.create(TEMPORAL_COLUMN_NAME, column.size()))
    }

    static <U> DateTimeColumn mapToDateTime(Column<U> column, Function<U, LocalDateTime> function) {
        return column.mapInto(function, DateTimeColumn.create(TEMPORAL_COLUMN_NAME, column.size()))
    }

    static Column minus(Column source, Column other) {
        if (source instanceof NumberColumn && other instanceof NumberColumn){
            return source.subtract(other)
        }
        throw new RuntimeException("can't subtract NON number columns")
    }
}
