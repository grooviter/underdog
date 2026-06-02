package underdog.sd.cli.openai

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

class ModelsResult {

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Model {
        @JsonProperty("id")
        String id

        @JsonProperty("object")
        String object

        @JsonProperty("model")
        String model

        @JsonProperty("created")
        Long created

        @JsonProperty("owned_by")
        String ownedBy

        @JsonProperty("root")
        String root

        @JsonProperty
        String parent

        @JsonProperty("max_model_length")
        Integer maxModelLength

        @JsonProperty("permission")
        List<Map> permission
    }

    @JsonProperty("object")
    String object

    @JsonProperty('data')
    List<Model> data
}
