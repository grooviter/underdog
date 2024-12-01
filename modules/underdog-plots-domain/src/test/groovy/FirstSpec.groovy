import underdog.plots.Options
import spock.lang.Specification

class FirstSpec extends Specification {

    def "something"() {
        when:
        def options = Options.create {
            title {
                text "texting"
                textStyle {
                    color "#333"
                }
            }

            xAxis {
                offset 10
            }
        }

        then:
        with(options.map) {
            title.text == 'texting'
            title.textStyle.color == '#333'
            xAxis.offset
        }

    }
}
