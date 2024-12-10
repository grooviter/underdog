package underdog.guide.dataframe.dataframe

import spock.lang.Specification

import java.time.LocalDate

class FilteringSpec extends Specification {
    def "filtering: numbers"() {
        setup:
        // tag::numbers[]
        def df = [
            years: (1991..2000),
            population: (1..10).collect { 1000 * it }
        ].toDataFrame("population increase")
        // end::numbers[]

        when:
        // tag::greaterThanNumber[]
        def last5Years = df[df['years'] > 1995]
        // end::greaterThanNumber[]

        // tag::lessThanNumber[]
        def yearsWithLessThan = df[df['population'] < 4000]
        // end::lessThanNumber[]
        println(yearsWithLessThan)

        then:
        last5Years.size() == 5
        yearsWithLessThan.size() == 3
    }

    def "filtering: strings"() {
        setup:
        // tag::string[]
        def df = [
            employees: ['Udo', 'John', 'Albert', 'Ronda'],
            department: ['sales', 'it', 'sales', 'it'],
            payroll: [10_000, 12_000, 11_000, 13_000]
        ].toDataFrame("employees")
        // end::string[]

        when:
        // tag::string_equals[]
        def salesPeople = df[df['department'] == 'sales']
        // end::string_equals[]
        println(salesPeople)

        // tag::string_in[]
        def employeesByName = df[df['employees'] in ['Ronda', 'Udo']]
        // end::string_in[]
        println(employeesByName)

        // tag::string_regex[]
        def employeesWithAnO = df[df['employees'] ==~ /.*o.*/ ]
        // end::string_regex[]
        println(employeesWithAnO)

        then:
        employeesByName.size() == 2
        salesPeople.size() == 2
    }

    def "filtering: by dates"() {
        setup:
        // tag::dates_df[]
        def initialDate = LocalDate.parse('01/01/2000', 'dd/MM/yyyy') // <1>

        def df = [ // <2>
            dates: (1..365).collect(initialDate::plusDays),
            rented: (1..365).collect { new Random().nextInt(200) }
        ].toDataFrame("rented bicycles 2000")
        // end::dates_df[]
        println(df)
        when:
        // tag::dates_after[]
        def lastMonth = df[df['dates'] >= LocalDate.parse('01/12/2000', 'dd/MM/yyyy')]
        // end::dates_after[]
        println(lastMonth)
        then:
        lastMonth.size() == 31
    }
}
