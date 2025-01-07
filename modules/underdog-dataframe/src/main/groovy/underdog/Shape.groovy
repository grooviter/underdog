package underdog

import groovy.transform.Canonical
import org.codehaus.groovy.runtime.DefaultGroovyMethods

/**
 * Represents the shape of a matrix
 *
 * @since 0.1.0
 */
@Canonical
class Shape {
    int rows, cols

    /**
     * Allows a {@link Shape} to be destructured as:
     *
     * <code>
     * def (rows, cols) = shape
     * </code>
     *
     * @param rowOrCol 0 represents the index for rows and 1 for cols
     * @return the value of rows or cols depending on the index passed as param
     * @since 0.1.0
     */
    int getAt(Integer rowOrCol) {
        return [rows, cols].get(rowOrCol)
    }

    /**
     * Returns the shape of an empty matrix
     *
     * @return a {@link Shape} with 0 rows and 0 cols
     * @since 0.1.0
     */
    static Shape empty() {
        return new Shape(0, 0)
    }

    /**
     * Returns a list with two elements: [rows, cols]
     *
     * @return a {@link List} with the following form [rows, cols]
     * @since 0.1.0
     */
    List toList() {
        return [rows, cols]
    }

    @Override
    String toString() {
        return "$rows rows X $cols cols"
    }
}
