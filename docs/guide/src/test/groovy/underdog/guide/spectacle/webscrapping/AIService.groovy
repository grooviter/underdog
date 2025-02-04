package underdog.guide.spectacle.webscrapping

import io.github.ollama4j.OllamaAPI
import io.github.ollama4j.models.chat.OllamaChatMessageRole
import io.github.ollama4j.models.chat.OllamaChatRequest
import io.github.ollama4j.models.chat.OllamaChatRequestBuilder
import io.github.ollama4j.models.chat.OllamaChatResult

class AIService {
    static OLLAMA_TIMEOUT_SECONDS = 60
    static OLLAMA_MODEL = 'llama3.2'
    static OLLAMA_SYSTEM_PROMPT = """\
    | You are an assistant that analyzes the contents of a website
    | and provides a short summary, ignoring text that might be navigation related.
    | Respond in markdown.
    """.stripMargin().stripIndent()

    @SuppressWarnings('all')
    String extractSummary(String pageTitle, String pageContent) {
        OllamaAPI aiAPI = new OllamaAPI(System.getenv("OLLAMA_HOST"))
        aiAPI.setRequestTimeoutSeconds(OLLAMA_TIMEOUT_SECONDS)

        OllamaChatRequest request = OllamaChatRequestBuilder.getInstance(OLLAMA_MODEL)
            .withMessage(OllamaChatMessageRole.SYSTEM, OLLAMA_SYSTEM_PROMPT)
            .withMessage(OllamaChatMessageRole.USER, buildUserPrompt(pageTitle, pageContent))
            .build()

        OllamaChatResult result = aiAPI.chat(request)

        return result.responseModel.message.content
    }

    private static String buildUserPrompt(String websiteTitle, String pageContent) {
        return """\
        | You are looking at a web site called $websiteTitle
        | The contents of this website is as follows;
        | please provide a short summary of this website in markdown.
        | If it includes news or announcements, then summarize these too.
        |
        | $pageContent
        """.stripMargin().stripIndent()
    }
}
