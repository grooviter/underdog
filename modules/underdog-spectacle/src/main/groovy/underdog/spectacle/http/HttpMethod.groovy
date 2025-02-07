package underdog.spectacle.http

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.InheritConstructors

import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * Represents a HTTP request method
 *
 * @since 0.1.0
 */
abstract class HttpMethod {
    /**
     * Represents the HTTP response as an array of bytes
     *
     * @since 0.1.0
     */
    @InheritConstructors
    static class HttpMethodAsBytes extends HttpAs {
        /**
         * Creates an instance of {@link ResourceHandlerPipe} to connect
         * current request with a {@link underdog.spectacle.dsl.ResourceHandler}
         *
         * @param name the name of the {@link underdog.spectacle.dsl.ResourceHandler}
         * @return an instance of {@link ResourceHandlerPipe}
         * @since 0.1.0
         */
        ResourceHandlerPipe pipeToResources(String name) {
            return new ResourceHandlerPipe(
                httpClient,
                httpClient.application.findResourceHandlerByName(name),
                httpRequest,
                HttpResponse.BodyHandlers.ofByteArray())
        }

        /**
         * Executes current request and returns an array of bytes
         *
         * @return an array of bytes
         * @since 0.1.0
         */
        byte[] execute() {
            return httpClient.javaHttpClient
                .send(httpRequest, HttpResponse.BodyHandlers.ofByteArray())
                .body()
        }
    }

    /**
     * Represents the HTTP response as an String
     *
     * @since 0.1.0
     */
    @InheritConstructors
    static class HttpMethodAsString extends HttpAs {
        /**
         * Executes current request and returns a {@link String}
         *
         * @return the result of the current request as {@link String}
         * @since 0.1.0
         */
        String execute() {
            return httpClient.javaHttpClient
                .send(httpRequest, HttpResponse.BodyHandlers.ofString())
                .body()
        }
    }

    @InheritConstructors
    static class HttpMethodAsJSON extends HttpAs {
        <U> U execute() {
            String jsonString = httpClient.javaHttpClient
                .send(httpRequest, HttpResponse.BodyHandlers.ofString())
                .body()

            return new JsonSlurper().parseText(jsonString) as U
        }
    }

    /**
     * Underlying HTTP client
     *
     * @since 0.1.0
     */
    HttpClient client

    /**
     * {@link URI} to get the data from
     *
     * @since 0.1.0
     */
    URI uri

    /**
     * Body of the HTTP request (POST)
     *
     * @since 0.1.0
     */
    String body = ""

    /**
     * Content-Type header of the request
     *
     * @since 0.1.0
     */
    String contentType = "text/plain"

    /**
     * Sets the uri of the request
     *
     * @param uri the {@link URI} of the request
     * @return current instance
     * @since 0.1.0
     */
    HttpMethod uri(URI uri) {
        this.uri = uri
        return this
    }

    /**
     * Sets the {@link HttpClient}
     *
     * @param httpClient an instance of type {@link HttpClient}
     * @return current instance
     * @since 0.1.0
     */
    HttpMethod client(HttpClient httpClient) {
        this.client = httpClient
        return this
    }

    /**
     * Sets the request body as JSON from the object passed as parameter. Uses Groovy's JSON
     * module to convert object to JSON
     *
     * @param object object to convert to JSON string
     * @return current instance
     * @since 0.1.0
     */
    HttpMethod bodyAsJSON(Object object){
        this.body = JsonOutput.toJson(object)
        this.contentType = "application/json"
        return this
    }

    /**
     * Builds part of the HttpRequest with the uri and the content type
     * as they are common of any HTTP method
     *
     * @return a {@link HttpRequest.Builder}
     * @since 0.1.0
     */
    protected HttpRequest.Builder buildRequest() {
        return HttpRequest
            .newBuilder()
            .uri(this.uri)
            .header("Content-Type", this.contentType)
    }

    /**
     * Returns the current HTTP request as a possible response as an array of bytes
     *
     * @return an instance of {@link HttpMethodAsBytes}
     * @since 0.1.0
     */
    abstract HttpMethodAsBytes responseAsBytes()

    /**
     * Returns the current HTTP request as a possible response as a String
     * @return an instance of {@link HttpMethodAsString}
     * @since 0.1.0
     */
    abstract HttpMethodAsString responseAsString()

    abstract HttpMethodAsJSON responseAsJSON()
}
