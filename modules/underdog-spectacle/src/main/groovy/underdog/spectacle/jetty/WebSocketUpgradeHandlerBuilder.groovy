package underdog.spectacle.jetty

import org.eclipse.jetty.server.Server
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlEvent
import org.eclipse.jetty.websocket.server.WebSocketUpgradeHandler
import underdog.spectacle.templates.CachedTemplateEngine

/**
 * This class builds an instance of {@link WebSocketUpgradeHandler} containing an embedded instance of
 * {@link WSBackendHandler}. And {@link WSBackendHandler} is the functionality by a Spectacle event
 * that is going to stream its results to output fields.
 *
 * In order to do that there are a few objects required, that's why all that construction is
 * enclosed in this builder class.
 *
 * @since 0.1.0
 */
class WebSocketUpgradeHandlerBuilder {
    static final Integer DEV_WS_TEXT_MESSAGE_SIZE = 128 * 1024

    HtmlEvent event
    HtmlApplication application
    Server server
    CachedTemplateEngine templateEngine

    /**
     * Sets the {@link HtmlEvent}
     *
     * @param event an instance of {@link HtmlEvent}
     * @return the current builder instance
     * @since 0.1.0
     */
    WebSocketUpgradeHandlerBuilder event(HtmlEvent event){
        this.event = event
        return this
    }

    /**
     * Sets the {@link HtmlApplication}
     *
     * @param application an instance of {@link HtmlApplication}
     * @return the current builder instance
     * @since 0.1.0
     */
    WebSocketUpgradeHandlerBuilder application(HtmlApplication application) {
        this.application = application
        return this
    }

    /**
     * Sets the {@link Server}
     *
     * @param server an instance of {@link Server}
     * @return the current builder instance
     * @since 0.1.0
     */
    WebSocketUpgradeHandlerBuilder server(Server server) {
        this.server = server
        return this
    }

    /**
     * Sets the template engine to render html
     *
     * @param templateEngine the application shared template engine of type {@link CachedTemplateEngine}
     * @return the current builder instance
     * @since 0.1.0
     */
    WebSocketUpgradeHandlerBuilder templateEngine(CachedTemplateEngine templateEngine) {
        this.templateEngine = templateEngine
        return this
    }

    /**
     * With the attributes passed previously to the builder it builds a new instance of
     * type {@link WebSocketUpgradeHandler}. This instance will have an embedded instance
     * of type {@link WSBackendHandler} which will make use of the elements of the surrounding
     * object such as request, response, callback and return the result that will be
     * sent to the output.
     *
     * @return an instance of {@link WebSocketUpgradeHandler}
     * @since 0.1.0
     */
    WebSocketUpgradeHandler build() {
        return WebSocketUpgradeHandler.from(server) { container ->
            container.with {
                maxTextMessageSize = DEV_WS_TEXT_MESSAGE_SIZE
                addMapping(this.event.path) { req, res, cb ->
                    return new WSBackendHandler(req, res, cb, application, event, templateEngine)
                }
            }
        }
    }
}
