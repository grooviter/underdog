def valueFieldId = "${element.name}-value"
def fileFieldName = "${element.name}-file"
def imageElementId = "${element.name}-image"
def imageElementWrapperId = "${imageElementId}-wrapper"
def hasElement = element.value ? true : false

div(id: element.name, class: 'mb-3', 'hx-swap-oob': 'true') {
    if (element.label) {
        label(
            class: "form-label ${element.required ? 'required' : ''}",
            for: element.label
        ) {
            yield element.label
        }
    }
    if (element.info) {
        small(class: 'form-hint mb-2') {
            yield element.info
        }
    }

    div(id: "${imageElementWrapperId}", class: 'card') {
        div(
            class: "card-body d-flex justify-content-center ${hasElement ? '' : 'd-none'}"
        ) {
            img(
                id: imageElementId,
                src: element.value,
                class: element.className
            ){}
        }
        div(
            class: "empty ${hasElement ? 'd-none' : ''}",
        ) {
            div(class: 'empty-image') {
                img(class: 'w-50', src: '/static/images/empty_image.svg'){}
            }
            p(class: 'empty-title'){ yield 'Image' }
            p(class: 'empty-subtitle text-muted') {
                yield 'This component will show a picture as soon as it gets one'
            }
        }
    }
    input(
        id: "${fileFieldName}",
        name: "${fileFieldName}",
        type: "file",
        class: "d-none"
    ) {}
    input(
        id: "${valueFieldId}",
        name: "${element.name}",
        type: "hidden"
    ) {}
    script(type: 'module') {
        yieldUnescaped """\
            | import { ImageBase64Loader } from "/static/js/spc-image.js";
            |
            | new ImageBase64Loader().init(
            |    "${element.name}",
            |    '${fileFieldName}',
            |    '${imageElementId}',
            |    '${valueFieldId}',
            |    '${imageElementWrapperId}'
            |);
        """.stripMargin().stripIndent()
    }
}