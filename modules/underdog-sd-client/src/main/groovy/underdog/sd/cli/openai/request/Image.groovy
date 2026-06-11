package underdog.sd.cli.openai.request

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.builder.Builder

@Builder
class Image {
    @JsonProperty("file_id")
    String fileID

    @JsonProperty("image_url")
    String imageURL
}
