package com.github.grooviter.underdog.impl

import com.github.grooviter.underdog.Series

import tech.tablesaw.columns.Column

class TSSeries implements Series {
    private final Column column

    TSSeries(Column column){
        this.column = column
    }

    @Override
    Object getImplementation() {
        return this.column
    }

    @Override
    Object getAt(Integer index) {
        return this.column.get(index)
    }

    @Override
    Series getIloc() {
        return this
    }

    @Override
    Series plus(Number o) {
        return new TSSeries(this.column.map(n -> n + o))
    }

    @Override
    Series plus(String st) {
        return new TSSeries(this.implementation.map(n -> n + st))
    }

    @Override
    Series plus(Series series) {
        return new TSSeries(this.implementation.append(series.implementation))
    }

    @Override
    Long size() {
        return this.column.size()
    }
}