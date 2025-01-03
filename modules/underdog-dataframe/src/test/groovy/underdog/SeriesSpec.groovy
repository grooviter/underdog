package underdog


import static underdog.Series.TypeCorrelation.PEARSON

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
        def mean = df.loc['FAT'].dropna().mean()

        then:
        mean == 11.92809544740972
    }

    def "[Series/utils/mean]: mean series values -> df['col'].mean(...)"() {
        when:
        def mean = df.loc['FAT'].dropna().mean(precision)

        then:
        mean == expectedValue

        where:
        precision | expectedValue
        2         | 12
        1         | 10
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

    def "[Series/creation]: iterable.toSeries()"() {
        setup:
        def seriesFromList = (1..20).toList().toSeries()

        when:
        def seriesByTwo = seriesFromList * 2

        then:
        seriesByTwo.toList() == (1..20).collect { it * 2 }
    }

    def "[Series/creation]: iterableWithNulls.toSeries()"() {
        setup:
        def seriesFromList = [1, 2, null, 3, 4].toSeries()

        when:
        def seriesByTwo = seriesFromList * 2

        then:
        seriesByTwo.toList() == [2, 4, null, 6, 8]*.toDouble()

        and:
        seriesByTwo.size() == 5

        and:
        seriesByTwo.dropna().size() == 4
    }

    def "[Series/categorize]: categorize booleans"() {
        setup:
        def seriesFromList = [true, false, true, true].toSeries()

        when:
        def encoded = seriesFromList.encode()

        then:
        encoded.toList() == [1, 0, 1, 1]
    }

    def "[Series/encode]: categorize booleans w/ mappings"() {
        setup:
        def seriesFromList = [true, false, true, true].toSeries()

        when:
        def encoded = seriesFromList.encode((true): 100, (false): 50)

        then:
        encoded.toList() == [100, 50, 100, 100]
    }

    def "[Series/encode]: categorize strings w/ mappings"() {
        setup:
        def seriesFromList = ["john", "jackson", "cooper"].toSeries()

        when:
        def encoded = seriesFromList.encode(john: 1, jackson: 30, cooper: 100)

        then:
        encoded.toList() == [1, 30, 100]
    }
}
