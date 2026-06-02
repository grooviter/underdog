package underdog.sd.cli.sdcpp.capabilities

import com.fasterxml.jackson.annotation.JsonProperty

class Slug {
    @JsonProperty("layer_end")
    Double layerEnd

    @JsonProperty("layer_start")
    Double layerStart

    @JsonProperty("layers")
    List<Integer> layers

    @JsonProperty("scale")
    Double scale
}
