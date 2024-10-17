package com.github.grooviter.underdog

import spock.lang.Specification
import com.github.grooviter.underdog.Underdog as ud

import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat

class Assignment3Spec extends Specification{
    static EXCEL_ENERGY = "src/test/resources/com/github/grooviter/underdog/tablesaw/Energy Indicators.xls"
    static CSV_WORLD_BANK = "src/test/resources/com/github/grooviter/underdog/tablesaw/world_bank.csv"
    static CSV_SCIMAGO = "src/test/resources/com/github/grooviter/underdog/tablesaw/scimagojr-3.xlsx"

    def loadEnergy() {
        def energy = ud
                .read_excel(path: EXCEL_ENERGY, skipRows: 16, skipFooter: 100)
                .drop("Unnamed: 0", "Unnamed: 1")
                .rename(columns: ["Country", "Energy Supply", "Energy Supply per Capita", "% Renewable"])

        energy["Energy Supply"] = energy["Energy Supply"].toNumeric(errors: "coerce") * 1_000_000
        energy["Country"] = energy["Country"](String) {
            return it.replace(/ \(.*\)/, "")
                    .replace(/[0-9]*/, "")
                    .strip()
                    .replace(
                            "Republic of Korea": "South Korea",
                            "United States of America": "United States",
                            "United Kingdom of Great Britain and Northern Ireland": "United Kingdom",
                            "China, Hong Kong Special Administrative Region": "Hong Kong")
        }

        // the problem is that the resulting dataframe coming from read_excel
        // has too many lines of garbage. We need to improve the dataset
        // detection
        //
        return energy
                .dropna(byColumns: ['Country'])
                .sort_values(by: 'Country') // TODO: dropna by index
    }

    DataFrame loadWorldBank() {
        def worldBank = ud.read_csv(CSV_WORLD_BANK, skipRows: 4)

        worldBank["Country Name"] = worldBank["Country Name"](String) {
            it.replace(
                    "Korea, Rep.": "South Korea",
                    "Iran, Islamic Rep.": "Iran",
                    "Hong Kong SAR, China": "Hong Kong")
        }

        return worldBank
            .iloc[__, [0] + (-10..-1)]
            .rename(mapper:  ["Country Name": "Country"])
    }

    DataFrame loadSciango() {
        return ud.read_excel(path: CSV_SCIMAGO, skipRows: 4).iloc[0..<15]
    }

    DataFrame answerOne() {
        def energy = loadEnergy()
        def worldBank = loadWorldBank()
        def sciango = loadSciango()

        return sciango
            .merge(energy, on: ["Country"])
            .merge(worldBank, on: ["Country"])
    }

    def "Question 1"() {
        expect:
        answerOne().size() == 6
    }

    def "Question 2: When you joined the previous datasets, how many entries did you lose?"() {
        setup:
        def energy = loadEnergy()
        def worldBank = loadWorldBank()
        def sciango = loadSciango()

        def innerMerged = answerOne()
        def outerMerged = sciango
                .merge(energy, how: TypeJoin.OUTER, on: ['Country'])
                .merge(worldBank, how: TypeJoin.OUTER, on: ['Country'])

        expect:
        outerMerged.size() - innerMerged.size() == 297
    }

    def "Question 3: What is the average GDP over the last 10 years for each country?"() {
        setup:
        def answer = answerOne()
            .iloc[__, [1] + (-10..-1)]
            .mean(axis: TypeAxis.rows, index: "Country")

        expect:
        answer.size() == 6

        and:
        answer.columns == ['Country', 'Country [Mean]']
    }

    def "Question 3b: What is the average GDP for each country the last 10 years ?"() {
        setup:
        def answer = answerOne()
                .iloc[__, [1] + (-10..-1)]
                .mean(axis: TypeAxis.columns, index: "Country")

        expect:
        answer.size() == 6

        and:
        answer.columns == ['Country'] + (2006..2015).collect { "Mean [$it]" }
    }

    def 'Question 4: By how much had the GDP changed over the 10 year span for the country with the 5th largest average GDP?'() {
        setup:
        def top_6 = answerOne()

        when:
        top_6['avgGDP'] = answerOne()
                .iloc[__, [1] + (-10..-1)]
                .mean(axis: TypeAxis.rows, index: "Country")
                .loc['Country [Mean]']

        and:
        def (gdp_2006, gdp_2015) = top_6
            .nlargest(6, ['avgGDP'])
            .iloc[-2, 11..20] // South Korea
            .iloc[-1, [0, -1]] as Tuple2<Double, Double>

        then:
        (gdp_2015 - gdp_2006) == 325560528159.9851
    }

    def "Question 5: What is the mean Energy Supply per Capita?"() {
        expect:
        answerOne()
            .loc['Energy Supply Per Capita']
            .toNumeric(errors: 'coerce')
            .mean() == 173.3333
    }

    def "Question 6: What country has the maximum % Renewable and what is the percentage?"() {
        when:
        def result = answerOne()
                .sort_values(by: '-% Renewable')
                .loc[__, ['Country', '% Renewable']]

        result['% Renewable'] = result['% Renewable'].toNumeric(errors: 'coerce')

        def (country, percentage) = result.iloc[0] as Tuple2<String, Double>

        then:
        country == 'Norway'

        and:
        percentage == 97.64
    }

    def "Question 7: What is the maximum value for this new column, and what country has the highest ratio?"() {
        setup:
        def df = answerOne()

        and:
        df['ratio'] = df['Self-citations'] / df['Citations']
        df['ratio'] = df['ratio'](Double){it
                .toBigDecimal()
                .setScale(2, RoundingMode.HALF_EVEN)
                .toDouble()
        }

        when:
        def (country, ratio) = df
            .sort_values(by: '-ratio')
            .loc[__, ['Country', 'ratio']]
            .iloc[0] as Tuple2<String, Double>

        then:
        country == 'India'

        and:
        ratio == 0.29
    }

    def "Question 8: What is the third most populous country according to this estimate?"() {
        setup:
        def df = answerOne()

        when:
        df['estimated'] = df['Energy Supply'] / df['Energy Supply per Capita'].toNumeric(errors: 'coerce')

        and:
        def country = df.sort_values(by: '-estimated').loc['Country'].iloc[2] as String

        then:
        country == 'India'
    }
}
