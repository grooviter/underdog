package com.github.grooviter.underdog.db.migration

import com.github.grooviter.underdog.db.Id
import groovy.transform.TupleConstructor

@TupleConstructor
class Migration {
    @Id
    String name
    String sql
    String sha256
}
