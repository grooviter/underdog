class HtmlFormStreaming {
    init() {
        document.body.addEventListener("htmx:wsBeforeSend", (evt) => { this.onBeforeSendingMessages() })
        document.body.addEventListener("htmx:wsClose", (evt) => { this.onClosingConnection() })
        document.body.addEventListener("htmx:wsError", (evt) => { this.onError() })
    }

    getIndicators() {
        const elt = document.querySelector("form");
        return document.querySelectorAll(elt.getAttribute("hx-indicator"));
    }

    onBeforeSendingMessages() {
        this.getIndicators().forEach(elt => htmx.addClass(elt, htmx.config.requestClass));
    }

    onClosingConnection() {
        this.getIndicators().forEach(elt => htmx.removeClass(elt, htmx.config.requestClass));
    }

    onError() {
        this.getIndicators().forEach(elt => htmx.removeClass(elt, htmx.config.requestClass));
    }
}

export { HtmlFormStreaming };