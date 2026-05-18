package underdog.guide.spectacle.components

import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlPage

import java.util.function.Function

class FlowPageProvider implements Function<HtmlApplication, HtmlPage> {
    @Override
    HtmlPage apply(HtmlApplication app) {
        return HtmlPage.create(
            path: "/components/flow",
            title: "Flow",
            preTitle: "Examples",
            application: app,
            icon: 'bi bi-repeat'
        ) {
            row {
                col {
                    markdown """\
                    | In this section you can find components that imply data flow or some kind of workflow
                    """
                }
            }
            row {
                col("col-4") {
                    row {
                        markdown """\
                        | ## Steps
                        | 
                        | Ordered steps
                        """
                    }
                    row {

                    }
                }
                col("col-4") {
                    row {
                        markdown """\
                        | ## Timeline
                        | 
                        | Ordered steps with more info: time, title, description
                        |
                        """
                    }
                    row {
                        // --8<-- [start:timeline]
                        timeLine("timeline") {
                            item(
                                title: "Normal",
                                when: "16 hrs",
                                description: "Hourly check, everything working fine",
                                iconText: "1",
                                iconBackground: "bg-green"
                            )
                            item(
                                title: "Warning",
                                when: "15 hrs",
                                description: "Hourly check, temperature above average",
                                iconText: "2",
                                iconBackground: "bg-warning"
                            )
                            item(
                                title: "Danger",
                                when: '14 hrs',
                                description: "Hourly check, temperature critical",
                                icon: "bi bi-person",
                                iconBackground: "bg-danger"
                            )
                        }
                        // --8<-- [end:timeline]
                    }
                }
            }
        }
    }
}
