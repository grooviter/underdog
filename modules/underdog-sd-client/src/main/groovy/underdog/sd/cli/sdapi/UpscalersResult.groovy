package underdog.sd.cli.sdapi

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includes = ["name"])
class UpscalersResult {
    @JsonProperty("name")
    String name

    @JsonProperty("model_name")
    String modelName

    @JsonProperty("model_path")
    String modelPath

    @JsonProperty("model_url")
    String modelURL

    @JsonProperty("scale")
    Double scale
}
