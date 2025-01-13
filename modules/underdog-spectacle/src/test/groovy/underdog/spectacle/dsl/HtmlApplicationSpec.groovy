package underdog.spectacle.dsl

import spock.lang.Specification

class HtmlApplicationSpec extends Specification {
    void 'check dsl'() {
        setup:
        def htmlApp = new HtmlApplication().tap {
            with {
                page('/about') {
                    text(name: 'username')
                    text(name: 'password')
                    button(name: 'submit')
                }
            }
        }

        expect:
        htmlApp.htmlPageList.size() == 1

        and:
        with(htmlApp.htmlPageList[0]) {
            it.children.size() == 3
            it.children[0].name == 'username'
            it.children[1].name == 'password'
            it.children[2].name == 'submit'
        }
    }
}
