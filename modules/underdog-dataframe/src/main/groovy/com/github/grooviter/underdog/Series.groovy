package com.github.grooviter.underdog

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam

import java.util.function.Function

/**
 * @since 0.1.0
 */
interface Series extends Columnar {

    static enum TypeCorrelation {
        PEARSON, KENDALL, SPEARMAN
    }

    <P> Series call(Class<P> clazz, @ClosureParams(value = FirstParam.FirstGenericType, options='P') Closure func)

    <P,O> Series call(Class<P> clazz, Class<O> output, @ClosureParams(value = FirstParam.FirstGenericType, options='P') Closure func)

    <P, O> Series call(Class<P> clazz, Class<O> output, Function<P, O> converter)

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
     * @since 0.1.0
     */
    float corr(Series other)

    /**
     * @since 0.1.0
     */
    Series dropna()


    Criteria inList(List options)


    Series lag(int index)

    /**
     * @since 0.1.0
     */
    Series plus(Number o)

    /**
     * @since 0.1.0
     */
    Series plus(String st)

    /**
     * @since 0.1.0
     */
    Series plus(Series series)

    /**
     * @since 0.1.0
     */
    Object getImplementation()

    /**
     * @since 0.1.0
     */
    Object getAt(Integer index)

    /**
     * @since 0.1.0
     */
    Series getAt(IntRange indexRange)

    /**
     * @since 0.1.0
     */
    Series getIloc()

    /**
     * @since 0.1.0
     */
    Long size()

    /**
     * @since 0.1.0
     */
    Series unique()

    /**
     * @since 0.1.0
     */
    @NamedVariant
    Double mean(boolean skipNa, int precision)

    /**
     * @since 0.1.0
     */
    Double mean()

    /**
     * @since 0.1.0
     */
    Series div(Series series)

    /**
     * @since 0.1.0
     */
    Criteria isGreaterThan(Number value)

    /**
     * @since 0.1.0
     */
    Criteria isLessThan(Number value)

    /**
     * @since 0.1.0
     */
    Criteria isEqualTo(Number value)

    /**
     * @since 0.1.0
     */
    Criteria isEqualTo(String value)

    /**
     * @since 0.1.0
     */
    Criteria isNotEqualTo(String value)

    /**
     * @since 0.1.0
     */
    Criteria isNotEqualTo(Number value)

    /**
     * @since 0.1.0
     */
    Series multiply(Number number)

    /**
     * @since 0.1.0
     */
    Series minus(Object value)

    /**
     * @since 0.1.0
     */
    Series minus(Series series)

    /**
     * @since 0.1.0
     */
    @NamedVariant
    Series toNumeric(String errors)

    /**
     * @since 0.1.0
     */
    DataFrame describe()

    String[] toStringArray()

    @NamedVariant
    Series sort(@NamedParam(required = false) boolean descending)

    Series sort()
}