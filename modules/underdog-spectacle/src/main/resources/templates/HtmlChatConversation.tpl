def messages = element.value?.messages ?: []

div(id: element.name){
    messages.each { message ->
        if (message.type.toString() == 'SYSTEM') {
            div(class: "d-flex flex-row-reverse mb-3") {
                span(class: "p-2 border border-secondary rounded") {
                    yieldUnescaped message.message
                }
            }
        } else {
            div(class: "d-flex flex-row mb-3") {
                span(class: "p-2 bg-primary text-white rounded") {
                    yieldUnescaped message.message
                }
            }
        }
    }
}
