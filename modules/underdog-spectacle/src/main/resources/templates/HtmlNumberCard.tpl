def delta = element.value?.delta
def value = element.value?.value

if (element.hasParentClass("card-body") || element.isParentType('HtmlCardBody')) {
    div(id: element.name, class: element.class, 'hx-swap-oob': 'true') {
        div(class: 'd-flex align-items-center') {
            div(class: 'subheader') {
                yield element.title
            }
        }
        div(class: 'd-flex align-items-baseline') {
            div(class: 'h1 mb-0 me-2') {
                yield value
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
            div(class: 'd-flex align-items-baseline justify-content-between'){
                div(class: 'h1 mb-0 me-2') {
                    if (element.value != null){
                        yield("${value} ${element.symbol}")
                    } else {
                        yield("- ${element.symbol}")
                    }
                }
                if (delta || delta == 0) {
                    def trendIcon = delta > 0
                        ? 'bi-arrow-up-right'
                        : delta == 0 ? 'bi-arrow-right-short' : 'bi-arrow-down-right'
                    def trendColor = delta > 0
                        ? 'text-green'
                        : delta == 0 ? 'text-warning' : 'text-danger'

                    span(class: "${trendColor} d-inline-flex align-items-center lh-1") {
                        yield "${delta} ${element.deltaSymbol ?: ''}"
                        if (element.deltaSymbol) {
                            i(class: "icon icon-sm ps-1 bi ${trendIcon}") {}
                        }
                    }
                }
            }
        }
    }
}