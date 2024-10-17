package com.github.grooviter.underdog.impl

class CollectionTypeDetector {
    Class[] detectFromMapOfLists(Map<String, List<?>> map) {
        return map.collect { k, v ->
            typeFromList(v, 0.25)
        }
    }

    Class[] detectFromMapOfValues(Map<String,?> map) {
        return map.collect { k, v ->
            typeFromList([v], 0.25)
        }
    }

    private Class typeFromList(List<?> list, double percentageToScan) {
        int size = list.size()
        int howFarWeGo = size >= 10 ? (size * percentageToScan).toInteger() : size

        List<Class> classes = list.take(howFarWeGo)
                .grep()
                .collect { it.class }
                .unique()

        if (classes.size() > 1 && classes.every{ Number.isAssignableFrom(it)}){
            return BigDecimal
        }

        if (classes.size() != 1) {
            return String
        }

        return classes.find()
    }
}
