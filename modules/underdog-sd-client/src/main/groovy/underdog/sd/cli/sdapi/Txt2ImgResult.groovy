package underdog.sd.cli.sdapi;

import com.fasterxml.jackson.annotation.JsonProperty

class Txt2ImgResult {
  @JsonProperty("images")
  List<String> images

  @JsonProperty("parameters")
  Txt2ImageOptions parameters

  @JsonProperty("info")
  String info
}
