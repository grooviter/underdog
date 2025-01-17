package underdog.spectacle

import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.Utils
import underdog.spectacle.jetty.JettyApplication

/**
 * Builds a new Spectable application
 *
 * @since 0.1.0
 */
class Spectacle {

    /**
     * Builds a new Spectacle application
     *
     * @param dsl DSL to build a new {@link Application}
     * @return an instance of {@link Application}
     * @since 0.1.0
     */
    static Application application(@DelegatesTo(HtmlApplication) Closure dsl) {
        HtmlApplication htmlApplication = new HtmlApplication(Utils.loadConfiguration())
        return new JettyApplication(htmlApplication.tap { with(dsl) })
    }
}
