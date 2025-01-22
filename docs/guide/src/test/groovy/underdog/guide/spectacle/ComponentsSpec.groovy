package underdog.guide.spectacle

import spock.lang.Specification
import underdog.DataFrame
import underdog.Underdog
import underdog.spectacle.Spectacle

class ComponentsSpec extends Specification {
    private static DataFrame loadDataFrame() {
        return Underdog.df()
            .read_csv(ComponentsSpec.class.classLoader.getResource("data/baseball.csv").file)
    }
    def "inputs"() {
        setup:
        def app = Spectacle.application {
            page('/experiment', theme: 'dark') {
                number(label: 'Temperature', info: 'Temperature in Celsius', value: 3)
                range(label: 'Percentage', value: 30, info: 'Choose the risk level of the experiment')
                text(label: 'Name', info: 'Name of the employee', placeHolder: 'insert name, example: John ,Anna...')
                select(label: 'Bike Brand', info: 'Select your favourite bike brand'){
                    option(0, 'Suzuki')
                    option(1, 'Honda')
                    option(2, 'Yamaha')
                    option(3, 'Kawasaki')
                }

                optionGroup(label: 'Algorithm Type', info: 'Choose the algorithm matching the type of ML case') {
                    option("Classification", true)
                    option("Regression", false)
                }

                row {
                    col(className: 'col-4') {
                        chart(label: 'Progression', info: "Shows some metric progression") {
                            return Underdog.plots().line(1..10, 40..50)
                        }
                    }
                }

                dataframe(
                    label: 'Baseball Data',
                    info: 'Shows Moneyball data csv',
                    value: loadDataFrame()
                )


                button(text: 'Execute', className: '+btn-warning')
                resetLink(text: '', className: '')
            }
        }
        expect:
        app.launch()
    }
}
