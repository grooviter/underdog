package underdog.sd.cli.openai

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.builder.Builder
import underdog.sd.cli.Options
import underdog.sd.cli.openai.edits.Image

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class EditsOptions implements Options {
    @JsonProperty("images")
    List<Image> images

    @JsonProperty("prompt")
    String prompt

    @JsonProperty("background")
    Background background

    @JsonProperty("mask")
    Image mask

    @JsonProperty("model")
    String model

    @JsonProperty("moderation")
    Moderation moderation

    @JsonProperty("n")
    Integer n

    @JsonProperty("output_format")
    OutputFormat outputFormat

    @JsonProperty("partial_images")
    Integer partialImages

    @JsonProperty("input_fidelity")
    Quality quality

    @JsonProperty("size")
    String size
}
