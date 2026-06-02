package underdog.sd.cli.sdapi

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.builder.Builder
import underdog.sd.cli.Txt2ImageBaseOptions

@Builder(includeSuperProperties = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class Txt2ImageOptions extends Txt2ImageBaseOptions {
    @JsonProperty(value  = "init_images", defaultValue = "[]")
    List<String> initImages = []

    @JsonProperty(value = "restore_faces", defaultValue = "false")
    Boolean restoreFaces = false

    @JsonProperty(value = "alwayson_scripts")
    Map<String, Object> alwaysonScripts = [:]
}