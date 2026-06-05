package underdog.sd.cli.sdcpp.domain

import com.fasterxml.jackson.annotation.JsonProperty

class OutputFormatsByMode {

    @JsonProperty("img_gen")
    List<String> imgGen
}
