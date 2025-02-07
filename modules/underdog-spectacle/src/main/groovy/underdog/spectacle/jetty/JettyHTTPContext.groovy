package underdog.spectacle.jetty

import groovy.transform.TupleConstructor
import org.eclipse.jetty.server.Request
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.ResourceHandler
import underdog.spectacle.http.HttpClient

@TupleConstructor
class JettyHTTPContext extends JettyContext {
    Request request
    HtmlApplication application

    @Override
    String param(String fieldName, String defaultValue) {
        return Request.getParameters(request).getValue(fieldName) ?: defaultValue
    }

    @Override
    Double paramDouble(String fieldName, Double defaultValue) {
        return param(fieldName)?.toDouble() ?: defaultValue
    }

    @Override
    Integer paramInteger(String fieldName, Integer defaultValue) {
        return param(fieldName)?.toInteger() ?: defaultValue
    }

    @Override
    Double pDouble(String fieldName, Double defaultValue) {
        return paramDouble(fieldName, defaultValue)
    }

    @Override
    Integer pInteger(String fieldName, Integer defaultValue) {
        return paramInteger(fieldName, defaultValue)
    }

    @Override
    Map<String, ?> getConfiguration() {
        return this.application.configuration
    }

    @Override
    ResourceHandler resources(String name) {
        return this.application.findResourceHandlerByName(name)
    }

    @Override
    HttpClient getHttpClient() {
        return HttpClient.createClient(this.application)
    }
}
