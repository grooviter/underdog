package underdog.guide.spectacle

import spock.lang.Specification
import underdog.spectacle.Spectacle
import underdog.spectacle.dsl.Context
import underdog.spectacle.dsl.components.HtmlChat

class ChatBotSpec extends Specification {
    def "create a chat bot"() {
        setup:
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
                         | When getting the input in the **onSend** function we receive the message introduced by
                         | the user and the `Context` object in case we'd like to get information about 
                         | the application or the current page.
                         | 
                         | The **onSend** method must return the answer (SYSTEM) to the user entry (USER).
                         |
                         | Although the chat component has its own history by default, you can provide your
                         | own history implementation (database, file...etc) creation your own implementation
                         | of the `HtmlChatHistory` interface.
                         |
                         | ### Want to know more ?
                         |
                         | Checkout [Underdog's documentation](https://grooviter.github.io/underdog)
                        """
                    }
                    col {
                        // --8<-- [start:chat]
                        chat(
                            name: field.chat,                         // chat element name
                            title: "Chat Room Title",                 // chat title
                            inputLabel: "User",                       // chat input label
                            inputPlaceholder: "Write anything here!", // chat input placeholder
                         // history: new HtmlChat.SimpleChatHistory() // chat history implementation (optional)
                        ) {
                            onSend { String userMessage, Context context ->
                                Thread.sleep(2_000)
                                return "$userMessage  (echo)"
                            }
                        }
                        // --8<-- [end:chat]
                    }
                }
            }
        }
        expect:
        application
        // application.launch()
    }
}
