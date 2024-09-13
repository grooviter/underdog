package com.github.grooviter.underdog


import groovy.transform.NamedParam
import groovy.transform.NamedVariant

import java.util.function.Function

interface DataFrame extends Columnar {

    /**
     * The transpose of the DataFrame.
     * */
    DataFrame getT()

    Object getImplementation()

    DataFrameLoc getLoc()

    DataFrameIloc getIloc()


    /**
     * The column labels of the DataFrame.
     * */
    List<String> getColumns()

    /**
     * Indicator whether Series/DataFrame is empty.
     * */
    Boolean isEmpty()

    /**
     * Return an int representing the number of elements in this object.
     * */
    Long getSize()

    /**
     * Return a Series/DataFrame with absolute numeric value of each element.
     **/
    DataFrame abs()

    /**
     * Get Addition of dataframe and other, element-wise (binary operator add).
     **/
    @NamedVariant
    DataFrame add(DataFrame other, TypeAxis axis, Integer level, BigDecimal fill)

    /**
     * Get Addition of dataframe and other, element-wise (binary operator add).
     **/
    DataFrame plus(Number number)

    /**
     * Prefix labels with string prefix.
     * */
    @NamedVariant
    DataFrame addPrefix(String prefix, TypeAxis axis)

    /**
     * Suffix labels with string prefix.
     * */
    @NamedVariant
    DataFrame addSuffix(@NamedParam(required = true) String suffix, @NamedParam(required = false) TypeAxis axis)

    /**
     * Aggregate using one or more operations over the specified axis.
     * */
    @NamedVariant
    DataFrame agg(Function fn, String fnName, List<String> namedFns, Map colFunc, TypeAxis axis)

    /**
     * Aggregate using one or more operations over the specified axis.
     * */
    DataFrame agg(String fnName, TypeAxis axis)

    /**
     * Aggregate using one or more operations over the specified axis.
     * */
    DataFrame agg(List<String> namedFns, TypeAxis axis)

    /**
     * Aggregate using one or more operations over the specified axis.
     * */
    DataFrame agg(Map colFunc, TypeAxis axis)

    /**
     * Align two objects on their axes with the specified join method.
     * */
    @NamedVariant
    <T extends Columnar> Tuple2<DataFrame, T> align(
        @NamedParam(required = true) T other,
        @NamedParam(required = false) TypeJoin joinType,
        @NamedParam(required = false) TypeAxis axis,
        @NamedParam(required = false) int level,
        @NamedParam(required = false) Boolean copy)

    /**
     * Return whether all elements are True, potentially over an axis.
     *
     * Returns True unless there at least one element within a series or along a Dataframe
     * axis that is False or equivalent (e.g. zero or empty).
     * */
    @NamedVariant
    boolean all(TypeAxis axisType, Boolean boolOnly, Boolean skipNa)

    /**
     * Return whether any element is True, potentially over an axis.
     *
     * Returns False unless there is at least one element within a series or along a Dataframe
     * axis that is True or equivalent (e.g. non-zero or non-empty).
     */
    @NamedVariant
    boolean any(TypeAxis axisType, Boolean boolOnly, Boolean skipNa)

    /**
     * Apply a function along an axis of the DataFrame.
     *
     * Objects passed to the function are Series objects whose index is either the DataFrame’s index (axis=0)
     * or the DataFrame’s columns (axis=1). By default (result_type=None), the final return type is inferred
     * from the return type of the applied function. Otherwise, it depends on the result_type argument.
     *
     * */
    @NamedVariant
    DataFrame apply(Function fn, TypeAxis axisType, boolean raw, TypeApplyResult resultType, TypeApplyByRow byRow)

    /**
     * Apply a function along an axis of the DataFrame.
     *
     * Objects passed to the function are Series objects whose index is either the DataFrame’s index (axis=0)
     * or the DataFrame’s columns (axis=1). By default (result_type=None), the final return type is inferred
     * from the return type of the applied function. Otherwise, it depends on the result_type argument.
     *
     * */
    DataFrame apply(Closure fn)

    /**
     * Convert time series to specified frequency.
     *
     * Returns the original data conformed to a new index with the specified frequency.
     * */
    @NamedVariant
    DataFrame asfreq(String freqExpression, boolean normalize)

    /**
     * Drop specified labels from rows or columns.
     * Remove rows or columns by specifying label names and corresponding axis, or by directly specifying
     * index or column names. When using a multi-index, labels on different levels can be removed by specifying
     * the level.
     * */
    @NamedVariant
    DataFrame drop(List<String> labels, String levelName, String levelIndex, TypeAxis axisType)

    /**
     * Drop specified labels from rows or columns.
     * Remove rows or columns by specifying label names and corresponding axis, or by directly specifying
     * index or column names. When using a multi-index, labels on different levels can be removed by specifying
     * the level.
     * */
    DataFrame drop(String... labels)

    @NamedVariant
    DataFrame merge(
        DataFrame right,
        @NamedParam TypeJoin how,
        @NamedParam List<String> on,
        @NamedParam List<String> left_on,
        @NamedParam List<String> right_on,
        @NamedParam boolean left_index,
        @NamedParam boolean right_index,
        @NamedParam boolean sort,
        @NamedParam List<String> suffixes,
        @NamedParam boolean copy
    )

    @NamedVariant
    Series min(TypeAxis axisType)

    @NamedVariant
    Series max(TypeAxis axisType)

    /**
     * Rename columns or index labels.
     * Function / dict values must be unique (1-to-1). Labels not contained in a dict / Series will be left as-is.
     * Extra labels listed don’t throw an error.
     * */
    @NamedVariant
    DataFrame rename(Map<String, String> mapper, Function<String, String> fn, List<String> columns, boolean copy)

    @Override
    default String toString() {
        return this.implementation.toString()
    }
}