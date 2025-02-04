package underdog.guide.spectacle

import io.github.ollama4j.OllamaAPI
import io.github.ollama4j.models.chat.OllamaChatRequest
import io.github.ollama4j.models.chat.OllamaChatRequestBuilder
import underdog.spectacle.dsl.Context

import static io.github.ollama4j.models.chat.OllamaChatMessageRole.SYSTEM
import static io.github.ollama4j.models.chat.OllamaChatMessageRole.USER

@SuppressWarnings('all')
class CodeTranslatorService {
    private static final OLLAMA_TIMEOUT_SECONDS = 60
    private static final OLLAMA_MODEL = 'qwen2.5-coder:7b'
    private static final OLLAMA_DEFAULT_HOST = 'http://localhost:11434'

    String translate(Context context) {
        String code = context.param('java')
        String host = System.getenv("OLLAMA_HOST") ?: OLLAMA_DEFAULT_HOST
        OllamaAPI aiAPI = new OllamaAPI(host).tap {
            setRequestTimeoutSeconds(OLLAMA_TIMEOUT_SECONDS)
        }
        OllamaChatRequest request = OllamaChatRequestBuilder.getInstance(OLLAMA_MODEL)
            .withMessage(SYSTEM, buildSystemPrompt())
            .withMessage(USER, buildUserPrompt(code))
            .build()
        return aiAPI.chat(request).responseModel.message.content
    }

    private static String buildSystemPrompt() {
        return this.getResource("/underdog/spectacle/CodeTranslatorSpec.prompt.system.txt").text
    }

    private static String buildUserPrompt(String javaCode) {
        return """\
        | I need you to translate the following java code to Groovy. Here is the code 
        | $javaCode
        """.stripMargin().stripIndent()
    }
}
