package underdog.sd.cli.http

import com.fasterxml.jackson.annotation.JsonProperty

class ServerValidationException extends ServerException {

  @JsonProperty("detail")
  Detail[] detail
    
  static class Detail {

    @JsonProperty("loc")
    String[] loc

    @JsonProperty("msg")
    String msg

    @JsonProperty("type")
    String type
  }
}
