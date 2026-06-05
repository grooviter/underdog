package underdog.sd.cli.sdapi.response;

import com.fasterxml.jackson.annotation.JsonProperty
import underdog.sd.cli.sdapi.request.Txt2ImageRequest

class Txt2ImgResponse {
  @JsonProperty("images")
  List<String> images

  @JsonProperty("parameters")
  Txt2ImageRequest parameters

  @JsonProperty("info")
  String info
}
