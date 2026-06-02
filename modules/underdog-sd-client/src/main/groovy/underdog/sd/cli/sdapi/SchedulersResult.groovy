package underdog.sd.cli.sdapi

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includes = ["name"])
class SchedulersResult {
    @JsonProperty("name")
    String name

    @JsonProperty("label")
    String label
}
