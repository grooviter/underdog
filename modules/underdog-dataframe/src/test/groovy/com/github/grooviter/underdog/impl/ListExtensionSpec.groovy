package com.github.grooviter.underdog.impl

import spock.lang.Specification

class ListExtensionSpec extends Specification {
    def "[DataFrame/Create]: create DataFrame from a Map"() {
        when:
        def newDf = [
            ID:    ["_1", "_2", "_3"],
            CARBS: [2.2, 3.0, 4.0],
            FAT:   [3.1, 4.2, 5.2]
        ].toDF("food")

        then:
        newDf.size() == 3

        and:
        newDf.columns == ["ID", "CARBS", "FAT"]
    }

    def "[DataFrame/Create]: create DataFrame from a list"() {
        when:
        def newDf = [
            [ID: "_1", "CARBS": 2.2, FAT: 3.1],
            [ID: "_2", "CARBS": 3.0, FAT: 4.2],
            [ID: "_3", "CARBS": 4.0, FAT: 5.2]
        ].toDF("food")

        then:
        newDf.size() == 3

        and:
        newDf.columns == ["ID", "CARBS", "FAT"]
    }
}
