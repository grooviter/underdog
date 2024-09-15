package com.github.grooviter.underdog

class SeriesSpec extends BaseSpec {
    def "[Series]: extract single value -> df['column'][9]"() {
        when:
        def carbs = df["CARBS"][9]

        then:
        carbs == 4.2
    }

    def "[Series]: extract single value -> df['column'].iloc[9]"() {
        when:
        def carbs = df["CARBS"].iloc[9]

        then:
        carbs == 4.2
    }
}
