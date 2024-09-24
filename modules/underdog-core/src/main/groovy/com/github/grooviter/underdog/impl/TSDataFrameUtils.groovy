package com.github.grooviter.underdog.impl

import tech.tablesaw.aggregate.AggregateFunction
import tech.tablesaw.aggregate.AggregateFunctions

class TSDataFrameUtils {
    static AggregateFunction resolveFnByName(String name) {
        return switch(name){
            case "mean"   -> AggregateFunctions.mean
            case "max"    -> AggregateFunctions.max
            case "min"    -> AggregateFunctions.min
            case "last"   -> AggregateFunctions.last
            case "first"  -> AggregateFunctions.first
            case "change" -> AggregateFunctions.change
            case "count"  -> AggregateFunctions.count
            case "sum"    -> AggregateFunctions.sum
            default       -> throw new RuntimeException("function '$name' not found")
        }
    }
}
