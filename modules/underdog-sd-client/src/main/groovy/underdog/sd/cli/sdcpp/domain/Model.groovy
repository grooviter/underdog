package underdog.sd.cli.sdcpp.domain

import com.fasterxml.jackson.annotation.JsonProperty

class Model {
    @JsonProperty("name")
    String name

    @JsonProperty("path")
    String path

    @JsonProperty("stem")
    String stem
}
