package com.github.grooviter.underdog.impl.extensions

import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.impl.TSDataFrame

/**
 * Utils methods when working with arrays and collections
 *
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

    /**
     * Returns the shape of a given double[][] array. If array is empty or null returns [0, 0] the rest of the
     * time it returns the length of the array (length) and the length of the first array (width).
     *
     * @param data the array the get the shape from
     * @return a list of type [length, width] of the array
     * @since 0.1.0
     */
    static List<Integer> shape(double[][] data) {
        if (!data) {
            return [0, 0]
        }
        def firstNestedArray = (data.find() ?: []) as double[]
        return [data.length, firstNestedArray.length]
    }
}