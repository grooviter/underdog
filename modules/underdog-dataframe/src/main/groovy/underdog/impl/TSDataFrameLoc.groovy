package underdog.impl

import underdog.Criteria
import underdog.DataFrame
import underdog.DataFrameLoc
import underdog.Series
import underdog.Wildcard
import underdog.impl.ast.ASTDriven
import groovy.transform.NamedVariant
import groovy.transform.TupleConstructor
import tech.tablesaw.api.Table

@TupleConstructor
class TSDataFrameLoc implements DataFrameLoc {
    Table table

    @ASTDriven
    @NamedVariant
    DataFrame getAt(Boolean selection) {
        throw new RuntimeException("")
    }

    @Override
    DataFrame getAt(Criteria criteria, List<String> columns) {
        return criteria.apply(new TSDataFrame(this.table.selectColumns(columns as String[])))
    }

    @Override
    Series getAt(String columnName) {
        return new TSSeries(this.table.column(columnName))
    }

    @Override
    DataFrame getAt(Wildcard wildcard, List<String> columns) {
        String[] colArray = columns as String[]
        Table filteredTable = switch(wildcard) {
            case Wildcard.ALL   -> this.table.selectColumns(colArray)
            case Wildcard.FIRST -> this.table.rows(0).selectColumns(colArray)
            case Wildcard.LAST  -> this.table.rows(this.table.rowCount() - 1).selectColumns(colArray)
            default             -> this.table.selectColumns(colArray)
        }
        return new TSDataFrame(filteredTable)
    }
}
