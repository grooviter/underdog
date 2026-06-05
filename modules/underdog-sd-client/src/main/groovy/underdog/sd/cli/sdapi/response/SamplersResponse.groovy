package underdog.sd.cli.sdapi.response

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includes = ["name"])
class SamplersResponse {
    @JsonProperty("aliases")
    List<String> aliases

    @JsonProperty("name")
    String name

    @JsonProperty("options")
    Map<String, ?> options
}
