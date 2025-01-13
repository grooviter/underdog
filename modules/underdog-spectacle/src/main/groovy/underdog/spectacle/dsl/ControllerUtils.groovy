package underdog.spectacle.dsl

class ControllerUtils {
    String resource(String path) {
        return this.class.classLoader.getResource(path).file
    }
}
