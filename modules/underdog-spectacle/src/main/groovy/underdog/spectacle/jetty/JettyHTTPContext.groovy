package underdog.spectacle.jetty

import groovy.transform.TupleConstructor
import org.eclipse.jetty.server.Request

@TupleConstructor
class JettyHTTPContext extends JettyContext {
    Request request
    Map<String, ?> configuration

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
}
