package underdog

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
        def carbs = df.loc[__, ["ID", "CARBS"]]

        then:
        carbs.columns == ["ID", "CARBS"]

        and:
        carbs.size() == CSV_TOTAL_SIZE
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

    def "[DataFrame/Selecting/loc]: multiple selection | some columns -> df.loc[df[colName] > val, ['A', 'B']]"() {
        when:
        def carbs = df.loc[df["CARBS"] > 90 & df["FAT"] < 1, ['BRAND', 'CARBS']]

        then:
        carbs.size() == 4

        and:
        carbs.columns == ['BRAND', 'CARBS']
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
        def doubled = df['numbers'] as List<Integer>

        then:
        doubled == [2, 4, 6, 8, 10, 12, 14, 16, 18, 20]
    }

    def "[DataFrame/Series]: create new series -> df['new_name'] = df['column'] * 2"() {
        setup:
        def df = [numbers: 1..10].toDF("example")

        when:
        df['doubled'] = df['numbers'] * 2

        and:
        def doubled = df['doubled'] as List<Integer>

        and:
        df.columns == ['numbers', 'doubled']

        then:
        doubled == [2, 4, 6, 8, 10, 12, 14, 16, 18, 20]
    }

    def "[DataFrame/Grouping/agg]: aggregate by grouping columns -> df.agg(column: fn...).by(column1...columnN)"() {
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

    def "[DataFrame/Grouping/mean]: calc mean of rows by a col index -> df.mean(axis: rows, index: 'indexCol')"() {
        setup:
        def df = [
            country: ['Spain', 'France', 'Italy'],
            profits_2010: [1000, 2000, 1000],
            profits_2011: [10000, 4000, 3000]
        ].toDF("dataframe")

        when:
        df = df
            .mean(axis: TypeAxis.rows, index: 'country')
            .sort_values(by: 'Country')

        then:
        df.size() == 3

        and:
        df.loc['Country'] as List<String> == ['France', 'Italy', 'Spain']

        and:
        df.loc['country [Mean]'] as List<Integer> == [3000, 2000, 5500]
    }

    def "[DataFrame/Sorting] single column"() {
        when:
        def sortedByCarbs = df.sort_values(by: "-CARBS", skipNa: true)

        then:
        sortedByCarbs['CARBS'].iloc[0] == 88
    }

    def "[DataFrame/Sorting] multiple columns"() {
        when:
        def sortedByCarbs = df.sort_values(by: ["GROUP NAME", "SUBGROUP NAME"])

        then:
        sortedByCarbs['GROUP NAME'].iloc[0] == "Aceites y grasas"
    }

    def "[DataFrame/Join]: inner join -> left.merge(right, on: 'key')"() {
        when:
        def merged = joinLeftDataframe.merge(joinRightDataFrame, on: ["name"])

        then:
        merged.columns.size() == 4

        and:
        merged.size() == 3

        and:
        merged.loc['name'] as List == ['Anne', 'Peter', 'Pirro']
    }

    def "[DataFrame/Join]: full outer join -> left.merge(right, on: 'key', how='outer')"() {
        when:
        def merged = joinLeftDataframe.merge(joinRightDataFrame, on: ["name"], how: TypeJoin.OUTER)

        then:
        merged.columns.size() == 4

        and:
        merged.size() == 5

        and:
        merged.iloc[3] as List == ["William", 25, "Journalist", null]
        merged.iloc[4] as List == ["Alice", null, "", 40000]
    }

    def "[DataFrame/Join]: left outer join -> left.merge(right, on: 'key', how='LEFT_OUTER')"() {
        when:
        def merged = joinLeftDataframe.merge(joinRightDataFrame, on: ["name"], how: TypeJoin.LEFT_OUTER)

        and:
        merged.columns.size() == 4

        then:
        merged.size() == 4

        and:
        merged.iloc[3] as List == ["William", 25, "Journalist", null]
    }

    def "[DataFrame/Join]: right outer join -> left.merge(right, on: 'key', how='RIGHT_OUTER')"() {
        when:
        def merged = joinLeftDataframe.merge(joinRightDataFrame, on: ["name"], how: TypeJoin.RIGHT_OUTER)

        then:
        merged.size() == 4

        and:
        merged.columns.size() == 4

        and:
        merged.loc['name'] as List == ['Anne', 'Peter', 'Pirro', 'Alice']
    }

    def "[DataFrame/Join]: right inner join -> left.merge(right, on: 'key', how='right_inner')"() {
        when:
        def merged = joinLeftDataframe.merge(joinRightDataFrame, on: ["name"], how: TypeJoin.RIGHT_INNER)

        then:
        merged.columns.size() == 4

        and:
        merged.size() == 3

        and:
        merged.loc['name'] as List == ['Anne', 'Peter', 'Pirro']
    }

    private static DataFrame getJoinLeftDataframe() {
        return [
            name: ['Anne', 'Peter', 'Pirro', 'William'],
            ages: [22, 54, 35, 25],
            job: ['Doctor', 'Teacher', 'Firefighter', 'Journalist']
        ].toDF("people")
    }

    private static DataFrame getJoinRightDataFrame() {
        return [
            name: ['Anne', 'Peter', 'Pirro', 'Alice'],
            salary: [100000, 30000, 45000, 40000]
        ].toDF("salaries")
    }

    def "[Dataframe/casting]: casting primitive arrays -> df as number[]"() {
        setup:
        def df = [
            x: (1..10),
            y: (10..1),
            z: (21..30)
        ].toDF("xyz")

        when:
        def theArray = df as int[][]

        then:
        theArray.length == 10

        and:
        theArray[0] == [1, 10, 21] as int[]
        theArray[1] == [2, 9, 22] as int[]
    }

    def "[Dataframe/casting]: casting dataframe with single row -> df as Tuple<x, y>"() {
        setup:
        def df = [x: [1, 2], y: ['a', 'b']].toDF("xyz")

        when:
        def (x, y) = df.iloc[1] as Tuple2<Integer, String>

        then:
        x == 2

        and:
        y == 'b'
    }

    def "[Dataframe/describe]: showing dataframe stats -> df.describe()"() {
        when:
        def df = df.describe()

        then:
        df.columns.size() == 21

        and:
        df.size() == 11
    }

    def "[Dataframe/head]: show first 10 rows -> df.head()"() {
        when:
        def df = df.head()

        then:
        df.columns.size() == 20

        and:
        df.size() == 10
    }

    def "[Dataframe/head]: show first n-rows -> df.head(n)"() {
        when:
        def df = df.head(5)

        then:
        df.columns.size() == 20

        and:
        df.size() == 5
    }

    def "[DataFrame/pivot]: "() {
        setup:
        def brandXGroup = df.pivot(x: 'GROUP NAME', y: 'BRAND', value: 'CARBS', fnName: 'mean')

        when:
        def (result) = brandXGroup
            .loc[brandXGroup['BRAND'] == 'Schweppes']
            .loc['Bebidas no alcohólicas'] as Double[]

        then:
        result == 7.4625
    }

    def "[DataFrame/add]: TypeAxis.rows w/ same columns"() {
        setup:
        def df1 = [a: (1..10), b: (1..10)].toDataFrame()
        def df2 = [a: (10..1), b: (10..1)].toDataFrame()

        when: "adding at row level"
        def df3 = df1.add(df2, axis: TypeAxis.rows)

        and: "getting the first and last result when adding rows"
        def aFirstNumber = df3.loc['a'].iloc[0] as double
        def aLastNumber = df3.loc['a'].iloc[(df3.size() - 1).toInteger()] as double

        then: "result and source tables are same size"
        df3.size() == df1.size()

        and: "first and last number should be the same"
        aFirstNumber == aLastNumber

        and: "no new columns were added"
        df3.columns == df1.columns
        df3.columns == df2.columns
    }

    def "[DataFrame/add]: TypeAxis.rows / different columns / same row size"() {
        setup:
        def df1 = [a: (1..10), b: (1..10)].toDataFrame()
        def df2 = [a: (10..1), b: (10..1), c: (10..19)].toDataFrame()

        when: "adding at row level"
        def df3 = df1.add(df2, axis: TypeAxis.rows)

        and: "getting the first and last result when adding rows"
        def aFirstNumber = df3.loc['a'].iloc[0] as double
        def aLastNumber = df3.loc['a'].iloc[(df3.size() - 1).toInteger()] as double

        then: "result and source tables are same size"
        df3.size() == df1.size()

        and: "first and last number should be the same"
        aFirstNumber == aLastNumber

        and: "new columns were added"
        df3.columns == df1.columns + ['c']
    }

    def "[DataFrame/add]: TypeAxis.rows / different columns / different row size"() {
        setup:
        def df1 = [a: (1..10), b: (1..10)].toDataFrame()
        def df2 = [a: (10..0), b: (10..0), c: (10..20)].toDataFrame()

        when: "adding at row level"
        def df3 = df1.add(df2, axis: TypeAxis.rows, fill: 0)

        and: "getting the first and last result when adding rows"
        def aFirstNumber = df3.loc['a'].iloc[0] as double
        def aLastNumber = df3.loc['a'].iloc[(df3.size() - 1).toInteger()] as double

        then: "result and source tables are same size"
        df3.size() == df1.size() + 1

        and: "first and last number should be the same"
        aFirstNumber == 11
        aLastNumber == 0

        and: "new columns were added"
        df3.columns == df1.columns + ['c']
    }

    def "[DataFrame/add]: TypeAxis.columns"() {
        setup:
        def df1 = [a: (1..10), b: (1..10)].toDataFrame()
        def df2 = [c: (11..20), d: (11..20)].toDataFrame()

        when:
        def df3 = df1.add(df2, axis: TypeAxis.columns)

        then:
        df3.columns == 'a'..'d'
        df3.size() == df1.size()
        df3.size() == df2.size()
    }

    def "[DataFrame/add]: TypeAxis.rows / using index"() {
        setup:
        def df1 = [
                id: [1, 2, 34, 65, 100],
                weight: [10, 20, 300, 600, 1000]
        ].toDataFrame()
        def df2 = [
                id: [2, 34, 65, 100, 1],
                weight: [2, 3, 4, 5, 1]
        ].toDataFrame()

        when:
        def df3 = df1.add(df2, axis: TypeAxis.rows, index: 'id')
        def weights = df3['weight'] as int[]
        def columns = df3.columns

        then:
        columns == ['id', 'weight']
        weights == [11, 22, 303, 604, 1005] as int[]
    }
}
