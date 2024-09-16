package com.github.grooviter.underdog.impl

import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.DataFrameAggregation
import groovy.transform.TupleConstructor
import tech.tablesaw.aggregate.AggregateFunction
import tech.tablesaw.aggregate.AggregateFunctions
import tech.tablesaw.api.Table

/**
 * @since 0.1.0
 */
@TupleConstructor
class TSDataFrameAggregation implements DataFrameAggregation {
    AggregationInfo aggregationInfo
    Table table

    @Override
    DataFrame by(String... columns) {
        List<Table> tables = aggregationInfo
                .infoByColumn()
                .collect {
                    AggregateFunction[] fns = it
                            .functions()
                            .collect { resolveFnByName(it) } as AggregateFunction[]
                    return table.copy().summarize(it.columnName(), fns).by(columns)
                }

        Table grouped = tables.inject { agg, val ->
            agg.joinOn(columns).inner(true, val)
        }

        return new TSDataFrame(grouped)
    }

    private static AggregateFunction resolveFnByName(String name) {
        return switch(name){
            case "mean"   -> AggregateFunctions.mean
            case "max"    -> AggregateFunctions.max
            case "min"    -> AggregateFunctions.min
            case "last"   -> AggregateFunctions.last
            case "first"  -> AggregateFunctions.first
            case "change" -> AggregateFunctions.change
            case "count"  -> AggregateFunctions.count
            default       -> throw new RuntimeException("function '$name' not found")
        }
    }

    @Override
    AggregationInfo getAggregationInfo() {
        return this.aggregationInfo
    }
}
