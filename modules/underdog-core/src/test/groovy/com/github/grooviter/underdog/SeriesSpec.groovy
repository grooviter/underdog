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

    def "[Series/operators]: multiply by number -> df['column'] * 2"(){
        setup:
        def df = [
            numbers: 1..10,
            letters: 'a'..'j'
        ].toDF("example")

        when:
        def byTwo = df['numbers'] * 2

        then:
        byTwo.iloc[0] == 2
    }
}
