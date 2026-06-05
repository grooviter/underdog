package underdog.sd.cli.openai

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.builder.Builder
import underdog.sd.cli.Request
import underdog.sd.cli.openai.generations.Style

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class GenerationsRequest implements Request {
    @JsonProperty("prompt")
    String prompt

    @JsonProperty("background")
    Background background

    @JsonProperty("quality")
    Quality quality

    @JsonProperty("model")
    String model

    @JsonProperty("moderation")
    Moderation moderation

    @JsonProperty("n")
    Integer n

    @JsonProperty("output_compression")
    Float outputCompression

    @JsonProperty("output_format")
    OutputFormat outputFormat

    @JsonProperty("size")
    String size

    @JsonProperty("style")
    Style style
}
