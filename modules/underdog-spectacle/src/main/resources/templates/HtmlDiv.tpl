def attributes = [
    'name': element.name,
    'class': element.className,
] + element.extraAttributes

div(attributes) {
    yieldUnescaped childrenContent
}