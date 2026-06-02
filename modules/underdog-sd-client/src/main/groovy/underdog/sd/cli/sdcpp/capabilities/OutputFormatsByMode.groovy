package underdog.sd.cli.sdcpp.capabilities

import com.fasterxml.jackson.annotation.JsonProperty

class OutputFormatsByMode {

    @JsonProperty("img_gen")
    List<String> imgGen
}
