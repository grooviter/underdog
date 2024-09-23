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
                            .collect { TSDataFrameUtils.resolveFnByName(it) } as AggregateFunction[]
                    return table.copy().summarize(it.columnName(), fns).by(columns)
                }

        Table grouped = tables.inject { agg, val ->
            agg.joinOn(columns).inner(true, val)
        }

        return new TSDataFrame(grouped)
    }

    @Override
    AggregationInfo getAggregationInfo() {
        return this.aggregationInfo
    }
}
