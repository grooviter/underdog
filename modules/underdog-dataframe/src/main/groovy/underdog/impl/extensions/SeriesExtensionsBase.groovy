package underdog.impl.extensions

import tech.tablesaw.api.DateColumn
import tech.tablesaw.api.NumericColumn
import tech.tablesaw.api.StringColumn
import tech.tablesaw.columns.Column
import underdog.Series

class SeriesExtensionsBase {
    protected static Column getAsColumn(Series series) {
        return series.implementation as Column
    }

    protected static NumericColumn getAsNumericColumn(Series series) {
        return series.implementation as NumericColumn
    }

    protected static DateColumn getAsDateColumn(Series series) {
        return series.implementation as DateColumn
    }

    protected static StringColumn getAsStringColumn(Series series) {
        return series.implementation as StringColumn
    }

    protected static boolean isNumericColumn(Series source){
        return source.implementation instanceof NumericColumn
    }

    protected static boolean isStringColumn(Series source) {
        return source.implementation instanceof StringColumn
    }

    protected static boolean isDateColumn(Series source) {
        return source.implementation instanceof DateColumn
    }

    protected static boolean areNumericColumns(Series left, Series right) {
        return [left, right].every { it.implementation instanceof NumericColumn }
    }
}
