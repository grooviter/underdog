package underdog.sd.cli.http

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.TupleConstructor
import org.apache.hc.client5.http.classic.HttpClient
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.core5.http.ClassicHttpRequest
import org.apache.hc.core5.http.ClassicHttpResponse
import org.apache.hc.core5.http.HttpHeaders
import org.apache.hc.core5.http.HttpStatus
import org.apache.hc.core5.http.io.entity.StringEntity
import org.apache.hc.core5.http.message.BasicHeader
import org.apache.hc.core5.net.URIBuilder
import org.apache.hc.core5.util.Timeout
import underdog.sd.cli.ApiOptions
import underdog.sd.cli.Options

import java.nio.charset.StandardCharsets

@TupleConstructor
class HTTPService {
    HttpClient client
    SerializationService serializationService
    ApiOptions options

    static final Timeout DEFAULT_TIMEOUT = Timeout.ofMinutes(30)

    static HTTPService defaults(ApiOptions apiOptions) {
        Timeout timeout = apiOptions.timeout
                ? Timeout.of(apiOptions.timeout)
                : DEFAULT_TIMEOUT

        PoolingHttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setDefaultConnectionConfig(ConnectionConfig.custom()
                        .setSocketTimeout(timeout)
                        .build())
                .build()

        HttpClientBuilder builder = HttpClients.custom()

        if (apiOptions.apiKey) {
            builder = builder.setDefaultHeaders([new BasicHeader(HttpHeaders.AUTHORIZATION, "Bearer $apiOptions.apiKey")])
        }

        CloseableHttpClient closeableHttpClient = builder
                .setConnectionManager(cm)
                .build()

        ObjectMapper objectMapper = new ObjectMapper()
        SerializationService serializationService = new SerializationService(objectMapper)
        return new HTTPService(closeableHttpClient, serializationService, apiOptions)
    }

    <T> T executeGET(String path, Class<T> clazz) {
        return client.execute(new HttpGet(this.resolve(path)),
                response -> parseResponse(response, clazz))
    }

    <T> T executePOST(String path, Options options, Class<T> clazz) {
        ClassicHttpRequest request = new HttpPost(this.resolve(path))

        if (options) {
            request.setEntity(new StringEntity(this.serializationService.toJson(options), StandardCharsets.UTF_8))
        }

        request.addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        return client.execute(request, response -> parseResponse(response, clazz))
    }

    void checkResponseStatus(ClassicHttpResponse response) {
        if (response.getCode() in [HttpStatus.SC_OK, HttpStatus.SC_ACCEPTED]) {
            return
        }

        if (response.getCode() == HttpStatus.SC_UNPROCESSABLE_CONTENT) {
            throw this.serializationService.fromJson(response.getEntity().getContent(), ServerValidationException.class)
        }
        String content = response.getEntity().getContent().text
        println(content)
        throw this.serializationService.fromJson(content, BadRequestException.class)
    }

    <T> T parseResponse(ClassicHttpResponse response, Class<T> clazz) {
        checkResponseStatus(response)
        return this.serializationService.fromJson(response.getEntity().getContent(), clazz)
    }

    private URI resolve(String path) {
        return new URIBuilder(this.options.baseUrl).setPath(path).build()
    }
}
