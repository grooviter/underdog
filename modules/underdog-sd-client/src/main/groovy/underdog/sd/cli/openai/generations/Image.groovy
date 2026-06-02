package underdog.sd.cli.openai.generations

import com.fasterxml.jackson.annotation.JsonProperty

class Image {
    @JsonProperty("b64_json")
    String b64Json

    @JsonProperty("revised_prompt")
    String revisedPrompt

    @JsonProperty("url")
    String url
}
