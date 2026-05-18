html {
    head {
        title(element.title ?: 'Spectacle')
        link(href: '/static/css/tabler.css', rel: 'stylesheet')
        link(href: '/static/css/spectacle.css', rel: 'stylesheet')
        link(href: '/static/icons/font/bootstrap-icons.css', rel: 'stylesheet')
    }
    body(class: "theme-${element.theme}") {
        div(class: 'page') {
            if (navigation) {
                yieldUnescaped navigation
            }
            div(class: 'page-wrapper') {
                if (element.title) {
                    div(class: 'page-header') {
                        div(class: 'container-xl') {
                            div(class: 'row') {
                                div(class: 'col') {
                                    if (element.preTitle) {
                                        div(class: 'page-pretitle') {
                                            yield element.preTitle
                                        }
                                    }
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
        script(src: '/static/js/bootstrap.js'){}
        script(src: '/static/js/htmx.min.js'){}
        script(src: '/static/js/htmx-ws.js'){}
        script(type: "module", src: '/static/js/spc-form.js'){}
        if (element.isDevelopment()) {
            script(type: "module", src: '/static/js/spc-dev.js'){}
        }
    }
}
