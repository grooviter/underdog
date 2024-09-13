package com.github.grooviter.underdog.io

import com.github.grooviter.underdog.Underdog as ud
import com.github.grooviter.underdog.tablesaw.test.BaseSpec
import tech.tablesaw.api.Table

class ReadProxyExtensionsSpec extends BaseSpec {
    void 'read a csv from matter import'() {
        when:
        Table table = ud.read().csv(
                path: "",
                sep: "",
                dateTimeFormat: "")

        then:
        table.rowCount() == 3197
    }
}
