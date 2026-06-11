package underdog.sd.cli.http

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.StreamReadConstraints
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
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpEntity
import org.apache.hc.core5.http.HttpHeaders
import org.apache.hc.core5.http.HttpStatus
import org.apache.hc.core5.http.io.entity.StringEntity
import org.apache.hc.core5.http.message.BasicHeader
import org.apache.hc.core5.net.URIBuilder
import org.apache.hc.core5.util.Timeout
import underdog.sd.cli.ApiOptions
import underdog.sd.cli.Request

import java.nio.charset.StandardCharsets

@TupleConstructor
class HTTPService {
    static final String EMPTY_JSON = '{}'
    static final Timeout DEFAULT_TIMEOUT = Timeout.ofMinutes(30)

    HttpClient client
    SerializationService serializationService
    ApiOptions options

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

        StreamReadConstraints constraints = StreamReadConstraints.builder()
                .maxStringLength(100_000_000) // set appropriately
                .build()

        JsonFactory factory = JsonFactory.builder()
                .streamReadConstraints(constraints)
                .build()

        ObjectMapper objectMapper = new ObjectMapper(factory)
        SerializationService serializationService = new SerializationService(objectMapper)
        return new HTTPService(closeableHttpClient, serializationService, apiOptions)
    }

    <T> T executeGET(String path, Class<T> clazz) {
        return client.execute(new HttpGet(this.resolve(path)),
                response -> parseResponse(response, clazz))
    }

    <T> T executePOST(String path, Request options, Class<T> clazz) {
        ClassicHttpRequest request = new HttpPost(this.resolve(path))
        request.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)

        if (options) {
            request.setEntity(new StringEntity(this.serializationService.toJson(options), StandardCharsets.UTF_8))
        }

        return client.execute(request, response -> parseResponse(response, clazz))
    }

    <T> T executeMultipartDataPOST(String path, HttpEntity entity, Class < T > clazz) {
        ClassicHttpRequest request = new HttpPost(this.resolve(path))
        request.setEntity(entity)
        return client.execute(request, response -> parseResponse(response, clazz))
    }

    private <T> T parseResponse(ClassicHttpResponse response, Class<T> clazz) {
        String json = response?.entity?.content?.text ?: EMPTY_JSON

        if (response.code !in [HttpStatus.SC_OK, HttpStatus.SC_ACCEPTED]) {
            throw new ServerException(response.code, json)
        }

        return this.serializationService.fromJson(json, clazz)
    }

    private URI resolve(String path) {
        return new URIBuilder(this.options.baseUrl).setPath(path).build()
    }
}
