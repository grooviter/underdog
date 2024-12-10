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
class SeriesOperatorsExtensions extends SeriesExtensionsBase {

    /**
     * Appends to an existent {@link Series} the {@link Series} passed as parameter
     *
     * @param source {@link Series} where series passed as parameter is going to be added to
     * @param other the {@link Series} to append
     * @return
     * @since 0.1.0
     */
    static Series plus(Series source, Series other) {
        return new TSSeries(getAsColumn(source).append(getAsColumn(other)))
    }

    /**
     * Appends an object to an existent {@link Series}
     *
     * @param source the {@link Series} where the object is going t obe added to
     * @param other the object to add
     * @return
     * @since 0.1.0
     */
    static Series plus(Series source, Object other) {
        return new TSSeries(getAsColumn(source).map(n -> n + other))
    }

    /**
     * Subtracts to an existent {@link Series} the {@link Series} passed as parameter
     *
     * @param source the {@link Series} to subtract from
     * @param other the series to subtract
     * @return the operation result
     * @since 0.1.0
     */
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

    /**
     * Subtracts an object from an existent {@link Series}
     *
     * @param source the {@link Series} to subtract from
     * @param other the object to subtract
     * @return the operation result
     * @since 0.1.0
     */
    static Series minus(Series source, Object other) {
        return new TSSeries(getAsColumn(source).map(val -> val - other))
    }

    /**
     * Multiplies row by row. It requires to have same number of rows
     *
     * @param source {@link Series} to multiply
     * @param multiplier {@link Series} multiplier
     * @return the product of both series row by row
     * @since 0.1.0
     */
    static Series multiply(Series source, Series multiplier) {
        assert areNumericColumns(source, multiplier), "Can't multiply non numeric Series"
        return new TSSeries(getAsNumericColumn(source) * getAsNumericColumn(multiplier))
    }

    /**
     * Multiplies every row of the series by the multiplier
     *
     * @param source the {@link Series} to multiply
     * @param multiplier the number multiplier
     * @return the product result
     * @since 0.1.0
     */
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

    /**
     * 
     * @param dividend
     * @param divisor
     * @return
     * @since 0.1.0
     */
    static Series div(Series dividend, Series divisor) {
        assert areNumericColumns(dividend, divisor), "Can't divide a non numeric series"
        return new TSSeries(getAsNumericColumn(dividend).divide(getAsNumericColumn(divisor)))
    }

    /**
     * @param dividend
     * @param divisor
     * @return
     * @since 0.1.0
     */
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
     * @param source
     * @param number
     * @return
     * @since 0.1.0
     */
    static Criteria isGreaterThanOrEqualTo(Series source, Number number) {
        assert isNumericColumn(source), "Can't compare value ${number} against a non numeric series"
        return new TSCriteria(getAsNumericColumn(source).isGreaterThanOrEqualTo(number.toDouble()))
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
     * @param source
     * @param date
     * @return
     * @since 0.1.0
     */
    static Criteria isGreaterThanOrEqualTo(Series source, LocalDate date) {
        assert isDateColumn(source), "Can't compare value ${date} against a non date series"
        return new TSCriteria(getAsDateColumn(source).isOnOrAfter(date))
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
     * @param source
     * @param number
     * @return
     * @since 0.1.0
     */
    static Criteria isLessThanOrEqualTo(Series source, Number number) {
        assert isNumericColumn(source), "Can't compare value ${number} against a non numeric series"
        return new TSCriteria(getAsNumericColumn(source).isLessThanOrEqualTo(number.toDouble()))
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

    /**
     * @param source
     * @param date
     * @return
     * @since 0.1.0
     */
    static Criteria isLessThanOrEqualTo(Series source, LocalDate date) {
        assert isDateColumn(source), "Can't compare value ${date} against a non date series"
        return new TSCriteria(getAsDateColumn(source).isOnOrBefore(date))
    }

    /**
     * @param source
     * @param number
     * @return
     * @since 0.1.0
     */
    static Criteria isEqualTo(Series source, Number number) {
        assert isNumericColumn(source), "Can't compare value ${number} against a non numeric series"
        return new TSCriteria(getAsNumericColumn(source).isEqualTo(number.toDouble()))
    }

    /**
     * @param source
     * @param value
     * @return
     * @since 0.1.0
     */
    static Criteria isEqualTo(Series source, String value) {
        assert isStringColumn(source), "Can't compare a string ${value} against a non string series"
        return new TSCriteria(getAsStringColumn(source).isEqualTo(value))
    }

    /**
     * @param source
     * @param value
     * @return
     * @since 0.1.0
     */
    static Criteria isNotEqualTo(Series source, String value) {
        assert isStringColumn(source), "Can't compare a string ${value} against a non string series"
        return new TSCriteria(getAsStringColumn(source).isNotEqualTo(value))
    }

    /**
     * @param source
     * @param value
     * @return
     * @since 0.1.0
     */
    static Criteria isNotEqualTo(Series source, Number value) {
        assert isNumericColumn(source), "Can't compare a number ${value} against a non numeric series"
        return new TSCriteria(getAsNumericColumn(source).isNotEqualTo(value.toDouble()))
    }

    /**
     * @param source
     * @param value
     * @return
     * @since 0.1.0
     */
    static Criteria isNotEqualTo(Series source, LocalDate date) {
        assert isDateColumn(source), "Can't compare value ${date} against a non date series"
        return new TSCriteria(getAsDateColumn(source).isNotEqualTo(date))
    }

    /**
     * @param source
     * @param options
     * @return
     * @since 0.1.0
     */
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

    /**
     * @param source
     * @param regex
     * @return
     * @since 0.1.0
     */
    static Criteria matches(Series source, String regex) {
        return new TSCriteria(getAsStringColumn(source).matchesRegex(regex))
    }
}
