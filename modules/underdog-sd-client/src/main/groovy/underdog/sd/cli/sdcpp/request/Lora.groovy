package underdog.sd.cli.sdcpp.request

import com.fasterxml.jackson.annotation.JsonProperty

class Lora {
    @JsonProperty("name")
    String name

    @JsonProperty("path")
    String path
}
