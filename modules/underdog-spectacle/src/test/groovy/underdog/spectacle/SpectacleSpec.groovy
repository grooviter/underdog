package underdog.spectacle

import underdog.DataFrame
import underdog.Underdog
import spock.lang.Specification
import underdog.spectacle.dsl.Context

class SpectacleSpec extends Specification {
    def "creating a complete application"() {
        setup:
        def application = Spectacle.application {
            def controller = { Context ctx ->
                def from = ctx.pInteger(field.from)
                def to = ctx.pInteger(field.to)
                def df = Underdog.df()
                    .read_csv(Spectacle.class.classLoader.getResource("data.csv").file)
                    .sort_values(by: 'X')

                df = df[df['X'].isGreaterThanOrEqualTo(from) & df['X'].isLessThanOrEqualTo(to)]
                return df
            }
            page('/chart') {
                row {
                    markdown """\
                        | # Spectacle
                        | This is a simple example of Spectacle
                    """
                }
                row {
                    col {
                        form {
                            row { number(name: field.from, label: 'From (X)', value: 1) }
                            row { number(name: field.to, label: 'To (X)', value: 10) }
                            row { button(text: 'submit', editable: true) }
                            onSubmit([field.from, field.to], [field.output], controller)
                        }
                    }
                    col {
                        chart(
                            name: field.output,
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
            page('/about'){
                row {
                    markdown """\
                        | # About
                        | This is something **important** about the project
                    """
                }
            }
        }
        // application.launch()

        expect:
        application
    }
}
