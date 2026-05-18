package underdog.guide.spectacle.components

import underdog.spectacle.dsl.HtmlApplication
import underdog.spectacle.dsl.HtmlPage

import java.util.function.Function

class LayoutPageProvider implements Function<HtmlApplication, HtmlPage> {
    @Override
    HtmlPage apply(HtmlApplication app) {
        return HtmlPage.create(
            path: "/components/layout",
            title: "Layout",
            preTitle: "Examples",
            application: app,
            icon: 'bi bi-layout-three-columns'
        ) {
            row {
                col {
                    markdown """\
                    | In this section you can find components used for organizing components visually
                    """
                }
            }
            row {
                col {
                    markdown("""\
                    | ## Bootstrap Rows & Cols
                    |
                    | You can make use of `row {}` or `col {}` blocks to arrange components. These work the
                    | same as the Bootstrap layout definitions of rows and cols. Here's an example of using 
                    | 1 row with 3 cols. Each col is spanning 3 cols of 12 total, leaving 3 cols of space at the end.  
                    """.stripMargin().stripIndent())
                }
            }
            row("+bg-gray-300") {
                (0..2).each {
                    col("col-3 bg-gray-500 border-end border-secondary") {
                        markdown(markdown: "col-3")
                    }
                }
            }
            row {
                col {
                    markdown("""\
                    | ## Bootstrap Flex
                    |
                    | Because each `col {}`, `row {}` or `div {}` blocks support 
                    | css classes (e.g `row('d-flex...') {}` you can use css flex in your layouts.
                    """.stripMargin().stripIndent())
                }
            }
            row("+bg-gray-300 text-align-right") {
                div("d-flex justify-content-between p-0") {
                    (0..2).each {
                        div("col-3 bg-gray-500 text-center") {
                            markdown("col-3")
                        }
                    }
                }
            }
        }
    }
}
