package com.github.grooviter.underdog

/**
 * @since 0.1.0
 */
interface DataFrameAggregation {
    /**
     * @since 0.1.0
     */
    static record SeriesInfo (String columnName, List<String> functions) {}

    /**
     * @since 0.1.0
     */
    static record AggregationInfo(List<SeriesInfo> infoByColumn) {}

    /**
     * @since 0.1.0
     */
    DataFrame by(String... columns)

    /**
     * @since 0.1.0
     */
    AggregationInfo getAggregationInfo()
}
