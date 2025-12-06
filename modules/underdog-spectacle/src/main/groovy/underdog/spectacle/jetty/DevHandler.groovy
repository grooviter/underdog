package underdog.spectacle.jetty

import groovy.json.JsonOutput
import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import org.eclipse.jetty.util.Callback
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.Session.Listener.AbstractAutoDemanding
import org.eclipse.jetty.websocket.server.ServerUpgradeRequest
import org.eclipse.jetty.websocket.server.ServerUpgradeResponse
import underdog.spectacle.dsl.Utils

import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

/**
 * This websocket handler is used by clients to know when to reload the page
 * because the server has restarted.
 *
 * @since 0.1.0
 */
@Slf4j
@TupleConstructor(includes = ['request', 'response', 'callback'])
class DevHandler extends AbstractAutoDemanding {
    /**
     * For every server run one and only one id will be generated
     * when the server is restarted the id will no longer match
     * the one in the client and the client must reload current npage
     *
     * @since 0.1.0
     */
    private static String version = Utils.generateRandomName()

    /**
     * Number of websocket clients connected. Can be used for example to decide whether
     * to automatically open a new browser or not if you already had the application
     * opened
     *
     * @since 0.1.0
     */
    static AtomicInteger clientsConnected = new AtomicInteger(0)

    ServerUpgradeRequest request
    ServerUpgradeResponse response
    Callback callback

    Session session

    @Override
    void onWebSocketOpen(Session session) {
        clientsConnected.incrementAndGet()
        this.session = session
        this.session.setIdleTimeout(Duration.ofSeconds(30))
        this.session.sendText(createOnPingMessage(version), null)
        log.debug("websocket connection open")
    }

    @Override
    void onWebSocketText(String message) {
        if (message == 'PING') {
            log.debug("websocket received ping")
            this.session.sendText(createOnPingMessage(version), null)
            log.debug("websocket pong sent")
        }
    }

    @Override
    void onWebSocketClose(int statusCode, String reason) {
        clientsConnected.decrementAndGet()
    }

    private static String createOnPingMessage(String version) {
        return JsonOutput.toJson(type: 'PONG', version: version)
    }
}
