package underdog

interface Criteria {
    Series apply(Series series)
    DataFrame apply(DataFrame dataFrame)
    DataFrame apply(DataFrameLoc dataFrame)
    Criteria and(Criteria criteria)
    Criteria or(Criteria criteria)
}