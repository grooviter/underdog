package underdog.spectacle.dsl

class Utils {
    static String generateRandomName(String _unused) {
        return new Date().toString().md5()
    }

    static String generateRandomName() {
        return new Date().toString().md5()
    }
}
