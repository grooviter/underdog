package underdog.spectacle.dsl.components

import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FromString
import underdog.spectacle.dsl.Context
import underdog.spectacle.dsl.HtmlContainer

/**
 * Represents a basic chat room where you can input your message and
 * see the response back.
 *
 * @since 0.1.0
 */
class HtmlChat extends HtmlContainer {

    /**
     * Types of messages
     *
     * @since 0.1.0
     */
    static enum HtmlChatMessageType {
        SYSTEM, USER
    }

    /**
     * {@link HtmlChat} component only can receive messages as instances of {@link HtmlChatMessage} via the
     * chat history
     *
     * @since 0.1.0
     */
    static class HtmlChatMessage {
        HtmlChatMessageType type
        String message

        boolean isSystem() {
            return this.type == HtmlChatMessageType.SYSTEM
        }

        boolean isUser() {
            return this.type == HtmlChatMessageType.USER
        }
    }

    /**
     * Represents all messages sent in a given chat conversation
     *
     * As targets in Spectacle can only receive one value instead of receiving a list of messages the
     * {@link HtmlChatConversation} element receives the history element
     *
     * @since 0.1.0
     */
    static interface HtmlChatHistory {
        /**
         * Adds a given chat message to the current chat history
         *
         * @param message
         * @return the current history instance
         * @since 0.1.0
         */
        HtmlChatHistory add(HtmlChatMessage message)

        /**
         * Adds a given chat message to the current chat history.
         * Created for using Groovy's left shift (<<) operator
         *
         * @param message
         * @return the current history instance
         * @since 0.1.0
         */
        HtmlChatHistory leftShift(HtmlChatMessage message)

        /**
         * Returns all history messages
         *
         * @return the list of all {@link HtmlChatMessage} messages
         * @since 0.1.0
         */
        List<HtmlChatMessage> getMessages()
    }

    /**
     * Default memory implementation of a {@link HtmlChatHistory}
     *
     * @since 0.1.0
     */
    static class SimpleChatHistory implements HtmlChatHistory{
        List<HtmlChatMessage> messages = []

        @Override
        HtmlChatHistory add(HtmlChatMessage message) {
            this.messages.add(message)
            return this
        }

        @Override
        HtmlChatHistory leftShift(HtmlChatMessage message) {
            return this.add(message)
        }
    }

    /**
     * Title of the chat (optional)
     *
     * @since 0.1.0
     */
    String title

    /**
     * Label of the input field of the chat
     *
     * @since 0.1.0
     */
    String inputLabel

    /**
     * Placeholder of the input field of the chat
     *
     * @since 0.1.0
     */
    String inputPlaceHolder

    /**
     * Function triggered when invoking the onSend method
     *
     * @since 0.1.0
     */
    Closure onSendClosure

    /**
     * Messages to show in the chat room
     *
     * @since 0.1.0
     */
    HtmlChatHistory history = new SimpleChatHistory()

    /**
     * Invoked with a closure. That closure functionality will be invoked by the backend
     *
     * @param closure function to be executed in the backend
     * @since 0.1.0
     */
    void onSend(
        @ClosureParams(
            value = FromString,
            options = ["java.lang.String", "underdog.spectacle.dsl.Context"]
        ) Closure closure
    ){
        this.onSendClosure = closure
    }

    /**
     * Initializes how the layout of the component. How nested components are
     * organized
     *
     * @return an instance of {@link HtmlChat}
     * @since 0.1.0
     */
    HtmlChat initLayout() {
        return this.tap {
            form {
                row(className: '+vh-100') {
                    card(className: '+p-0 h-75') {
                        if (title) {
                            cardHeader(title: this.title)
                        }
                        cardBody(name: cardBodyName, className: "+vh-75 overflow-auto"){
                            element(new HtmlChatConversation(name: conversationName))
                        }
                        cardFooter {
                            text(
                                name: chatInputName,
                                label: this.inputLabel,
                                placeHolder: this.inputPlaceHolder,
                                required: true
                            )
                        }
                    }
                }
                onSubmit([chatInputName], [conversationName]) { Context context ->
                    def message = context.param(chatInputName)
                    history << createUserMessage(message)
                    def system = this.onSendClosure.curry(message)(context)
                    history << createSystemMessage(system.toString())
                    return history
                }
            }
        }
    }

    /**
     * Returns the chat input field name given the chat element name
     *
     * @param chatName name of the {@link HtmlChat} element
     * @return the chat input field name
     * @since 0.1.0
     */
    static String getChatInputName(String chatName) {
        return "${chatName}-input"
    }

    /**
     * Returns the conversation html element name given the chat element name
     *
     * @param chatName name of the {@link HtmlChat} element
     * @return the conversation html element name
     * @since 0.1.0
     */
    static String getChatConversationName(String chatName) {
        return "${chatName}-conversation"
    }

    /**
     * Returns the card body html element name given the chat element name
     *
     * @param chatName name of the {@link HtmlChat} element
     * @return the card body element name
     * @since 0.1.0
     */
    static String getChatCardBodyName(String chatName){
        return "${chatName}-card-body"
    }

    /**
     * Returns the chat input field name
     *
     * @return the chat input field name
     * @since 0.1.0
     */
    String getChatInputName() {
        return getChatInputName(this.name)
    }

    /**
     * Returns the conversation html element name
     *
     * @return the conversation html element name
     * @since 0.1.0
     */
    String getConversationName() {
        return getChatConversationName(this.name)
    }

    /**
     * Returns the card body html element name
     *
     * @return the card body html element name
     * @since 0.1.0
     */
    String getCardBodyName() {
        return getChatCardBodyName(this.name)
    }

    /**
     * Creates a new {@link HtmlChatMessage} of type SYSTEM
     *
     * @return an instance of {@link HtmlChatMessage}
     * @since 0.1.0
     */
    static HtmlChatMessage createSystemMessage(String message){
        return new HtmlChatMessage(type: HtmlChatMessageType.SYSTEM, message: message)
    }

    /**
     * Creates a new {@link HtmlChatMessage} of type USER
     *
     * @return an instance of {@link HtmlChatMessage}
     * @since 0.1.0
     */
    static HtmlChatMessage createUserMessage(String message) {
        return new HtmlChatMessage(type: HtmlChatMessageType.USER, message: message)
    }
}
