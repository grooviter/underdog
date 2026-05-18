def value = element.value?.value
def symbol = element?.symbol ?: ''
def delta = element.value?.delta
def deltaSymbol = element?.deltaSymbol ?: ''
def text = element.value?.text ?: '-'

div(class: 'card card-sm') {
  div(class: 'card-body') {
    div(class: 'row align-items-center') {
      div(class: 'col-auto') {
        span(class: "${delta > 0 ? 'bg-success' : 'bg-danger'}-lt avatar") {
           i(class: "icon bi ${delta > 0 ? 'bi-arrow-up' : 'bi-arrow-down'}") {}
        }
      }
      div(class: 'col') {
          div(class: 'font-weight-medium') {
              yield "${value} ${symbol} "
              span(class: "float-right font-weight-medium ${delta > 0 ? 'text-green' : 'text-red'}") {
                  yield "${delta}${deltaSymbol}"
              }
          }
          div(class: 'text-muted') {
              yield "${text}"
          }
      }
    }
  }
}