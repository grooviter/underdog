html('data-bs-theme': element.theme) {
    head {
        title('Title')
        link(href: 'static/css/bootstrap.css', rel: 'stylesheet')
        link(href: 'static/css/spectacle.css', rel: 'stylesheet')
        script(src: 'static/js/spc-utils.js'){}
        script(src: 'static/js/htmx.min.js'){}

        if (element.isDevelopment()) {
            script(src: 'static/js/spc-dev.js'){}
        }

    }
    body {
        div(class: 'container-fluid px-4 py-2') {
            yieldUnescaped childrenContent
        }
    }
}