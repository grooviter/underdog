div(class: 'card-header') {
    if (element.title || element.description) {
        div {
            if (element.title) {
                h3(class: 'card-title') {
                    yield element.title
                }
            }
            if (element.description) {
                p(class: 'card-subtitle') {
                    yield element.description
                }
            }
        }
    }
    yieldUnescaped childrenContent
}