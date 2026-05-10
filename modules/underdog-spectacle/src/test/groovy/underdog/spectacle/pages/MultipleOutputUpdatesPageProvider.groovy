package underdog.spectacle.pages

import underdog.spectacle.dsl.Context
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlPage

import java.util.function.Function

class MultipleOutputUpdatesPageProvider implements Function<HtmlApplication, HtmlPage> {
    @Override
    HtmlPage apply(HtmlApplication app) {
        return HtmlPage.create(
            path: "/multiple-updates",
            title: "Multiple Updates",
            theme: 'dark',
            application: app
        ) {
            form(indicatorSelector: "button") {
                row("row gap-2") {
                    numberCard(className: "card col", name: app.field.count, title: "Count")
                    numberCard(className: "card col", name: app.field.rate, title: "Rate", symbol: "%")
                    numberCard(className: "card col", name: app.field.price, title: 'Price ($)', symbol: '$')
                }
                row("row mt-2") {
                    button(name: app.field.send, text: "Execute")
                }
                onSubmit(
                    [],
                    [
                        app.field.count,
                        app.field.rate,
                        app.field.price
                    ]
                ) { Context ctx ->
                    Thread.sleep(2000)
                    return [
                        new Random().nextInt(20),
                        new Random().nextFloat(1),
                        new Random().nextDouble(1000)
                    ]
                }
            }
        }
    }
}
