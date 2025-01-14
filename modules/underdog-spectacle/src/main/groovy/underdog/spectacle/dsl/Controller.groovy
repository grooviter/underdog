package underdog.spectacle.dsl

class Controller {
    String path
    String name = Utils.generateRandomName()
    String method
    Closure function
}
