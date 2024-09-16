package com.github.grooviter.underdog

class DataFrameSpec extends BaseSpec {
    def "[DataFrame/Indexing]: all rows | many columns -> df['col1','coln']"() {
        when:
        def carbs = df["ID", "CARBS"]

        then:
        carbs.columns == ["ID", "CARBS"]

        and:
        carbs.size() == CSV_TOTAL_SIZE
    }

    def "[DataFrame/Indexing/loc]: all rows | many columns -> df.loc['col1','coln']"() {
        when:
        def carbs = df.loc["ID", "CARBS"]

        then:
        carbs.columns == ["ID", "CARBS"]

        and:
        carbs.size() == CSV_TOTAL_SIZE
    }

    def "[DataFrame/Indexing/iloc]: many rows | all columns -> df.iloc[0..<10]"() {
        when:
        def carbs = df.iloc[0..<10]

        then:
        carbs.size() == 10
    }

    def "[DataFrame/Indexing/iloc]: many rows | many columns -> df.iloc[0..<1, 0..<1]"() {
        when:
        def carbs = df.iloc[0..<1,0..<1]

        then:
        carbs.size() == 1

        and:
        carbs.columns.size() == 1
    }

    def "[DataFrame/Indexing/iloc]: single row | many columns -> df.iloc[0, 0..<10]"() {
        when:
        def carbs = df.iloc[0,0..<10]

        then:
        carbs.size() == 1

        and:
        carbs.columns.size() == 10
    }

    def "[DataFrame/Indexing/iloc]: single row (-) | many columns -> df.iloc[-1, 0..<10]"() {
        when:
        def carbs = df.iloc[index,0..<10]

        then:
        carbs.size() == 1

        and:
        carbs.columns.size() == 10

        where:
        index << [-1, -3197]
    }

    def "[DataFrame/Selecting]: single selection | all columns -> df[df[colName] > val]"() {
        when:
        def carbs = df[df["CARBS"] > 90]

        then:
        carbs.size() == 5
    }

    def "[DataFrame/Selecting]: multiple selection | all columns -> df[df[colName] > val & df[colName] < val]"() {
        when:
        def carbs = df[df["CARBS"] > 90 & df["FAT"] < 1]

        then:
        carbs.size() == 4
    }

    def "[DataFrame/Selecting/loc]: single selection | all columns -> df.loc[df[colName] > val]"() {
        when:
        def carbs = df.loc[df["CARBS"] > 90]

        then:
        carbs.size() == 5
    }

    def "[DataFrame/Selecting/loc]: multiple selection | all columns -> df.loc[df[colName] > val & df[colName] < val]"() {
        when:
        def carbs = df.loc[df["CARBS"] > 90 & df["FAT"] < 1]

        then:
        carbs.size() == 4
    }

    def "[DataFrame/Series]: extract series -> df.loc['column']"() {
        when:
        def carbs = df.loc["CARBS"]

        then:
        carbs.size() == CSV_TOTAL_SIZE
    }

    def "[DataFrame/Series]: extract series -> df['column']"() {
        when:
        def carbs = df["CARBS"]

        then:
        carbs.size() == CSV_TOTAL_SIZE
    }

    def "[DataFrame/Series]: modify series values -> df['column'] = df['other_column'] * 2"() {
        setup:
        def df = [numbers: 1..10].toDF("example")

        when:
        df['numbers'] = df['numbers'] * 2

        and:
        def doubled = df['numbers'].toIntegerList()

        then:
        doubled == [2, 4, 6, 8, 10, 12, 14, 16, 18, 20]
    }

    def "[DataFrame/Series]: create new series -> df['new_name'] = df['column'] * 2"() {
        setup:
        def df = [numbers: 1..10].toDF("example")

        when:
        df['doubled'] = df['numbers'] * 2

        and:
        def doubled = df['doubled'].toIntegerList()

        and:
        df.columns == ['numbers', 'doubled']

        then:
        doubled == [2, 4, 6, 8, 10, 12, 14, 16, 18, 20]
    }

    def "[DataFrame/Grouping/agg"() {
        when:
        def grouped = df
            .agg(
                CARBS: 'max',
                FAT: 'max',
                ENERGY: 'max',
                PROTEINS: 'max')
            .by('GROUP NAME')

        then:
        grouped.size() == 12

        and:
        grouped.columns.size() == 5
    }

    def "[DataFrame/Sorting] single column"() {
        when:
        def sortedByCarbs = df//.sortBy("-CARBS")

        then:
        sortedByCarbs['CARBS'].iloc[0] == 98
    }
}
