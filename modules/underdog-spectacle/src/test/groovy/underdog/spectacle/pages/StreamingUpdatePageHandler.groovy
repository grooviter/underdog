package underdog.spectacle.pages

import reactor.core.publisher.Flux
import underdog.spectacle.dsl.Context
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlPage
import underdog.spectacle.jetty.JettyWSContext

import java.time.Duration
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Function

class StreamingUpdatePageHandler implements Function<HtmlApplication, HtmlPage> {
    JettyWSContext wsContext

    @Override
    HtmlPage apply(HtmlApplication app) {
        return HtmlPage.create(
            path: "/streaming-update",
            title: "Multiple Streaming Sources",
            theme: "dark",
            application: app
        ) {
           form(
               streaming: true,
               indicatorSelector: "#${app.field.start}"
           ) {
               row {
                   col("col-6 d-flex flex-columns gap-2") {
                       numberCard(className: "card w-100", name: app.field.google, title: "Google", symbol: '$')
                       numberCard(className: "card w-100", name: app.field.nvidia, title: "Nvidia", symbol: '$')
                       numberCard(className: "card w-100", name: app.field.msft, title: "Microsoft", symbol: '$')
                   }
               }
               row {
                   col("col-6") {
                       textArea(name: app.field.log, label: "Log", required: false)
                   }
               }
               row {
                   col("col-6 d-flex gap-2") {
                       button(name: app.field.start, text: "Start", iconName: "bi bi-play")
                       button(name: app.field.stop, text: "Stop", iconName: "bi bi-stop"){
                           onClick([], [app.field.log]) { Context ctx ->
                               wsContext.cancelContext()
                               return "Context Cancelled!!!"
                           }
                       }
                   }
               }
               onSubmit(
                   [],
                   [app.field.google, app.field.nvidia, app.field.msft]
               ) { JettyWSContext ctx ->
                   wsContext = ctx
                   return [
                       generateFlux(),
                       generateFlux(),
                       generateFlux()
                   ]
               }
           }
        }
    }

    static Flux<Double> generateFlux() {
        return Flux
            .interval(Duration.ofSeconds(1))
            .map(tick -> ThreadLocalRandom.current().nextDouble())
            .map(n -> n.round(2))
    }
}
