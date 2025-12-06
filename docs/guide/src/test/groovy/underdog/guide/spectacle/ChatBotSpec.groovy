package underdog.guide.spectacle

import spock.lang.Specification
import underdog.spectacle.Spectacle
import underdog.spectacle.dsl.Context

class ChatBotSpec extends Specification {
    def "create a chat bot"() {
        setup:
        def messages = []
        def application = Spectacle.application {
            page("/chat") {
                row {
                    col {
                        markdown """\
                         | ## Spectacle Chat                         
                         | ### About the example
                         |
                         | In this example each time you enter a new message in the input field the system will
                         | respond back with an echo response after a couple of seconds.
                         |
                         | ### Some theory
                         |
                         | This example shows the behavior of Spectacle's HtmlChat component.
                         |
                         | When getting the input in the **onSend** function we can use the util static function
                         | `HtmlChat.getChatInputName(chatName)` to get the name of the input field and therefore
                         | getting the input field value via the Context object.
                         | 
                         | The **onSend** method must return a list of `HtmlChat.HtmlChatMessage` instances. Instances
                         | of this class can be of type SYSTEM or type USER to distinguish visually whether the
                         | message has been created by a user or by the system.
                         |
                         | There are _utility static_ functions to create such messages:
                         |
                         | - `HtmlChat.HtmlChatMessage.createUserMessage(text)`
                         | - `HtmlChat.HtmlChatMessage.createSystemMessage(text)`
                         |
                         | ### Want to know more ?
                         |
                         | Checkout [Underdog's documentation](https://grooviter.github.io/underdog)
                        """
                    }
                    col {
                        // --8<-- [start:chat]
                        chat(
                            name: field.chat,                        // chat element name
                            title: "Chat Room Title",                // chat title
                            inputLabel: "User",                      // chat input label
                            inputPlaceholder: "Write anything here!" // chat input placeholder
                        ) {
                            onSend { Context context -> // backend function
                                def message = context.param(getChatInputName(field.chat))
                                Thread.sleep(2_000)
                                messages << createUserMessage(message)
                                messages << createSystemMessage("$message (echo)")
                                return messages
                            }
                        }
                        // --8<-- [end:chat]
                    }
                }
            }
        }
        expect:
        application
    }
}
