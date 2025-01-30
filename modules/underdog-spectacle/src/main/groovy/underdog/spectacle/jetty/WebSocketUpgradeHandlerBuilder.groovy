package underdog.spectacle.jetty

import org.eclipse.jetty.server.Server
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlEvent
import org.eclipse.jetty.websocket.server.WebSocketUpgradeHandler

class WebSocketUpgradeHandlerBuilder {
    static final Integer DEV_WS_TEXT_MESSAGE_SIZE = 128 * 1024

    HtmlEvent event
    HtmlApplication application
    Server server

    WebSocketUpgradeHandlerBuilder event(HtmlEvent event){
        this.event = event
        return this
    }

    WebSocketUpgradeHandlerBuilder application(HtmlApplication application) {
        this.application = application
        return this
    }

    WebSocketUpgradeHandlerBuilder server(Server server) {
        this.server = server
        return this
    }

    WebSocketUpgradeHandler build() {
        return WebSocketUpgradeHandler.from(server) { container ->
            container.with {
                maxTextMessageSize = DEV_WS_TEXT_MESSAGE_SIZE
                addMapping(this.event.path) { req, res, cb ->
                    return new StreamingHandler(req, res, cb, application, event)
                }
            }
        }
    }
}
