package underdog

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam

import java.util.function.Function

/**
 * @since 0.1.0
 */
interface Series extends Columnar, Iterable {

    /**
     * @since 0.1.0
     */
    static enum TypeCorrelation {
        PEARSON, KENDALL, SPEARMAN
    }

    /**
     * @param clazz
     * @param func
     * @return
     * @since 0.1.0
     */
    <P> Series call(Class<P> clazz, @ClosureParams(value = FirstParam.FirstGenericType, options='P') Closure func)

    /**
     * @param clazz
     * @param output
     * @param func
     * @return
     * @since 0.1.0
     */
    <P,O> Series call(Class<P> clazz, Class<O> output, @ClosureParams(value = FirstParam.FirstGenericType, options='P') Closure func)

    /**
     * @param clazz
     * @param output
     * @param converter
     * @return
     * @since 0.1.0
     */
    <P, O> Series call(Class<P> clazz, Class<O> output, Function<P, O> converter)

    /**
     * @since 0.1.0
     */
    Series categorize()

    /**
     * Compute correlation with other Series, excluding missing values.
     * The two Series objects are not required to be the same length and will be aligned internally before
     * the correlation function is applied.
     *
     * @param other
     * @param method
     * @param observations
     * @since 0.1.0
     */
    @NamedVariant
    float corr(
        @NamedParam(required = true) Series other,
        @NamedParam(required = false) TypeCorrelation method,
        @NamedParam(required = false) Integer observations)

    /**
     * Compute correlation with other Series, excluding missing values.
     * The two Series objects are not required to be the same length and will be aligned internally before
     * the correlation function is applied.
     *
     * @param other
     * @return
     * @since 0.1.0
     */
    float corr(Series other)

    /**
     * @return
     * @since 0.1.0
     */
    Series dropna()

    /**
     * @return
     * @since 0.1.0
     */
    Criteria inList(List options)

    /**
     * @param index
     * @return
     * @since 0.1.0
     */
    Series lag(int index)

    /**
     * @param o
     * @return
     * @since 0.1.0
     */
    Series plus(Number o)

    /**
     * @param st
     * @return
     * @since 0.1.0
     */
    Series plus(String st)

    /**
     * @param series
     * @return
     * @since 0.1.0
     */
    Series plus(Series series)

    /**
     * @return
     * @since 0.1.0
     */
    Object getImplementation()

    /**
     * @param index
     * @return
     * @since 0.1.0
     */
    Object getAt(Integer index)

    /**
     * @return
     * @since 0.1.0
     */
    String getName()

    /**
     * @param newName
     * @return
     * @since 0.1.0
     */
    Series rename(String newName)

    /**
     * @param indexRange
     * @return
     * @since 0.1.0
     */
    Series getAt(IntRange indexRange)

    /**
     * @return
     * @since 0.1.0
     */
    Series getIloc()

    /**
     * @return
     * @since 0.1.0
     */
    Long size()

    /**
     * @return
     * @since 0.1.0
     */
    Series unique()

    /**
     * @param skipNa
     * @param precision
     * @return
     * @since 0.1.0
     */
    @NamedVariant
    Double mean(boolean skipNa, int precision)

    /**
     * @return
     * @since 0.1.0
     */
    Double mean()

    /**
     * @param series
     * @return
     * @since 0.1.0
     */
    Series div(Series series)

    /**
     * @param value
     * @return
     * @since 0.1.0
     */
    Criteria isGreaterThan(Number value)

    /**
     * @param value
     * @return
     * @since 0.1.0
     */
    Criteria isLessThan(Number value)

    /**
     * @param value
     * @return
     * @since 0.1.0
     */
    Criteria isEqualTo(Number value)

    /**
     * @param value
     * @return
     * @since 0.1.0
     */
    Criteria isEqualTo(String value)

    /**
     * @param value
     * @return
     * @since 0.1.0
     */
    Criteria isNotEqualTo(String value)

    /**
     * @param value
     * @return
     * @since 0.1.0
     */
    Criteria isNotEqualTo(Number value)

    /**
     * @param number
     * @return
     * @since 0.1.0
     */
    Series multiply(Number number)

    /**
     * @param value
     * @return
     * @since 0.1.0
     */
    Series minus(Object value)

    /**
     * @param series
     * @return
     * @since 0.1.0
     */
    Series minus(Series series)

    /**
     * @param errors
     * @return
     * @since 0.1.0
     */
    @NamedVariant
    Series toNumeric(String errors)

    /**
     * @return
     * @since 0.1.0
     */
    DataFrame describe()

    /**
     * @return
     * @since 0.1.0
     */
    String[] toStringArray()

    /**
     * @param descending
     * @return
     * @since 0.1.0
     */
    @NamedVariant
    Series sort(@NamedParam(required = false) boolean descending)

    /**
     * @return
     * @since 0.1.0
     */
    Series sort()
}