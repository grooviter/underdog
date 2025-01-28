package underdog.guide.spectacle

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.ai.openai.api.OpenAiApi
import spock.lang.IgnoreIf
import spock.lang.Specification
import underdog.spectacle.Spectacle
import underdog.spectacle.dsl.Context

class SpringAISpec extends Specification {

    @IgnoreIf({ !System.getenv("OPENAI_API_KEY") })
    void "simple chat client"() {
        setup:
        def app = Spectacle.application {
            page('/spring') {
                markdown """\
                | # Spectacle + Spring AI
                |
                | This example uses [Spectacle](https://grooviter.github.io/underdog/spectacle/) as UI 
                | and [Spring AI](https://docs.spring.io/spring-ai/reference/index.html) as backend service to use OpenAI
                |
                | ## Instructions
                |
                | Just insert a question and click **"Run"**. You can use one of the **examples** below.
                """
                spec {
                    inputs {
                        text(name: field.question, label: 'Question?', info: 'Ask anything to OpenAI', required: true)
                    }
                    outputs {
                        markdown()
                    }
                    examples = [
                        [question: "Can you give me recipe for lasagna ?"],
                        [question: "Is Madrid in Spain normally cold during winter ?"],
                        [question: "Tell me a joke"]
                    ]
                    onSubmit { Context context ->
                        String openAIKey = System.getenv("OPENAI_API_KEY")
                        String model = context.configuration.openai_model
                        OpenAiChatOptions options = OpenAiChatOptions.builder().model(model).build()
                        return ChatClient.builder(new OpenAiChatModel(new OpenAiApi(openAIKey), options))
                            .build()
                            .prompt()
                            .user(context.param(field.question))
                            .call()
                            .content()
                    }
                }
            }
        }
        expect:
        app.launch()
    }
}
