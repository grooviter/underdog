package underdog.sd.cli.sdapi.response

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includes = ["name"])
class SchedulersResponse {
    @JsonProperty("name")
    String name

    @JsonProperty("label")
    String label
}
