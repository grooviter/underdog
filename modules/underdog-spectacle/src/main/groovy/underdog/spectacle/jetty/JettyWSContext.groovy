package underdog.spectacle.jetty

import groovy.json.JsonSlurper
import groovy.transform.TupleConstructor
import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.ResourceHandler
import underdog.spectacle.http.HttpClient

@TupleConstructor
class JettyWSContext extends JettyContext {
    String message
    HtmlApplication application

    @Override
    String param(String fieldName, String defaultValue) {
        return this.params.get(fieldName, defaultValue)
    }

    private Map getParams() {
        return new JsonSlurper().parseText(this.message) as Map
    }

    @Override
    Double paramDouble(String fieldName, Double defaultValue) {
        return this.params.get(fieldName, defaultValue).toDouble()
    }

    @Override
    Integer paramInteger(String fieldName, Integer defaultValue) {
        return this.params.get(fieldName, defaultValue).toInteger()
    }

    @Override
    Double pDouble(String fieldName, Double defaultValue) {
        return this.paramDouble(fieldName, defaultValue)
    }

    @Override
    Integer pInteger(String fieldName, Integer defaultValue) {
        return this.paramInteger(fieldName, defaultValue)
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
