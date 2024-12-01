package underdog.impl

import tech.tablesaw.api.ColumnType
import tech.tablesaw.columns.Column

class CollectionTypeDetector {
    ColumnType[] detectFromMapOfLists(Map<String, Iterable<?>> map) {
        return map.collect { k, v ->
            typeFromList(v, 0.25)
        }
    }

    ColumnType[] detectFromMapOfValues(Map<String,?> map) {
        return map.collect { k, v ->
            typeFromList([v], 0.25)
        }
    }

    private ColumnType typeFromList(Iterable<?> list, double percentageToScan) {
        if (list instanceof TSSeries) {
            Object implementation = list.implementation
            if (implementation instanceof Column) {
                return implementation.type()
            }
        }

        if (list instanceof Column) {
            return list.type()
        }

        int size = list.size()
        int howFarWeGo = size >= 10 ? (size * percentageToScan).toInteger() : size

        List<Class> classes = list.take(howFarWeGo)
                .grep()
                .collect { it.class }
                .unique()

        if (classes.size() > 1 && classes.every{ Number.isAssignableFrom(it)}){
            return resolveFrom(BigDecimal.simpleName)
        }

        if (classes.size() != 1) {
            return resolveFrom(String.simpleName)
        }

        return resolveFrom(classes.find().simpleName)
    }

    private static ColumnType resolveFrom(String clazzName) {
        return switch(clazzName.toUpperCase()) {
            case 'SHORT'                  -> ColumnType.SHORT
            case 'INTEGER'                -> ColumnType.INTEGER
            case ['LONG', 'BIGINTEGER']   -> ColumnType.LONG
            case 'FLOAT'                  -> ColumnType.FLOAT
            case 'BOOLEAN'                -> ColumnType.BOOLEAN
            case 'STRING'                 -> ColumnType.STRING
            case ['DOUBLE', 'BIGDECIMAL'] -> ColumnType.DOUBLE
            case 'LOCALDATE'              -> ColumnType.LOCAL_DATE
            case 'LOCALTIME'              -> ColumnType.LOCAL_TIME
            case 'LOCALDATETIME'          -> ColumnType.LOCAL_DATE_TIME
            case 'INSTANT'                -> ColumnType.INSTANT
            default                       -> ColumnType.STRING
        }
    }
}
