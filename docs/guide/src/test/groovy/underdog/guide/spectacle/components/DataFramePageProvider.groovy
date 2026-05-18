package underdog.guide.spectacle.components

import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlPage

import java.util.function.Function

class DataFramePageProvider implements Function<HtmlApplication, HtmlPage> {
    @Override
    HtmlPage apply(HtmlApplication app) {
        return HtmlPage.create(
            path: "/components/data",
            title: "Data",
            preTitle: "Examples",
            application: app,
            icon: 'bi bi-database'
        ) {
            row {
                col {
                    markdown """\
                    | In this section you can find components that shows data
                    """
                }
            }
            row {
                col("col-sm-6 col-md-3 col-lg-2") {
                    // --8<-- [start:numberCard]
                    numberCard(
                        title: "Accuracy (test set)",
                        defaultValue: 89.32,
                        symbol: "%",
                        defaultDelta: 10,
                        deltaSymbol: '%'
                    )
                    // --8<-- [end:numberCard]
                }
                col("col-sm-6 col-md-3 col-lg-2") {
                    numberCard(
                        title: "Profit (monthly)",
                        defaultValue: 1.344,
                        symbol: '$',
                        defaultDelta: -8,
                        deltaSymbol: '%'
                    )
                }
                col("col-sm-6 col-md-3 col-lg-2") {
                    numberCard(
                        title: "Loss (monthly)",
                        defaultValue: 1.200,
                        symbol: '$',
                        defaultDelta: 0,
                        deltaSymbol: '%'
                    )
                }
                col("col-sm-6 col-md-3 col-lg-2") {
                    numberCard(
                        title: "Documents",
                        defaultValue: 1,
                        symbol: '',
                        defaultDelta: 2000,
                        deltaSymbol: ''
                    )
                }
                col("col-sm-6 col-md-3 col-lg-2") {
                    numberCard(
                        title: "Embeddings",
                        defaultValue: 340,
                        symbol: '',
                        defaultDelta: 600,
                        deltaSymbol: ''
                    )
                }
                col("col-sm-6 col-md-3 col-lg-2") {
                    numberCard(
                        title: "Memberships",
                        defaultValue: 34,
                        defaultDelta: 98,
                    )
                }
            }
            row {
                col("col-sm-6 col-md-3 col-lg-3") {
                    subjectSummary(
                        name: app.field.githubCommits,
                        icon: 'bi bi-github',
                        iconBackground: 'bg-warning',
                        primaryText: '78 Commits',
                        secondaryText: '116 waiting pull requests'
                    )
                }
                col("col-sm-6 col-md-3 col-lg-3") {
                    subjectSummary(
                        name: app.field.twitterSummary,
                        icon: 'bi bi-twitter',
                        iconBackground: 'bg-pink',
                        primaryText: '123 messages',
                        secondaryText: '54 retweets'
                    )
                }
                col("col-sm-6 col-md-3 col-lg-3") {
                    subjectTrend(
                        name: app.field.saleTrend,
                        symbol: '$',
                        deltaSymbol: '%',
                        defaultValue: 120000,
                        defaultDelta: -12,
                        defaultText: '24 sales in the past 12 months'
                    )
                }
                col("col-sm-6 col-md-3 col-lg-3") {
                    progressPanel(
                        name: app.field.embeddingProgress,
                        title: 'Embeddings',
                        defaultProgressText: '...',
                        defaultValue: 25
                    )
                }
            }
            row {
                col {
                    // --8<-- [start:dataframe]
                    dataframe(
                        name: app.field.dataframe,              // string
                        label: 'Baseball Data',             // string
                        info: 'Shows Moneyball data csv',   // string
                        value: Constants.loadDataFrame()              // underdog.DataFrame
                    )
                    // --8<-- [end:dataframe]
                }
            }
        }
    }
}
