package underdog.impl

import underdog.Criteria
import underdog.DataFrame
import underdog.DataFrameLoc
import underdog.Series
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
        TSCriteria criteriaImplementation = criteria as TSCriteria
        return new TSCriteria(this.selection & criteriaImplementation.selection)
    }

    @Override
    Criteria or(Criteria criteria) {
        TSCriteria criteriaImplementation = criteria as TSCriteria
        return new TSCriteria(this.selection | criteriaImplementation.selection)
    }
}
