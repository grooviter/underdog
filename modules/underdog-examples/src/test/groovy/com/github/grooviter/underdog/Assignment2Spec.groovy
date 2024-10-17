package com.github.grooviter.underdog

import spock.lang.Specification
import com.github.grooviter.underdog.Underdog as ud

class Assignment2Spec extends Specification {
    static CSV_OLIMPICS = "src/test/resources/com/github/grooviter/underdog/tablesaw/olympics.csv"

    def getCsvDataframe() {
        return ud.read_csv(
            CSV_OLIMPICS,
            skipRows: 1,
            allowedDuplicatedNames: true
        )
    }

    def getDf() {
        def seasons = [1] * 4 + [2] * 4 + [3] * 4
        def renamedDf = csvDataframe.rename { index, name ->
            if (index == 0) return "Country"
            return switch(name){
                case ~/01.*/ -> "Gold.${seasons.pop()}"
                case ~/02.*/ -> "Silver.${seasons.pop()}"
                case ~/03.*/ -> "Bronze.${seasons.pop()}"
                case ~/№.*/ -> "Total.${seasons.pop()}"
                default -> name
            }
        }

        renamedDf['ID'] = renamedDf['Country'](String) { it[0..2].toUpperCase() }
        renamedDf['Country'] = renamedDf['Country'](String) {
            it.split(/.\(.*\)/).first()
        }

        def colsToKeep = ['ID', 'Country'] + renamedDf
                .columns
                .findAll { it ==~ /Gold.*|Silver.*|Bronze.*/ }

        return renamedDf[renamedDf['Country'] != 'Totals'].loc[__, colsToKeep]
    }

    def 'Question 0: The following code loads the olympics dataset (olympics.csv)'() {
        expect:
        df.size() == 146
    }

    def 'Question 0: Getting cleaned dataframe with proper column names'() {
        expect:
        df.columns.size() == 11
    }

    def 'Question 0: What is the first country in df?'() {
        when:
        def (country) = df
            .iloc[0]
            .loc['Country'] as List<String>

        then:
        country  == 'Afghanistan'
    }

    def "Question 1: Which country has won the most gold medals in summer games?"() {
        when:
        def (country) = df
            .agg('Gold.1': 'sum').by('Country')
            .sort_values(by: '-Sum [Gold.1]')
            .loc['Country'] as List<String>

        then:
        country == 'United States'
    }

    def "Question 2: Which country had the biggest difference between their summer and winter gold medal counts?"() {
        when:
        def current = df.loc[__, ['Country', 'Gold.1', 'Gold.2']]

        and:
        current['Gold.Diff'] = current['Gold.1'] - current['Gold.2']
        current['Gold.Diff'] = current['Gold.Diff'](Double){ it.abs()}

        and:
        def (country) = current
            .sort_values(by: '-Gold.Diff')
            .iloc[0]
            .loc['Country'] as List<String>

        then:
        country == 'United States'
    }
}
