package underdog.sd.cli.sdapi.response;

import com.fasterxml.jackson.annotation.JsonProperty
import underdog.sd.cli.sdapi.request.Image2ImageRequest

class Image2ImageResponse {

  @JsonProperty("images")
  List<String> images

  @JsonProperty("parameters")
  Image2ImageRequest parameters

  @JsonProperty("info")
  String info
}
