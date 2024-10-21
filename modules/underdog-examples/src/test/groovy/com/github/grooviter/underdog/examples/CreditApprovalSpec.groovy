package com.github.grooviter.underdog.examples

import com.github.grooviter.underdog.DataFrame
import com.github.grooviter.underdog.Series
import com.github.grooviter.underdog.Underdog
import com.github.grooviter.underdog.smile.Smile
import spock.lang.Specification

class CreditApprovalSpec extends Specification {
    static String CREDIT_CSV = "src/test/resources/com/github/grooviter/underdog/examples/german.data"
    static List<String> COLUMNS = [
        'status',
        'duration',
        'history',
        'purpose',
        'amount',
        'savings',
        'employment_since',
        'installment_rate',
        'personal_status',
        'debtors',
        'residence_since',
        'property',
        'age',
        'installment_others',
        'housing',
        'existing_credits',
        'job',
        'people_being_liable',
        'telephone',
        'foreign_worker',
        'label'
    ]

    DataFrame loadDataFrame() {
        return Underdog
            .read_csv(CREDIT_CSV, sep: " ", allowedDuplicatedNames: true)
            .rename(columns: COLUMNS)
            .dropna()
    }

    DataFrame convertCategoricalToNumerical() {
        def df = loadDataFrame()
        def cols_not_to_convert = [
            'duration',
            'installment_rate',
            'age',
            'amount',
            'existing_credits',
            'people_being_liable',
            'label'
        ]

        df.columns
            .findAll { it !in cols_not_to_convert }
            .each { colName ->
                df[colName] = toNumerical(df[colName])
            }

        return df
    }

    private static Series toNumerical(Series series) {
        def uniques = series.sort().unique() as List<String>
        def nValues = uniques
                .indexed()
                .collectEntries { k, v -> [(v): k] }
        return series(String, Integer) {nValues[it]}
    }

    Tuple2<double[][], int[]> creatingTrainingSet() {
        def feature_cols = [
            'duration',
            'amount',
            'job',
            'age',
            'history',
            'employment_since',
            'telephone',
            'existing_credits',
            'savings',
            'property'
        ]
        def df = convertCategoricalToNumerical()
        return [
            df[feature_cols] as double[][],
            df['label'](Integer, Integer){ it == 1 ? 1 : -1 } as int[]
        ]
    }

    def "loading feature cols"() {
        setup: "getting features and labels"
        def (X, y) = creatingTrainingSet()

        and: "creating training and test sets"
        def (
            xTrain,
            xTest,
            yTrain,
            yTest
        ) = Smile.utils.trainTestSplit(X, y, random_state: 10, shuffle: true)

        and: "scaling feature set"
        def scaler = Smile.features.minMaxScaler(xTrain)
        def xTrainScaled = scaler.apply(xTrain)
        def xTestScaled = scaler.apply(xTest)

        when: "creating a model with the training set"
        def classifier = Smile.classification.svc(xTrainScaled, yTrain, C: 0.001)
        def prediction = classifier.predict(xTestScaled)

        then: "we expect to reach a certain score threshold"
        Smile.metrics.accuracy(yTest, prediction) > 0.671
    }

    def "loading dataframe"() {
        expect:
        convertCategoricalToNumerical()[
            ['label', 'duration', 'history', 'purpose', 'amount']
        ].columns.size() == 5
    }
}
