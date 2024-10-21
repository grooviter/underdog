package com.github.grooviter.underdog.stocks.db

import com.github.grooviter.underdog.stocks.db.common.HasSymbol
import groovy.transform.TupleConstructor

@TupleConstructor
class Stock extends HasSymbol {
    String name
    String stockExchange
}
