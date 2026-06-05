package underdog.sd.cli.sdapi.response

import com.fasterxml.jackson.annotation.JsonProperty

class LoraResponse {
    @JsonProperty("name")
    String name

    @JsonProperty("path")
    String path
}
