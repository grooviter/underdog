package underdog.sd.cli.sdcpp.domain

import com.fasterxml.jackson.annotation.JsonProperty

class Lora {
    @JsonProperty("name")
    String name

    @JsonProperty("path")
    String path
}
