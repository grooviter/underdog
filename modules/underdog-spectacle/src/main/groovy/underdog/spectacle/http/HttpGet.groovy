package underdog.spectacle.http

import java.net.http.HttpRequest

/**
 * Represents a HTTP GET request
 *
 * @since 0.1.0
 */
class HttpGet extends HttpMethod {
    @Override
    HttpMethodAsBytes responseAsBytes() {
        HttpRequest request = buildRequest().GET().build()
        return new HttpMethodAsBytes(client, request)
    }

    @Override
    HttpMethodAsString responseAsString() {
        HttpRequest request = buildRequest().GET().build()
        return new HttpMethodAsString(client, request)
    }

    @Override
    HttpMethodAsJSON responseAsJSON() {
        HttpRequest request = buildRequest().GET().build()
        return new HttpMethodAsJSON(client, request)
    }
}
