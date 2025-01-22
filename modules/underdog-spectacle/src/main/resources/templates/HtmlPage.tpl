html {
    head {
        title(element.title ?: 'Spectacle')
        link(href: 'static/css/tabler.css', rel: 'stylesheet')
        link(href: 'static/css/spectacle.css', rel: 'stylesheet')
        script(src: 'static/js/spc-utils.js'){}
        script(src: 'static/js/htmx.min.js'){}

        if (element.isDevelopment()) {
            script(src: 'static/js/spc-dev.js'){}
        }

    }
    body(class: "theme-${element.theme}") {
        div(class: 'page') {
            div(class: 'page-wrapper') {
                if (element.title) {
                    div(class: 'page-header') {
                        div(class: 'container-xl') {
                            div(class: 'row') {
                                div(class: 'col') {
                                    h2(class: 'page-title') { yield element.title }
                                }
                            }
                        }
                    }
                }
                div(class: 'page-body') {
                    div(class: 'container-xl') {
                        if (element.children) {
                            yieldUnescaped childrenContent
                        } else {
                            include template: 'templates/HtmlPageEmpty.tpl'
                        }

                    }
                }
            }
        }
    }
}
