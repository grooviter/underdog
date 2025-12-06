package underdog.spectacle.jetty

import underdog.spectacle.dsl.Context

abstract class JettyContext extends Context {
    @Override
    URL resource(String path) {
        return this.class.classLoader.getResource(path)
    }
}
