/*
 * This Spock specification was generated by the Gradle 'init' task.
 */
package com.github.grooviter.underdog

import spock.lang.Specification
import com.github.grooviter.underdog.Underdog as ud

class GettingStartedTest extends Specification {
    static EXCEL_ENERGY = "src/test/resources/com/github/grooviter/underdog/tablesaw/Energy Indicators.xls"
    static CSV_WORLD_BANK = "'"
    static CSV_SCIANGO = ""
    static CSV_FOOD = "src/test/resources/com/github/grooviter/underdog/tablesaw/food.csv"
    static CSV_CENSUS = "src/test/resources/com/github/grooviter/underdog/tablesaw/census.csv"

    def "read a CSV file"() {
        when:
        def df = ud.read_csv(path: CSV_FOOD, nanValues: ["NaN"], sep: ";")

        then:
        df.size == 3197

        and:
        df.columns.size() == 20
    }

    def "Indexing (loc)"() {
        setup:
        def df = ud.read_csv(path: CSV_FOOD, nanValues: ["NaN"], sep: ";")

        when:
        df = df.loc[df["CARBS"] > 12, ["ID", "NAME", "CARBS"]]

        then:
        df.columns.size() == 3

        and:
        df.size == 30
    }

    def "read a CSV file and filter"() {
//        setup: "ENERGY: load excel"
//        def energy = ud
//            .read_excel(path: EXCEL_ENERGY, skipRows: 1, skipFooter: 1)
//            .drop("Unnamed: 0", "Unnamed: 1")
//            .rename(columns: ["Country", "Energy Supply", "Energy Supply per Capita", "% Renewable"])
//
//        and: "ENERGY: cleaning"
//        energy["Energy Supply"]  = energy["Energy Supply"].toNumeric(errors: "coerce") * 1_000_000
//
//        and: "Fixing 'Energy Country' column"
//        energy["Energy Country"] = energy["Country"].str
//            .replace(/ \(.*\)/, "")
//            .replace(/[0-9]*/, "")
//            .strip()
//            .replace(
//                "Republic of Korea": "South Korea",
//                "United States of America": "United States",
//                "United Kingdom of Great Britain and Northern Ireland": "United Kingdom",
//                "China, Hong Kong Special Administrative Region": "Hong Kong")
//
//        and: "WORLD BANK: load csv"
//        def worldBank = ud.read_csv(path: CSV_WORLD_BANK, skipRows: 4)
//
//        and: "cleaning country name columns as well"
//        worldBank["Country Name"] = worldBank["Country Name"].str.replace(
//                "Korea, Rep.": "South Korea",
//                "Iran, Islamic Rep.": "Iran",
//                "Hong Kong SAR, China": "Hong Kong")
//
//        worldBank = worldBank
//            .iloc[__, -10..0]
//            .rename(["Country Name": "Country"])
//
//        and: "SCIANGO: load"
//        def sciango = ud.read_excel(path: CSV_SCIANGO, skipRows: 4).iloc[0..15]
//
//        and: "ALL: merge"
//        def result = sciango
//            .merge(energy, on: ["Country"])
//            .merge(worldBank, left_on: ["Country"], right_on: ["Country"])
//
//        expect: "the size of the selection is just 2 records"
//        result.size == 15
    }
}
