def icon = element.value?.icon ?: ''
def iconBackground = element.value?.iconBackground ?: 'bg-primary'
def primaryText = element.value?.primaryText ?: '-'
def secondaryText = element.value?.secondaryText ?: '-'

div(class: 'card card-sm') {
    div(class: 'card-body') {
        div(class: 'row align-items-center') {
            div(class: 'col-auto') {
                span(class: "${iconBackground} text-white avatar") {
                    i(class: "icon ${icon}") {}
                }
            }
            div(class: 'col') {
                div(class: 'font-weight-medium') { yield "${primaryText}" }
                div(class: 'text-muted') { yield "${secondaryText}" }
            }
        }
    }
}