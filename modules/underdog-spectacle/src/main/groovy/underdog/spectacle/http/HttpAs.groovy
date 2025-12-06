package underdog.spectacle.http

import groovy.transform.TupleConstructor

import java.net.http.HttpRequest

/**
 * All possible responses must have in common
 *
 * - {@link HttpClient}
 * - {@link HttpRequest}
 *
 * @since 0.1.0
 */
@TupleConstructor
abstract class HttpAs {
    HttpClient httpClient
    HttpRequest httpRequest
}
