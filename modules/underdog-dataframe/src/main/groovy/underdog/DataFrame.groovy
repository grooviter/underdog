package underdog

import underdog.impl.ast.ASTDriven
import groovy.transform.NamedParam
import groovy.transform.NamedVariant
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString

import java.util.function.Function

/**
 * @since 0.1.0
 */
interface DataFrame extends Columnar {
    /**
     * The transpose of the DataFrame.
     *
     * @return
     * @since 0.1.0
     */
    DataFrame getT()

    /**
     * @return
     * @since 0.1.0
     */
    Object getImplementation()

    /**
     * @return
     * @since 0.1.0
     */
    DataFrameLoc getLoc()

    /**
     * @return
     * @since 0.1.0
     */
    DataFrameIloc getIloc()

    /**
     * The column labels of the DataFrame.
     *
     * @return
     * @since 0.1.0
     */
    List<String> getColumns()

    /**
     * @return
     * @since 0.1.0
     */
    DataFrame head()

    /**
     *
     * @param rows
     * @since 0.1.0
     */
    DataFrame head(int rows)

    /**
     *
     * @return
     * @since 0.1.0
     */
    DataFrame describe()

    /**
     * Indicator whether Series/DataFrame is empty.
     *
     * @return
     * @since 0.1.0
     */
    Boolean isEmpty()

    /**
     * Return an int representing the number of elements in this object.
     *
     * @return
     * @since 0.1.0
     */
    Long size()

    /**
     * @since 0.1.0
     */
    DataFrame first()

    /**
     * @since 0.1.0
     */
    DataFrame first(int rows)

    /**
     * @since 0.1.0
     */
    DataFrame last()

    /**
     * @since 0.1.0
     */
    DataFrame last(int rows)

    /**
     * @since 0.1.0
     */
    Series getAt(String column)

    /**
     * @since 0.1.0
     */
    DataFrame getAt(String[] columns)

    /**
     * @since 0.1.0
     */
    DataFrame getAt(List<String> columns)

    /**
     * @since 0.1.0
     */
    default DataFrame getAt(Criteria criteria) {
        return criteria.apply(this)
    }

    /**
     * @since 0.1.0
     */
    @ASTDriven
    default DataFrame getAt(Boolean selection) {
        throw new RuntimeException("")
    }

    /**
     * Return a Series/DataFrame with absolute numeric value of each element.
     *
     * @since 0.1.0
     */
    DataFrame abs()

    /**
     * Get Addition of dataframe and other, element-wise (binary operator add).
     *
     * @since 0.1.0
     */
    @NamedVariant
    DataFrame add(
            DataFrame other,
            @NamedParam(required = false) boolean inPlace,
            @NamedParam(required = false) TypeAxis axis,
            @NamedParam(required = false) Object fill,
            @NamedParam(required = false) String index)

    /*
     *
     * @since 0.1.0
     */
    @NamedVariant
    DataFrame pivot(String x, String y, String value, String fnName)

    /**
     * Get Addition of dataframe and other, element-wise (binary operator add).
     *
     * @since 0.1.0
     */
    DataFrame plus(Number number)

    /**
     * Prefix labels with string prefix.
     *
     * @since 0.1.0
     */
    @NamedVariant
    DataFrame addPrefix(String prefix, TypeAxis axis)

    /**
     * Suffix labels with string prefix.
     *
     * @since 0.1.0
     */
    @NamedVariant
    DataFrame addSuffix(@NamedParam(required = true) String suffix, @NamedParam(required = false) TypeAxis axis)

    /**
     * Aggregate using one or more operations over the specified axis.
     *
     * @since 0.1.0
     */
    @NamedVariant
    DataFrame agg(Function fn, String fnName, List<String> namedFns, Map colFunc, TypeAxis axis)

    /**
     * Aggregate using one or more operations over the specified axis.
     *
     * @since 0.1.0
     */
    DataFrame agg(String fnName, TypeAxis axis)

    /**
     * Aggregate using one or more operations over the specified axis.
     *
     * @since 0.1.0
     */
    DataFrame agg(List<String> namedFns, TypeAxis axis)

    /**
     * Aggregate using one or more operations over the specified axis.
     * */
    DataFrame agg(Map colFunc, TypeAxis axis)

    /**
     * Align two objects on their axes with the specified join method.
     *
     * @since 0.1.0
     */
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
     *
     * @since 0.1.0
     */
    @NamedVariant
    boolean all(TypeAxis axisType, Boolean boolOnly, Boolean skipNa)

    /**
     * Return whether any element is True, potentially over an axis.
     *
     * Returns False unless there is at least one element within a series or along a Dataframe
     * axis that is True or equivalent (e.g. non-zero or non-empty).
     *
     * @since 0.1.0
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
     * @since 0.1.0
     */
    @NamedVariant
    DataFrame apply(Function fn, TypeAxis axisType, boolean raw, TypeApplyResult resultType, TypeApplyByRow byRow)

