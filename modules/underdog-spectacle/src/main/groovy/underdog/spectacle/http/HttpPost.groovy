package underdog.spectacle.http

import java.net.http.HttpRequest

/**
 * Represents a HTTP POST request
 *
 * @since 0.1.0
 */
class HttpPost extends HttpMethod {
    @Override
    HttpMethodAsBytes responseAsBytes() {
        HttpRequest request = buildRequest()
                .POST(HttpRequest.BodyPublishers.ofString(this.body))
                .build()
        return new HttpMethodAsBytes(client, request)
    }

    @Override
    HttpMethodAsString responseAsString() {
        HttpRequest request = buildRequest()
                .POST(HttpRequest.BodyPublishers.ofString(this.body))
                .build()
        return new HttpMethodAsString(client, request)
    }

    @Override
    HttpMethodAsJSON responseAsJSON() {
        HttpRequest request = buildRequest()
                .POST(HttpRequest.BodyPublishers.ofString(this.body))
                .build()
        return new HttpMethodAsJSON(client, request)
    }
}
