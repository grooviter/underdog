package underdog.guide.spectacle

import spock.lang.Specification
import underdog.Underdog
import underdog.spectacle.Spectacle
import underdog.spectacle.dsl.Context

class TutorialSpec extends Specification {
    def "tutorial: title and description"() {
        when:
        // --8<-- [start:title_and_description]
        def app = Spectacle.application {
            page('/poc', title: "Vehicle emission") {
                markdown """\
                 | This **POC** tries to create an ML model to predict the vehicle
                 | emissions using Spectacle
                """
            }
        }
        // --8<-- [end:title_and_description]
        /*
        // --8<-- [start:launch]
        app.launch()
        // --8<-- [end:launch]
        */
        then:
        app
    }

    def "tutorial: train and test split"() {
        when:
        // --8<-- [start:train_and_test_split]
        def app = Spectacle.application {
            page('/poc', title: "Vehicle emission") {
                markdown """\
                 | This **POC** tries to create an ML model to predict the vehicle
                 | emissions using Spectacle
                """
                number(label: 'Train rate', value: 75)
            }
        }
        // --8<-- [end:train_and_test_split]
        // app.launch()
        then:
        app
    }

    def "tutorial: adding execution"() {
        when:
        // --8<-- [start:train_and_test_split]
        def app = Spectacle.application {
            page('/poc', title: 'Vehicle emission') {
                markdown """\
                 | This **POC** tries to create an ML model to predict the vehicle
                 | emissions using Spectacle
                """
                number(name: field.trainingRate, label: 'Train rate', value: 75)
            }
        }
        // --8<-- [end:train_and_test_split]
        // app.launch()
        then:
        app
    }

    def "tutorial: adding interaction"() {
        when:
        // --8<-- [start:interaction]
        def app = Spectacle.application {
            page('/poc', title: 'Vehicle emission') {
                markdown """\
                 | This **POC** tries to create an ML model to predict the vehicle
                 | emissions using Spectacle
                """

                number(name: field.trainingRate, label: 'Train rate', value: 75)
                numberCard(name: field.score, defaultValue: 0, title: 'Test Score', className: "+mb-3")

                // button with onClick event
                button(text: 'Execute Model') {
                    onClick(
                        [field.trainingRate], // --> inputs
                        [field.score]         // --> outputs
                    ){ context ->
                        return new Random().nextDouble()
                    }
                }
            }
        }
        // --8<-- [end:interaction]
        // app.launch()
        then:
        app
    }

    def "tutorial: interaction simplified"() {
        when:
        // --8<-- [start:interaction_simplified]
        def app = Spectacle.application {
            page('/poc', title: 'Vehicle emission') {
                markdown """\
                 | This **POC** tries to create an ML model to predict the vehicle
                 | emissions using Spectacle
                """

                // using variables
                def rate = number(label: 'Train rate', value: 75)
                def card = numberCard(defaultValue: 0, title: 'Test Score', className: "+mb-3")

                // using variables to get input and output names
                button(text: 'Execute Model') {
                    onClick([rate.name], [card.name]){ context ->
                        return new Random().nextDouble()
                    }
                }
            }
        }
        // --8<-- [end:interaction_simplified]
        // app.launch()
        then:
        app
    }

    def "tutorial: complete"() {
        when:
        // --8<-- [start:complete]
        def app = Spectacle.application {
            page('/poc', title: 'Vehicle emission') {
                markdown """\
                 | This **POC** tries to create an ML model to predict the vehicle
                 | emissions using Spectacle
                """
                form {
                    number(name: field.trainingRate, label: 'Train rate', value: 75)
                    numberCard(name: field.score, defaultValue: 0, title: 'Test Score', className: "+mb-3")
                    button(text: 'Execute Model')

                    onSubmit([field.trainingRate], [field.score]) { Context context ->
                        // getting value from request
                        def trainRate = context.pDouble(field.trainingRate) / 100

                        // loading data from src/main/resources/data/spectacle/FuelConsumption.csv
                        def df = Underdog.df()
                            .read_csv(context.resource("data/spectacle/FuelConsumption.csv").file)

                        def X = df['ENGINE SIZE','CYLINDERS','FUEL CONSUMPTION'] as double[][]
                        def y = df['COEMISSIONS'] as double[]

                        def (
                            xTrain,
                            xTest,
                            yTrain,
                            yTest
                        ) = Underdog.ml().utils.trainTestSplit(X, y, train_size: trainRate)

                        return Underdog.ml().regression.ols(xTrain, yTrain).score(xTest, yTest)
                    }
                }
            }
        }
        // --8<-- [end:complete]
        app.launch()
        then:
        app
    }
}
