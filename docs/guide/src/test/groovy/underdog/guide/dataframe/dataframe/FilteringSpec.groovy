package underdog.guide.dataframe.dataframe

import spock.lang.Specification

import java.time.LocalDate

class FilteringSpec extends Specification {
    def "filtering: numbers"() {
        setup:
        // --8<-- [start:numbers]
        def df = [
            years: (1991..2000),
            population: (1..10).collect { 1000 * it }
        ].toDataFrame("population increase")
        // --8<-- [end:numbers]

        when:
        // --8<-- [start:greaterThanNumber]
        def last5Years = df[df['years'] > 1995]
        // --8<-- [end:greaterThanNumber]

        // --8<-- [start:lessThanNumber]
        def yearsWithLessThan = df[df['population'] < 4000]
        // --8<-- [end:lessThanNumber]
        println(yearsWithLessThan)

        then:
        last5Years.size() == 5
        yearsWithLessThan.size() == 3
    }

    def "filtering: strings"() {
        setup:
        // --8<-- [start:string]
        def df = [
            employees: ['Udo', 'John', 'Albert', 'Ronda'],
            department: ['sales', 'it', 'sales', 'it'],
            payroll: [10_000, 12_000, 11_000, 13_000]
        ].toDataFrame("employees")
        // --8<-- [end:string]

        when:
        // --8<-- [start:string_equals]
        def salesPeople = df[df['department'] == 'sales']
        // --8<-- [end:string_equals]
        println(salesPeople)

        // --8<-- [start:string_in]
        def employeesByName = df[df['employees'] in ['Ronda', 'Udo']]
        // --8<-- [end:string_in]
        println(employeesByName)

        // --8<-- [start:string_regex]
        def employeesWithAnO = df[df['employees'] ==~ /.*o.*/ ]
        // --8<-- [end:string_regex]
        println(employeesWithAnO)

        then:
        employeesByName.size() == 2
        salesPeople.size() == 2
    }

    def "filtering: by dates"() {
        setup:
        // --8<-- [start:dates_df]
        // Using a given date as the beginning of our df dates series
        def initialDate = LocalDate.parse('01/01/2000', 'dd/MM/yyyy')

        // a dataframe containing the simulation of bicycles rented through 2000
        def df = [
            dates: (1..365).collect(initialDate::plusDays),
            rented: (1..365).collect { new Random().nextInt(200) }
        ].toDataFrame("rented bicycles 2000")
        // --8<-- [end:dates_df]
        println(df)
        when:
        // --8<-- [start:dates_after]
        def lastMonth = df[df['dates'] >= LocalDate.parse('01/12/2000', 'dd/MM/yyyy')]
        // --8<-- [end:dates_after]
        println(lastMonth)
        then:
        lastMonth.size() == 31
    }

    def "combine or and and"() {
        setup:
        def df = [
            years: (1991..2000),
            population: (1..10).collect { 1000 * it }
        ].toDataFrame("population increase")

        when:
        // --8<-- [start:combine_and]
        def result1 = df[df['years'] > 1995 & df['population'] <= 8000]
        // --8<-- [end:combine_and]

        // --8<-- [start:combine_or]
        def result2 = df[df['years'] <= 1998 | df['population'] > 9000]
        // --8<-- [end:combine_or]

        then:
        result1
        result2
    }
}
