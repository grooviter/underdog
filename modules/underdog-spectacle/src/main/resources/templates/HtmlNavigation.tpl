package templates

div(class: "navbar-expand-md") {
    div(class: "collapse navbar-collapse", id:"navbar-menu") {
        div(class: "navbar navbar-light") {
            div(class: 'container-xl'){
                ul(class: 'navbar-nav') {
                    element.applicationPageList.each { page ->
                        li(class: "nav-item ${element.currentPage?.path == page?.path ? 'active' : ''}") {
                            a(class: 'nav-link', href:"${page.path}"){
                                if (page.icon){
                                    i(class: "icon $page.icon me-2"){}
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