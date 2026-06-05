package underdog.sd.cli.sdcpp.request

import com.fasterxml.jackson.annotation.JsonProperty

class Model {
    @JsonProperty("name")
    String name

    @JsonProperty("path")
    String path

    @JsonProperty("stem")
    String stem
}
