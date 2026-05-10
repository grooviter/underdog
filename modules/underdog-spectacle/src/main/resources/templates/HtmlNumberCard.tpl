if (element.hasParentClass("card-body") || element.isParentType('HtmlCardBody')) {
    div(id: element.name, class: element.class, 'hx-swap-oob': 'true') {
        div(class: 'd-flex align-items-center') {
            div(class: 'subheader') {
                yield element.title
            }
        }
        div(class: 'd-flex align-items-baseline') {
            div(class: 'h1 mb-0 me-2') {
                yield element.value
            }
        }
    }
} else {
    div(id: element.name, class: element.classNames('card'), 'hx-swap-oob': 'true') {
        div(class: 'card-body') {
            div(class: 'd-flex align-items-center'){
                div(class: 'subheader'){
                    yield element.title
                }
            }
            div(class: 'd-flex align-items-baseline'){
                div(class: 'h1 mb-0 me-2') {
                    if (element.value != null){
                        yield("${element.value} ${element.symbol}")
                    } else {
                        yield("- ${element.symbol}")
                    }
                }
            }
        }
    }
}