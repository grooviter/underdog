package underdog.spectacle

import groovy.transform.NamedParam
import groovy.transform.NamedVariant
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
     * @param theme default theme for all pages
     * @return an instance of {@link Application}
     * @since 0.1.0
     */
    @NamedVariant
    static Application application(
        @NamedParam(required = false) String theme = 'light',
        @NamedParam(required = false) boolean showNavigation = true,
        @DelegatesTo(HtmlApplication) Closure dsl
    ) {
        HtmlApplication htmlApplication = new HtmlApplication(
            configuration: Utils.loadConfiguration(),
            defaultTheme: theme,
            showNavigation: showNavigation
        )
        return new JettyApplication(htmlApplication.tap { with(dsl) })
    }
}
