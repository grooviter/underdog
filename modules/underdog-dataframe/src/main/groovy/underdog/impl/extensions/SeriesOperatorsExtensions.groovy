package underdog.impl.extensions

import tech.tablesaw.api.DateColumn
import tech.tablesaw.api.NumericColumn
import tech.tablesaw.api.StringColumn
import tech.tablesaw.columns.Column
import tech.tablesaw.selection.BitmapBackedSelection
import tech.tablesaw.selection.Selection
import underdog.Criteria
import underdog.Series
import underdog.impl.TSCriteria
import underdog.impl.TSSeries

import java.time.LocalDate
import java.util.function.Predicate

/**
 * Groovy's symbol operators supported by a {@link Series} object
 *
 * @since 0.1.0
 */
class SeriesOperatorsExtensions {

    static Series plus(Series source, Series other) {
        return new TSSeries(getAsColumn(source).append(getAsColumn(other)))
    }

    static Series plus(Series source, Object other) {
        return new TSSeries(getAsColumn(source).map(n -> n + other))
    }

    static Series minus(Series source, Series other) {
        Column left = getAsColumn(source)
        Column right = getAsColumn(other)

        if (left instanceof NumericColumn && right instanceof NumericColumn) {
            return new TSSeries(left.subtract(right))
        }

        if (left instanceof DateColumn && right instanceof DateColumn) {
            return new TSSeries(left.daysUntil(right))
        }

        if (left instanceof StringColumn && right instanceof StringColumn) {
            int col1Size = left.size()
            for (int r = 0; r < col1Size; ++r) {
                left.set(r, left.getString(r) - right.getString(r))
            }
        }

        return new TSSeries(left)
    }

    static Series minus(Series source, Object other) {
        return new TSSeries(getAsColumn(source).map(val -> val - other))
    }

    static Series multiply(Series source, Series multiplier) {
        assert areNumericColumns(source, multiplier), "Can't multiply non numeric Series"
        return new TSSeries(getAsNumericColumn(source) * getAsNumericColumn(multiplier))
    }

    static Series multiply(Series source, Number multiplier) {
        def column = getAsColumn(source)

        if (column instanceof NumericColumn){
            return new TSSeries(column * multiplier)
        }

        if (column instanceof StringColumn) {
            return new TSSeries(column.map(st -> st * multiplier.toInteger()))
        }

        throw new RuntimeException("can't multiply ${multiplier} by a series of type ${column.type()}")
    }

    static Series div(Series dividend, Series divisor) {
        assert areNumericColumns(dividend, divisor), "Can't divide a non numeric series"
        return new TSSeries(getAsNumericColumn(dividend).divide(getAsNumericColumn(divisor)))
    }

    static Series div(Series dividend, Number divisor) {
        assert isNumericColumn(dividend), "Can't divide a non numeric Series by a number"
        return new TSSeries(getAsNumericColumn(dividend).divide(divisor))
    }

    /**
     * Finds all values of a {@link Series} greater than the value passed as parameter
     *
     * @param source
     * @param value all values should be greater than this value
     * @return a {@link Criteria instance}
     * @since 0.1.0
     */
    static Criteria isGreaterThan(Series source, Number number) {
        assert isNumericColumn(source), "Can't compare value ${number} against a non numeric series"
        return new TSCriteria(getAsNumericColumn(source).isGreaterThan(number.toDouble()))
    }

    /**
     * Finds all values of a {@link Series} greater than the value passed as parameter
     *
     * @param source
     * @param value all values should be greater than this value
     * @return a {@link Criteria instance}
     * @since 0.1.0
     */
    static Criteria isGreaterThan(Series source, LocalDate date) {
        assert isDateColumn(source), "Can't compare value ${date} against a non date series"
        return new TSCriteria(getAsDateColumn(source).isAfter(date))
    }

    /**
     * Finds all values of a {@link Series} less than the value passed as parameter
     *
     * @param source
     * @param value all values should be less than this value
     * @return a {@link Criteria instance}
     * @since 0.1.0
     */
    static Criteria isLessThan(Series source, Number number) {
        assert isNumericColumn(source), "Can't compare value ${number} against a non numeric series"
        return new TSCriteria(getAsNumericColumn(source).isLessThan(number.toDouble()))
    }

    /**
     * Finds all values of a {@link Series} less than the value passed as parameter
     *
     * @param source
     * @param value all values should be less than this value
     * @return a {@link Criteria instance}
     * @since 0.1.0
     */
    static Criteria isLessThan(Series source, LocalDate date) {
        assert isDateColumn(source), "Can't compare value ${date} against a non date series"
        return new TSCriteria(getAsDateColumn(source).isBefore(date))
    }

    static Criteria isEqualTo(Series source, Number number) {
        assert isNumericColumn(source), "Can't compare value ${number} against a non numeric series"
        return new TSCriteria(getAsNumericColumn(source).isEqualTo(number.toDouble()))
    }

    static Criteria isEqualTo(Series source, String value) {
        assert isStringColumn(source), "Can't compare a string ${value} against a non string series"
        return new TSCriteria(getAsStringColumn(source).isEqualTo(value))
    }

    static Criteria isNotEqualTo(Series source, String value) {
        assert isStringColumn(source), "Can't compare a string ${value} against a non string series"
        return new TSCriteria(getAsStringColumn(source).isNotEqualTo(value))
    }

    static Criteria isNotEqualTo(Series source, Number value) {
        assert isNumericColumn(source), "Can't compare a number ${value} against a non numeric series"
        return new TSCriteria(getAsNumericColumn(source).isNotEqualTo(value.toDouble()))
    }

    static Criteria inList(Series source, List options) {
        Selection selection = new BitmapBackedSelection();
        Predicate predicate = { it in options }

        for(int idx = 0; idx < source.size(); ++idx) {
            if (predicate.test(getAsColumn(source).get(idx))) {
                selection.add(new int[]{idx})
            }
        }

        return new TSCriteria(selection)
    }

    private static Column getAsColumn(Series series) {
        return series.implementation as Column
    }

    private static NumericColumn getAsNumericColumn(Series series) {
        return series.implementation as NumericColumn
    }

    private static DateColumn getAsDateColumn(Series series) {
        return series.implementation as DateColumn
    }

    private static StringColumn getAsStringColumn(Series series) {
        return series.implementation as StringColumn
    }

    private static boolean isNumericColumn(Series source){
        return source.implementation instanceof NumericColumn
    }

    private static boolean isStringColumn(Series source) {
        return source.implementation instanceof StringColumn
    }

    private static boolean isDateColumn(Series source) {
        return source.implementation instanceof DateColumn
    }

    private static boolean areNumericColumns(Series left, Series right) {
        return [left, right].every { it.implementation instanceof NumericColumn }
    }
}
