package underdog.sd.cli

import groovy.transform.builder.Builder

import java.time.Duration

@Builder
class ApiOptions {
    String baseUrl

    String apiKey

    Duration timeout
}
