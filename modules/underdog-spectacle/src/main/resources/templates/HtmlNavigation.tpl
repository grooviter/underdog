package templates

div(class: "navbar-expand-md") {
    div(class: "collapse navbar-collapse", id:"navbar-menu") {
        div(class: "navbar navbar-light") {
            div(class: 'container-xl'){
                ul(class: 'navbar-nav') {
                    element.topElements.each { topElement ->
                       if (topElement.class.simpleName == 'HtmlPage') {
                            li(class: "nav-item ${element.currentPage?.path == topElement?.path ? 'active' : ''}") {
                               a(class: 'nav-link', href:"${topElement.path}"){
                                   if (topElement.icon){
                                       i(class: "icon ${topElement.icon} me-2"){}
                                   }
                                   yield topElement.title ?: 'topElement.title?'
                               }
                            }
                       } else if (topElement.class.simpleName == 'HtmlNavigationGroup') {
                            li(class: "nav-item ${element.currentPage?.path in topElement?.included?.path ? 'active' : ''} dropdown") {
                                a(
                                    'class': 'nav-link dropdown-toggle',
                                    'href': "#navbar-${topElement.title.toLowerCase()}",
                                    'data-bs-toggle': "dropdown",
                                    'data-bs-auto-close': "outside",
                                    'aria-expanded': 'false',
                                    'role': "button"
                                ) {
                                    if (topElement.icon) {
                                       i(class: "icon ${topElement.icon} me-2"){}
                                    }
                                    span(class: 'nav-link-title') {
                                        yield topElement.title
                                    }
                                }
                                div(class: "dropdown-menu") {
                                    topElement.included.each { page ->
                                        a(href:"${page.path}", class: "dropdown-item ${element.currentPage?.path == page?.path ? 'active' : ''}") {
                                           if (page.icon){
                                               span(class: 'nav-link-icon d-md-none d-lg-inline-block') {
                                                   i(class: "icon ${page.icon} me-2"){}
                                               }
                                           }
                                           yield page.title ?: 'page.title?'
                                        }
                                    }
                                }
                            }
                       }
                    }
                }
            }
        }
    }
}