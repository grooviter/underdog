package com.github.grooviter.underdog.impl.extensions

import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.impl.TSDataFrame
import tech.tablesaw.api.Table

/**
 * @since 0.1.0
 */
class CollectionsExtensions {

    /**
     *
     * @param list
     * @param dataFrameName
     * @since 0.1.0
     */
    static DataFrame toDF(Collection<Map<String,?>> collection, String dataFrameName) {
        return toDataFrame(collection, dataFrameName)
    }

    /**
     *
     * <code>
     *     def df = [
     *        ["ID": "1", CARBS: 2.0],
     *        ["ID": "2", CARBS: 3.0],
     *        ["ID": "3", CARBS: 4.0],
     *     ].toDF("dataFrameName")
     * </code>
     *
     * @param list
     * @param dataFrameName
     * @since 0.1.0
     */
    static DataFrame toDataFrame(Collection<Map<String,?>> collection, String dataFrameName) {
        return TSDataFrame.from(dataFrameName, collection)
    }

    /**
     *
     * @param map
     * @param dataFrameName
     * @since 0.1.0
     */
    static DataFrame toDF(Map<String,List<?>> map, String dataFrameName) {
        return toDataFrame(map, dataFrameName)
    }

    /**
     *
     * <code>
     *     def df = [
     *         ID: ["1", "2", "3"],
     *         CARBS: [2.0, 3.0, 4.0]
     *     ].toDF("dataFrameName")
     * </code>
     *
     * @param map
     * @param dataFrameName
     * @since 0.1.0
     */
    static DataFrame toDataFrame(Map<String,List<?>> map, String dataFrameName) {
        return TSDataFrame.from(dataFrameName, map)
    }
}
