package underdog.spectacle

class SpectacleApplication {
    static void main(String[] args) {
        def app = Spectacle.application(theme: 'dark') {
            page(new VehicleEmissionPage())
            page(new AboutPage())
        }
        app.dev()
    }
}