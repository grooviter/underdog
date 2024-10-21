package com.github.grooviter.underdog.examples

import com.github.grooviter.underdog.Underdog
import spock.lang.Specification

class ChessGraphAnalysisSpec extends Specification {
    static String CHESS_GRAPH = "src/test/resources/com/github/grooviter/underdog/examples/chess_graph.txt"

    def "getting most prolific player"() {
        setup: "loading chess games"
        def df = Underdog
                .read_csv(CHESS_GRAPH, sep: ' ', header: false)
                .rename(mapper: [C0: 'white', C1: 'black', C2: 'outcome', C3: 'date'])
                .drop('date')

        when: "getting the winners as white and black"
        def wonAsWhite = df[df['outcome'] == 1]
                .agg(outcome: 'sum')
                .by('white')
                .rename(mapper: [white: 'id', 'Sum [outcome]': 'wins'])

        def wonAsBlack = df[df['outcome'] == -1]
                .agg(outcome: 'sum')
                .by('black')
                .rename(mapper: [black: 'id', 'Sum [outcome]': 'wins'])

        and: "combining these wins to see how players did with both roles"
        def winCount = wonAsWhite
                .add(wonAsBlack, fill: 0, index: 'id')

        and: "getting the five best performers"
        def winners = winCount
                .nlargest(5, columns: ['wins'])
                .loc['id'] as int[]

        then: "we should get a specific list of ids"
        winners.every { it in [461, 371, 275, 98, 623]}
    }
}
