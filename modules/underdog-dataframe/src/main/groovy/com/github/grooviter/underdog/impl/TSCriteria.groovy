package com.github.grooviter.underdog.impl

import com.github.grooviter.underdog.Criteria
import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.DataFrameLoc
import com.github.grooviter.underdog.Series
import groovy.transform.TupleConstructor
import tech.tablesaw.api.Table
import tech.tablesaw.columns.Column
import tech.tablesaw.selection.Selection

@TupleConstructor
class TSCriteria implements Criteria {
    Selection selection

    @Override
    Series apply(Series series) {
        Column column = series.implementation as Column
        return new TSSeries(column.where(this.selection))
    }

    @Override
    DataFrame apply(DataFrame dataFrame) {
        Table table = dataFrame.implementation as Table
        return new TSDataFrame(table.where(this.selection))
    }

    @Override
    DataFrame apply(DataFrameLoc dataFrame) {
        Table table = ((TSDataFrameLoc) dataFrame).table
        return new TSDataFrame(table.where(this.selection))
    }

    @Override
    Criteria and(Criteria criteria) {
        TSCriteria crit = criteria as TSCriteria
        return new TSCriteria(this.selection & crit.selection)
    }
}
