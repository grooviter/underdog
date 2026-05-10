package underdog.spectacle.pages

import underdog.DataFrame
import underdog.Underdog
import underdog.spectacle.Spectacle
import underdog.spectacle.dsl.Context
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlPage

import java.util.function.Function

class ChartUpdatePageProvider implements Function<HtmlApplication, HtmlPage> {
    @Override
    HtmlPage apply(HtmlApplication application) {
        return HtmlPage.create(
            path: "/chart-update",
            title: "Chart Update",
            theme: 'dark',
            application: application
        ) {
            def controller = { Context ctx ->
                // TO SIMULATE WAITING FOR A BACKGROUND TASK
                Thread.sleep(2000)

                def from = ctx.pInteger(application.field.from)
                def to = ctx.pInteger(application.field.to)
                def df = Underdog.df()
                        .read_csv(Spectacle.class.classLoader.getResource("data.csv").file)
                        .sort_values(by: 'X')

                return df[df['X'].isGreaterThanOrEqualTo(from) & df['X'].isLessThanOrEqualTo(to)]
            }
            row {
                markdown """\
                | ## Overview
                | This is a simple example of Spectacle
                """
            }
            row {
                col {
                    form(indicatorSelector: "#input, button, .form-range") {
                        row { number(name: application.field.from, label: 'From (X)', value: 1) }
                        row("row mb-4") {
                            range(
                                name: application.field.to,
                                label: 'To (X)',
                                value: 10,
                                min: 1,
                                max: 10,
                                step: 1,
                                showMarkers: true,
                                showUpdatedValue: true
                            )
                        }
                        row { button(text: 'submit', editable: true) }
                        onSubmit(
                            [application.field.from, application.field.to],
                            [application.field.output],
                            controller
                        )
                    }
                }
                col {
                    chart(
                        name: application.field.output,
                        defaultValue: [X: [], y: []].toDataFrame("empty")
                    ) { DataFrame df ->
                        return Underdog.plots()
                            .line(
                                df['X'],
                                df['y'],
                                title: 'Underdog Example Chart'
                            )
                    }
                }
            }
        }
    }
}
