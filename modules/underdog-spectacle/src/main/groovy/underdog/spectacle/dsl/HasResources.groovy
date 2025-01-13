package underdog.spectacle.dsl

trait HasResources {

    String resource(String path) {
        return this.class.classLoader.getResource(path).toString()
    }
}