package underdog.sd.cli.openai.response

import com.fasterxml.jackson.annotation.JsonProperty
import underdog.sd.cli.openai.Background
import underdog.sd.cli.openai.OutputFormat
import underdog.sd.cli.openai.Quality

class ImagesResponse {
    static class Image {
        @JsonProperty("b64_json")
        String b64Json

        @JsonProperty("revised_prompt")
        String revisedPrompt

        @JsonProperty("url")
        String url
    }

    @JsonProperty("created")
    Long created

    @JsonProperty("background")
    Background background

    @JsonProperty("data")
    List<Image> data

    @JsonProperty('output_format')
    OutputFormat outputFormat

    @JsonProperty("quality")
    Quality quality

    @JsonProperty("size")
    String size

    @JsonProperty("usage")
    Usage usage
}
