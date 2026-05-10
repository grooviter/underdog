package underdog.spectacle.pages

import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlPage

import java.util.function.Function

class AboutPageProvider implements Function<HtmlApplication, HtmlPage> {
    @Override
    HtmlPage apply(HtmlApplication htmlApplication) {
        return HtmlPage.create(
            path: "/about",
            title: "About",
            theme: 'dark',
            application: htmlApplication
        ) {
            row {
                markdown """\
                | ## Overview
                | This is something **important** about the project
                """
            }
        }
    }
}
