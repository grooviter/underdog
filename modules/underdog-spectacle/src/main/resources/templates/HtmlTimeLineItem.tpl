li {
  div(class: "list-timeline-icon ${element.iconBackground ?: 'bg-primary'}") {
     if (element.icon) {
         i(class: "icon bi ${element.icon}") {}
     }
     if (!element.icon && element.iconText) {
         span(class: "icon d-flex align-items-center justify-content-center") {
             yield "${element.iconText}"
         }
     }
  }
  div(class: "list-timeline-content") {
    if (element.when) {
        div(class: "list-timeline-time") {
            yield "${element.when}"
        }
    }
    p(class: "list-timeline-title") {
        yield "${element.title}"
    }
    p(class: "text-muted") {
        yield "${element.description}"
    }
  }
}