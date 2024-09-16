package com.github.grooviter.underdog.impl

class CollectionTypeDetector {
    Class[] detectFromMapOfLists(Map<String, List<?>> map) {
        return map.collect { k, v ->
            typeFromList(v, 0.05)
        }
    }

    Class[] detectFromMapOfValues(Map<String,?> map) {
        return map.collect { k, v ->
            typeFromList([v], 0.05)
        }
    }

    private Class typeFromList(List<?> list, double percentageToScan) {
        int size = list.size()
        int howFarWeGo = size >= 10 ? (size * percentageToScan).toInteger() : size

        List<Class> classes = list.take(howFarWeGo)
                .grep()
                .collect { it.class }
                .unique()

        if (classes.size() != 1) {
            return String.class
        }

        return classes.find()
    }
}
