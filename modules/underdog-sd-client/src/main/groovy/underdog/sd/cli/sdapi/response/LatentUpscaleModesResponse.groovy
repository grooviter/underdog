package underdog.sd.cli.sdapi.response

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includes = ["name"])
class LatentUpscaleModesResponse {
    @JsonProperty("name")
    String name
}
