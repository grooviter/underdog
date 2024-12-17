package underdog

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam

import java.time.LocalDate
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
     * Fill NA/NaN values using the specified value
     *
     * @param value the value to replace the NA/Nan values with
     * @return the series instance with replaced values
     * @since 0.1.0
     */
    Series fillna(Object value)

    /**
     * @param index
     * @return
     * @since 0.1.0
     */
    Series lag(int index)

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

    <U> List<U> toList()
}