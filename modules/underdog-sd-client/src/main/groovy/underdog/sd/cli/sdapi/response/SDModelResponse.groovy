package underdog.sd.cli.sdapi.response

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString
class SDModelResponse {

  @JsonProperty("title")
  String title

  @JsonProperty("model_name")
  String modelName

  @JsonProperty("hash")
  String hash

  @JsonProperty("sha256")
  String sha256

  @JsonProperty("filename")
  String filename

  @JsonProperty("config")
  String config
}