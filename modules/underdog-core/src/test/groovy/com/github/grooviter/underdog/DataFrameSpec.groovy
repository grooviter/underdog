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
}
