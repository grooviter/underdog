package underdog.sd.cli.sdapi

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includes = ["name"])
class LatentUpscaleModesResult {
    @JsonProperty("name")
    String name
}
