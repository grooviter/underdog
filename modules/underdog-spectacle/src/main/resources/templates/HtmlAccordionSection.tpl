def elementId = "${element.name}"

div(class: 'accordion-item') {
    h2(class: 'accordion-header') {
        button(
            class: 'accordion-button collapsed',
            type: 'button',
            'data-bs-toggle': 'collapse',
            'data-bs-target': "#${elementId}"
        ) {
            if (element.icon) {
                i(class: "icon ${element.icon}") {}
            }
            yield element.title
        }
    }
    div(
        id: elementId,
        class: 'accordion-collapse collapse',
        'data-bs-parent': "#${element.parent.name}"
    ) {
        div(class: 'accordion-body') {
            yieldUnescaped childrenContent
        }
    }
}