    /**
     * Apply a function along an axis of the DataFrame.
     *
     * Objects passed to the function are Series objects whose index is either the DataFrame’s index (axis=0)
     * or the DataFrame’s columns (axis=1). By default (result_type=None), the final return type is inferred
     * from the return type of the applied function. Otherwise, it depends on the result_type argument.
     *
     * @since 0.1.0
     */
    DataFrame apply(Closure fn)

    /**
     * Convert time series to specified frequency.
     *
     * Returns the original data conformed to a new index with the specified frequency.
     *
     * @since 0.1.0
     */
    @NamedVariant
    DataFrame asfreq(String freqExpression, boolean normalize)

    /**
     * Drop specified labels from rows or columns.
     * Remove rows or columns by specifying label names and corresponding axis, or by directly specifying
     * index or column names. When using a multi-index, labels on different levels can be removed by specifying
     * the level.
     *
     * @since 0.1.0
     */
    @NamedVariant
    DataFrame drop(List<String> labels, String levelName, String levelIndex, TypeAxis axisType)

    /**
     * Drop specified labels from rows or columns.
     * Remove rows or columns by specifying label names and corresponding axis, or by directly specifying
     * index or column names. When using a multi-index, labels on different levels can be removed by specifying
     * the level.
     *
     * @since 0.1.0
     */
    DataFrame drop(String... labels)

    /**
     * @since 0.1.0
     */
    @NamedVariant
    DataFrame dropna(@NamedParam(required = false) String by, @NamedParam(required = false) List<String> byColumns)

    /**
     * @since 0.1.0
     */
    DataFrame dropna()

    /**
     * @since 0.1.0
     */
    String getName()

    /**
     * @since 0.1.0
     */
    @NamedVariant
    DataFrame mean(
        @NamedParam(required = true) TypeAxis axis,
        @NamedParam(required = true) String index,
        @NamedParam(required = false) List<String> cols)

    /**
     * @since 0.1.0
     */
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
        @NamedParam boolean allowDuplicateColumns,
        @NamedParam boolean copy
    )

    /**
     * @since 0.1.0
     */
    @NamedVariant
    Series min(TypeAxis axisType)

    /**
     * @since 0.1.0
     */
    DataFrame minus(Series series)

    /**
     * @since 0.1.0
     */
    @NamedVariant
    Series max(TypeAxis axisType)

    /**
     *
     *
     * @since 0.1.0
     */
    DataFrame nlargest(Integer n)

    /**
     *
     *
     * @since 0.1.0
     */
    @NamedVariant
    DataFrame nlargest(Integer n, @NamedParam(required = false) List<String> columns)

    /**
     * Rename columns or index labels.
     * Function / dict values must be unique (1-to-1). Labels not contained in a dict / Series will be left as-is.
     * Extra labels listed don’t throw an error.
     *
     * @since 0.1.0
     */
    @NamedVariant
    DataFrame rename(Map<String, String> mapper, Function<String, String> fn, List<String> columns, boolean copy)

    /**
     * @since 0.1.0
     */
    DataFrame rename(@ClosureParams(value = FromString, options = ['java.lang.Integer,java.lang.String']) Closure<String> function)

    /**
     * @since 0.1.0
     */
    @NamedVariant
    DataFrame sort_values(@NamedParam(required = false) boolean skipNa, @NamedParam(required = true) Object by)

    /**
     * @since 0.1.0
     */
    @Override
    default String toString() {
        return this.implementation.toString()
    }

    /**
     * @param colName
     * @param value
     * @since 0.1.0
     */
    void putAt(String colName, Series value)

    /**
     * @param colName
     * @param values
     * @since 0.1.0
     */
    void putAt(String colName, List values)

    /**
     * @return
     * @since 0.1.0
     */
    Shape shape()

    /**
     * @param aggFn
     * @return
     * @since 0.1.0
     */
    DataFrameAggregation agg(Map<String, ?> aggFn)

    /**
     * @param name
     * @return
     * @since 0.1.0
     */
    DataFrame setName(String name)

    /**
     * @return
     * @since 0.1.0
     */
    DataFrame schema()

    /**
     * Converts the current {@link DataFrame} in a {@link List} of {@link List}
     *
     * @return an instance of a {@link List}
     * @type <U> any type you want to cast to under your own risk
     * @since 0.1.0
     */
    <U> List<U> toList()

    /**
     * @param labels
     * @param values
     * @return
     * @since 0.1.0
     */
    @NamedVariant
    DataFrame xTabCounts(String labels, String values)
}