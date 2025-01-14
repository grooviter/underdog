package underdog.spectacle

import underdog.Underdog
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.jetty.JettyApplication

class Spectacle {

    static Application application(@DelegatesTo(HtmlApplication) Closure dsl) {
        return new JettyApplication(new HtmlApplication().tap { with(dsl) })
    }

    static void main(args) {
        def application = application {
            def chartData = post('/chart/data') { req, res ->
                return Underdog.df()
                    .read_csv(resource('data.csv'))
                    .toJSON(asSeriesMap: true, pretty: true)
            }
            page('/chart') {
                row {
                    markdown """\
                        | # Spectacle
                        | This is a simple example of Spectacle
                    """
                }
                row {
                    number(name: field.from, label: 'from')
                    number(name: field.to, label: 'to')
                }
                row {
                    textArea(name: field.output, label: 'comments')
                    button(text: 'submit') {
                        onClick(chartData.name, [field.from, field.to], [field.output])
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

        application.launch()
    }
}
