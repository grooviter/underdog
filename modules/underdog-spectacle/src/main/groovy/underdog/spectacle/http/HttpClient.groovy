package underdog.spectacle.http

import groovy.transform.TupleConstructor
import underdog.spectacle.dsl.HtmlApplication

import java.net.http.HttpClient as JavaHttpClient
import java.time.Duration
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * A very opinionated HTTP client for a very few use cases. It's based on {@link java.net.http.HttpClient}
 *
 * If basic cases are not enough you can access to the underlying pooled {@link java.net.http.HttpClient}
 * getting the {@link HttpClient#javaHttpClient} property
 *
 * @since 0.1.0
 */
@TupleConstructor
class HttpClient {
    JavaHttpClient javaHttpClient
    HtmlApplication application

    /**
     * Creates a new instance of the {@link HttpClient}
     *
     * @param application
     * @return
     * @since 0.1.0
     */
    static HttpClient createClient(HtmlApplication application) {
        Executor executor = Executors.newFixedThreadPool(10)

        JavaHttpClient underlyingClient = JavaHttpClient.newBuilder()
            .executor(executor)
            .connectTimeout(Duration.ofSeconds(120))
            .build()

        return new HttpClient(underlyingClient, application)
    }

    /**
     * Builds a new HTTP POST request
     *
     * @param uri the {@link URI} to get the data from
     * @return an instance of type {@link HttpMethod}
     * @since 0.1.0
     */
    HttpMethod POST(URI uri) {
        return new HttpPost().client(this).uri(uri)
    }

    /**
     * Builds a new HTTP POST request
     *
     * @param uri a string with a valid URI
     * @return an instance of type {@link HttpMethod}
     * @since 0.1.0
     */
    HttpMethod POST(String uri) {
        return POST(URI.create(uri))
    }

    /**
     * Builds a new HTTP GET request
     *
     * @param uri the {@link URI} to get the data from
     * @return an instance of type {@link HttpMethod}
     * @since 0.1.0
     */
    HttpMethod GET(URI uri) {
        return new HttpGet().client(this).uri(uri)
    }
}
