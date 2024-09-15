package com.github.grooviter.underdog

class DataFrameSpec extends BaseSpec {
    def "[DataFrame/Indexing]: extract several columns -> df.loc['col1',...,'coln']"() {
        when:
        def carbs = df.loc["ID", "CARBS"]

        then:
        carbs.columns == ["ID", "CARBS"]

        and:
        carbs.size() == CSV_TOTAL_SIZE
    }

    def "[DataFrame/Indexing]: extract several columns -> df['col1',...,'coln']"() {
        when:
        def carbs = df["ID", "CARBS"]

        then:
        carbs.columns == ["ID", "CARBS"]

        and:
        carbs.size() == CSV_TOTAL_SIZE
    }

    def "[DatafFrame/Series]: extract series -> df.loc['column']"() {
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
