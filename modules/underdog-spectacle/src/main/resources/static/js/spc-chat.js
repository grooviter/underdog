
/**
 * Initializes HtmlChat behavior
 */
class HtmlChatInitializer {
    /**
     * Adds ids from the:
     *
     * - input field
     * - conversation div
     * - card body div which has the scroll
     */
    constructor(options) {
        this.inputId = options.inputId;
        this.conversationId = options.conversationId;
        this.bodyId = options.bodyId;
    }

    /**
     * Binds HTMX events with this class functions
     */
    init() {
        document.body.addEventListener('htmx:afterSwap', (evt) => { this.cleanupInput() });
        document.body.addEventListener('htmx:beforeRequest', (evt) => { this.addNewUserMessage() });
        document.body.addEventListener('htmx:afterSettle', (evt) => { this.updateConversationScroll(); });
    }

    /**
     * Adds the new user message to the conversation panel and also a busy indicator while backend is
     * not responding
     */
    addNewUserMessage() {
        const messageText = this.getInputElement().value;
        const message = this.buildUserMessage(messageText);
        const container = this.getConversationElement();
        container.innerHTML = container.innerHTML + message;
        this.updateConversationScroll();
    }

    /**
     * Builds the new user message and busy indicator elements
     */
    buildUserMessage(text) {
        return `
            <div class="d-flex flex-row mb-3">
                <span class="p-2 bg-primary text-white rounded">
                    ${text}
                </span>
            </div>
            <div class="d-flex flex-row-reverse mb-3">
                <span class="p-2 ps-3 border border-secondary rounded animated-dots"> </span>
            </div>
        `;
    }

    /**
     * Sets the current user view at the end of the conversation
     */
    updateConversationScroll() {
        const body = this.getBodyElement();
        body.scrollTop = body.scrollHeight;
    }

    /**
     * Cleans up the input value whenever the response has been received successfully
     */
    cleanupInput() {
        const input = this.getInputElement()
        input.value = '';
        input.focus();
    }

    /**
     * Returns the Html element with the input id
     */
    getInputElement() {
        return document.getElementsByName(this.inputId)[0];
    }

    /**
     * Returns the Html element with the conversation id
     */
    getConversationElement() {
        return document.getElementById(this.conversationId);
    }

    /**
     * Returns the html element with the body id
     */
    getBodyElement() {
        return document.getElementById(this.bodyId);
    }
};

export { HtmlChatInitializer };