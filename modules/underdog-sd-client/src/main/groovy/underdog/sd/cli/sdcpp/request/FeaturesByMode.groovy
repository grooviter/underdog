package underdog.sd.cli.sdcpp.request

import com.fasterxml.jackson.annotation.JsonProperty

class FeaturesByMode {
    @JsonProperty("img_gen")
    Features imgGen
}
