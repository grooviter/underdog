package com.github.grooviter.underdog.stocks.db.common

import com.github.grooviter.underdog.db.Id

abstract class HasSymbol {
    @Id
    String symbol
}