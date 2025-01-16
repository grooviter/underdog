// html('data-bs-theme': "dark") {
html {
    head {
        title('Title')
        link(href: 'static/css/bootstrap.css', rel: 'stylesheet')
        link(href: 'static/css/spectacle.css', rel: 'stylesheet')
        script(src: 'static/js/utils.js'){}
        script(src: 'static/js/htmx.min.js'){}
    }
    body {
        div(class: 'container-fluid px-4 py-2') {
            yieldUnescaped childrenContent
        }

    }
}