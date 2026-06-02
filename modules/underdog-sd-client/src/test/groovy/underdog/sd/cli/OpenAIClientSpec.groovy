package underdog.sd.cli

import underdog.sd.cli.common.SDAwareSpec
import underdog.sd.cli.openai.GenerationsOptions
import underdog.sd.cli.openai.ImagesResult
import underdog.sd.cli.openai.ModelsResult
import underdog.sd.cli.openai.generations.Moderation
import underdog.sd.cli.openai.generations.Quality
import underdog.sd.cli.openai.generations.Style

class OpenAIClientSpec extends SDAwareSpec {

    def '/v1/models'() {
        when:
        ModelsResult result = openAI.models()

        then:
        result.data.size() == 1
    }

    def '/v1/images/generations'() {
        setup:
        String prompt = """\
        | A realistic photograph of a dog at a beach bar, natural highlights and shadows, 
        | true-to-life complexion, editorial photography, RAW photo, high-end camera capture.
        """

        and:
        GenerationsOptions options = GenerationsOptions.builder()
            .prompt(prompt)
            .size("256x256")
            .model("z-image-turbo")
            .moderation(Moderation.low)
            .style(Style.natural)
            .n(1)
            .quality(Quality.hd)
            .build()
        when:
        ImagesResult result = openAI.imageGeneration(options)

        then:
        result
        result.data.size() == 1
        result.data.every { it.b64Json }
    }
}
