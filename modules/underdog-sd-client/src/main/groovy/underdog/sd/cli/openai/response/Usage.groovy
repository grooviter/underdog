package underdog.sd.cli.openai.response

import com.fasterxml.jackson.annotation.JsonProperty

class Usage {

    static class Details {
        @JsonProperty("image_tokens")
        Integer imageTokens

        @JsonProperty("text_tokens")
        Integer textTokens
    }

    @JsonProperty("input_tokens")
    Integer inputTokens

    @JsonProperty("output_tokens")
    Integer outputTokens

    @JsonProperty("total_tokens")
    Integer totalTokens

    @JsonProperty("input_tokens_details")
    Details inputTokensDetails

    @JsonProperty("output_tokens_details")
    Details outputTokensDetails
}
