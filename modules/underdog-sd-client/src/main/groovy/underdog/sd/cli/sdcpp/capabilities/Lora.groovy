package underdog.sd.cli.sdcpp.capabilities

import com.fasterxml.jackson.annotation.JsonProperty

class Lora {
    @JsonProperty("name")
    String name

    @JsonProperty("path")
    String path
}
