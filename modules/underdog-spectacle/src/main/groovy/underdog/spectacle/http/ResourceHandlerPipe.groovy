package underdog.spectacle.http

import groovy.transform.TupleConstructor
import underdog.spectacle.dsl.ResourceHandler

import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * Connects possible file coming from an HTTP request to a {@link ResourceHandler}
 * making it easier to save files with the same resource handlers that are going
 * to expose them.
 *
 * @since 0.1.0
 */
@TupleConstructor
class ResourceHandlerPipe {
    /**
     * Underlying http client
     *
     * @since 0.1.0
     */
    HttpClient httpClient

    /**
     * Resource handler to use to store response
     *
     * @since 0.1.0
     */
    ResourceHandler resourceHandler

    /**
     * Current http request
     *
     * @since 0.1.0
     */
    HttpRequest httpRequest

    /**
     * The body handler responsible for processing http response
     *
     * @since 0.1.0
     */
    HttpResponse.BodyHandler<byte[]> bodyHandler

    /**
     * Saves current response without a specific name
     *
     * @return the name of the saved resource
     * @since 0.1.0
     */
    String save() {
        return resourceHandler.save(send())
    }

    /**
     * Saves current response with a specific name
     *
     * @param filename name of the file to save
     * @return the name of the saved resource
     * @since 0.1.0
     */
    String save(String filename) {
        return resourceHandler.save(filename, send())
    }

    private byte[] send() {
        return httpClient.javaHttpClient.send(httpRequest, bodyHandler).body()
    }
}
