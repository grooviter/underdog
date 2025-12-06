package templates

def event = element.onClick
def icon = element.icon

/**
 * Base attributes
 */
def basicButtonAttributes = [
    'class': element.classNames('btn btn-outline-primary mt-1'),
    'id': element.name,
    'name': element.name
]

if (event) {
    if (event.isStreaming()){
        /**
         * Attributes for html element when using WS
         *
         * NOTE: WS areas should be rendered by HtmlPage processing
         * as they must enclosed all swapped elements. The form
         * or input types triggering sending information to the
         * WS only should be marked as 'ws-send'
         *
         */
        def asyncAttributes = [
            'ws-send': true
        ]

        button(basicButtonAttributes + asyncAttributes) {
            if (icon) {
                i(class: "icon $icon"){}
            }
            input(type: 'hidden', name: 'message', value: 'ws-trigger'){}
            yield element.text
        }
    } else {
        /**
         * Attributes for html element when using HTTP
         */
        def syncAttributes = [
            'hx-post': event.path,
            'hx-trigger': "click",
            'hx-target': "#${event.outputList.find()}",
            'hx-swap': 'outerHTML'
        ]

        button(basicButtonAttributes + syncAttributes) {
            if (icon) {
                i(class: "icon $icon"){}
            }
            yield element.text
        }
    }
} else {
    button(basicButtonAttributes) {
        if (icon) {
            i(class: "icon $icon"){}
        }
        yield element.text
    }
}
