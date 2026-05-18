def title = element?.title ?: '-'
def progressText = element.value?.progressText ?: '-'
def value = element.value?.value ?: 0

if (element.hasParentClass("card-body") || element.isParentType('HtmlCardBody')) {
    div(id: element.name, class: element.class, 'hx-swap-oob': 'true') {
       div(class: 'd-flex align-items-center justify-content-between'){
            div(class: 'font-weight-medium'){
                yield title
            }
            div(class: 'text-muted'){
                yield progressText
            }
        }
        div(class: 'row align-items-baseline mt-0'){
            div(class: 'col-9 pe-0 me-0') {
                div(class: 'progress') {
                    div(class: 'progress-bar', role: 'progressbar', style: "width: ${value.intValue()}%") {}
                }
            }
            span(class: "col d-flex justify-content-end mx-0") {
                yield "${value.round(2)} %"
            }
        }
    }
} else {
    div(id: element.name, class: element.classNames('card'), 'hx-swap-oob': 'true') {
        div(class: 'card-body') {
            div(class: 'd-flex align-items-center justify-content-between'){
                div(class: 'font-weight-medium'){
                    yield title
                }
                div(class: 'text-muted'){
                    yield progressText
                }
            }
            div(class: 'row align-items-baseline mt-0'){
                div(class: 'col-9 pe-0 me-0') {
                    div(class: 'progress') {
                        div(class: 'progress-bar', role: 'progressbar', style: "width: ${value.round()}%") {}
                    }
                }
                span(class: "col d-flex justify-content-end mx-0") {
                    yield "${value.round(2)} %"
                }
            }
        }
    }
}