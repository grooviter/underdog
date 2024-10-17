package com.github.grooviter.underdog.impl

import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.TypeJoin
import groovy.transform.builder.Builder
import tech.tablesaw.api.Table

@Builder
class TSDataFrameJoinInfo {
    List<String> on
    List<String> leftOn
    List<String> rightOn
    TypeJoin how
    DataFrame left
    DataFrame right

    String[] rightKeys() {
        return [rightOn, on].grep().find()
    }

    String[] leftKeys() {
        return [leftOn, on].grep().find()
    }

    Table leftTable() {
        return left.implementation as Table
    }

    Table rightTable() {
        return right.implementation as Table
    }
}
