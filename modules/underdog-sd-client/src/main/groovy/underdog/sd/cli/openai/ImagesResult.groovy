package underdog.sd.cli.openai

import com.fasterxml.jackson.annotation.JsonProperty
import underdog.sd.cli.openai.generations.Image
import underdog.sd.cli.openai.generations.Usage

class ImagesResult {
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
