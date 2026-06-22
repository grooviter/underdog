class HtmlFormStreaming {
    init() {
        document.body.addEventListener("htmx:wsBeforeSend", (evt) => { this.onBeforeSendingMessages() })
        document.body.addEventListener("htmx:wsClose", (evt) => { this.onClosingConnection() })
        document.body.addEventListener("htmx:wsError", (evt) => { this.onError() })
    }

    getIndicators() {
        const elt = document.querySelector("form");
        const found = elt.getAttribute("hx-indicator");

        if (!found) {
            return [];
        }

        return document.querySelectorAll(found);
    }

    getDisabled() {
        const form = document.querySelector("form");
        const found = form.getAttribute("hx-disabled-elt");

        if (!found) {
            return [];
        }

        return document.querySelectorAll(found)
    }

    onBeforeSendingMessages() {
        this.getIndicators().forEach(elt => htmx.addClass(elt, htmx.config.requestClass));
        this.getDisabled().forEach(elt => elt.setAttribute("disabled", ""));
    }

    onClosingConnection() {
        this.getIndicators().forEach(elt => htmx.removeClass(elt, htmx.config.requestClass));
        this.getDisabled().forEach(elt => elt.removeAttribute("disabled"));
    }

    onError() {
        this.getIndicators().forEach(elt => htmx.removeClass(elt, htmx.config.requestClass));
        this.getDisabled().forEach(elt => elt.removeAttribute("disabled"));
    }
}

export { HtmlFormStreaming };