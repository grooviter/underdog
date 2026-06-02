package underdog.sd.cli.sdapi

import com.fasterxml.jackson.annotation.JsonProperty

class LoraResult {
    @JsonProperty("name")
    String name

    @JsonProperty("path")
    String path
}
