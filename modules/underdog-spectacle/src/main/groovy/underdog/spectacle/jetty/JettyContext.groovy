package underdog.spectacle.jetty

import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.Response
import underdog.spectacle.dsl.Context

class JettyContext extends Context {
    Request request
    Response response
    Map<String, ?> configuration

    JettyContext(
        Request request,
        Response response,
        Map<String,?> configuration
    ) {
        this.request = request
        this.response = response
        this.configuration = configuration
    }

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
    URL resource(String path) {
        return this.class.classLoader.getResource(path)
    }
}
