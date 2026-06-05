package underdog.sd.cli.sdcpp.request

import com.fasterxml.jackson.annotation.JsonProperty

class DefaultsByMode {
    @JsonProperty("img_gen")
    Defaults imgGen
}
