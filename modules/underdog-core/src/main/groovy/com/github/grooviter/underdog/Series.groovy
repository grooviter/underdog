package com.github.grooviter.underdog

import groovy.transform.NamedVariant
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam

/**
 * @since 0.1.0
 */
interface Series extends Columnar {

    <P> Series call(Class<P> clazz, @ClosureParams(value = FirstParam.FirstGenericType, options='P') Closure func)

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
    Criteria isGreaterThan(double value)

    /**
     * @since 0.1.0
     */
    Criteria isLessThan(double value)

    /**
     * @since 0.1.0
     */
    Criteria isEqualTo(double value)

    /**
     * @since 0.1.0
     */
    Criteria isEqualTo(String value)

    /**
     * @since 0.1.0
     */
    Series multiply(Number number)

    /**
     * @since 0.1.0
     */
    List<Integer> toIntegerList()

    /**
     * @since 0.1.0
     */
    DataFrame describe()
}