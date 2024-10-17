package com.github.grooviter.underdog

import static com.github.grooviter.underdog.Series.TypeCorrelation.PEARSON

class SeriesSpec extends BaseSpec {
    def "[Series/Indexing]: extract single value -> df['column'][9]"() {
        when:
        def carbs = df["CARBS"][9]

        then:
        carbs == 4.2
    }

    def "[Series/Indexing]: extract single value -> df['column'].iloc[9]"() {
        when:
        def carbs = df["CARBS"].iloc[9]

        then:
        carbs == 4.2
    }

    def "[Series/operators]: multiply by number -> df['column'] * 2"(){
        setup:
        def df = [
            numbers: 1..10,
            letters: 'a'..'j'
        ].toDF("example")

        when:
        def byTwo = df['numbers'] * 2

        then:
        [2, 4, 6, 8, 10, 12, 14, 16, 18, 20] == byTwo as List<Integer>
    }

    def "[Series/operators]: apply a function -> df['column'](Class<T>) { T x -> x * 2 }"(){
        setup:
        def df = [numbers: 1..10].toDF("example")

        when:
        def byTwo = df['numbers'](Double){ it * 2 }

        then:
        byTwo.iloc[0..1] as List<Integer> == [2, 4]
    }

    def "[Series/utils/casting]: casting to arrays of numbers"(){
        setup:
        def df = [numbers: [4, 8]].toDF("example")

        when:
        def byTwo = df['numbers'] * 0.5

        then:
        [2.0, 4.0] as double[] == byTwo as double[]
        [2.0, 4.0] as Double[] == byTwo as Double[]

        and:
        [2.0, 4.0] as BigDecimal[] == byTwo as BigDecimal[]

        and:
        [2, 4] as int[] == byTwo as int[]
        [2, 4] as Integer[] == byTwo as Integer[]
    }

    def "[Series/utils/describe]: describing series"() {
        when:
        def carbs = df["CARBS"].describe()

        then:
        carbs.columns == ["Measure", "Value"]

        and:
        carbs.size() == 8
    }

    def "[Series/utils/unique]: unique series values -> df['col'].unique()"() {
        expect:
        df['GROUP NAME'].size() == 3197

        and:
        df['GROUP NAME'].unique().size() == 12
    }

    def "[Series/utils/mean]: mean series values -> df['col'].mean()"() {
        when:
        def mean = df.loc['FAT'].mean()

        then:
        mean == 11.9281
    }

    def "[Series/utils/mean]: mean series values -> df['col'].mean(...)"() {
        when:
        def mean = df.loc['FAT'].mean(skipNa: skipNa, precision: precision)

        then:
        mean == expectedValue

        where:
        skipNa | precision | expectedValue
        true   | 2         | 12
        false  | 1         | 10
    }

    def "[Series/utils/corr]: default corr -> df['col'].corr(df['other'])"() {
        setup:
        def df = [
                x: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
                y: [1, 2, 3, 4, 5, 6, 7, 8, 9, 9]
        ].toDF("numbers")

        expect:
        df['x'].corr(df['y']) * 100 == 99.55914616584778
    }

    def "[Series/utils/corr]: Pearson's corr -> df['col'].corr(other: df['other'], type: PEARSON)"() {
        setup:
        def df = [
                x: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
                y: [1, 2, 3, 4, 5, 6, 7, 8, 9, 9]
        ].toDF("numbers")

        expect:
        df['x'].corr(other: df['y'], method: PEARSON) * 100 == 99.55914616584778
    }

    def "[Series/utils/corr]: corr w/ missing values -> df['col'].corr(df['other'])"() {
        setup:
        def df = [
            x: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
            y: [1, 2, 3, 4, 5, null, 7, 8, 9, 9]
        ].toDF("numbers")

        def df_ref = [
            x: [1, 2, 3, 4, 5, 7, 8, 9, 10],
            y: [1, 2, 3, 4, 5, 7, 8, 9, 9]
        ].toDF("numbers_reference")

        expect:
        df['x'].corr(df['y']) == df_ref['x'].corr(df_ref['y'])
    }

    def "[Series/utils/corr]: corr w/ diff lengths -> df['col'].corr(df['other'])"() {
        setup:
        def df1 = [x: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]].toDF("xs")
        def df2 = [y: [1, 2, 3, 4, 5]].toDF("ys")

        expect:
        df1['x'].corr(df2['y']) * 100 == 100
    }

    def "[Series/utils/corr]: corr w/ observation number -> df['col'].corr(other: df['other'], observations: 3)"() {
        setup:
        def df1 = [x: [1, 2, 3, 40, 102]].toDF("xs")
        def df2 = [y: [1, 2, 3, 42, 500]].toDF("ys")

        expect:
        df1['x'].corr(other: df2['y'], observations: 3) * 100 == 100
    }
}
