package underdog.spectacle

import underdog.DataFrame
import underdog.Underdog
import underdog.spectacle.dsl.Context

class SpectacleApplication {
    static void main(String[] args) {
        def app = Spectacle.application {
            page('/poc', title: "Vehicle emission", theme: 'dark') {
                markdown """\
                 | ### Description
                 | This **POC** tries to create an **ML** model to predict the vehicle
                 | emissions using **Spectacle**.
                 | More information in the [About](/about) page.
                """
                spec {
                    inputs {
                        number(name: field.cylinders, label: 'Cylinders', value: 3, info: 'Number of cylinders')
                    }
                    outputs {
                        dataframe(label: 'Results', info: "Filtered results")
                    }
                    onSubmit { Context context ->
                        def cylinders = context.pInteger(field.cylinders)
                        def df = loadDataFrame()
                        return df[df['CYLINDERS'].isEqualTo(cylinders)]
                    }
                }
            }
            page("/about", theme: 'dark') {
                markdown """\
                | ### About
                | - [Spectacle Documentation](https://grooviter.github.io/underdog)
                | - [Back to POC](/poc)
                """
            }
        }

        app.dev()
    }

    static DataFrame loadDataFrame() {
        return Underdog.df()
            .read_csv(SpectacleApplication.classLoader.getResource('FuelConsumption.csv').file)
            .drop("Year","MAKE","MODEL","VEHICLE CLASS","ENGINE SIZE","TRANSMISSION","FUEL","COEMISSIONS")
    }
}