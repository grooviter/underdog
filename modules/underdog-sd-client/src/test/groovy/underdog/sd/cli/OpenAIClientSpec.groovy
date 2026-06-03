package underdog.sd.cli

import underdog.sd.cli.common.SDAwareSpec
import underdog.sd.cli.openai.EditsOptions
import underdog.sd.cli.openai.GenerationsOptions
import underdog.sd.cli.openai.ImagesResult
import underdog.sd.cli.openai.ModelsResult
import underdog.sd.cli.openai.Moderation
import underdog.sd.cli.openai.Quality
import underdog.sd.cli.openai.edits.Image
import underdog.sd.cli.openai.generations.Style

class OpenAIClientSpec extends SDAwareSpec {

    def '/v1/models'() {
        when:
        ModelsResult result = openAI.models()

        then:
        result.data.size() > 0
        result.data.every {it.id }
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

    def '/v1/images/edits'() {
        setup:
        String prompt = """\
        | highly realistic restored color photograph of a man with a 
        | tribal tattoo in half of his face, natural skin tones, historically 
        | accurate colors, detailed, subtle film grain
        """.stripIndent().stripMargin()

        and:
        EditsOptions options = EditsOptions.builder()
            .prompt(prompt)
            .size("512x512")
            .model("z-image-turbo")
            .images([Image.builder().imageURL(alfredHitchcockBase64Image).build()])
            .build()

        when:
        ImagesResult result = openAI.edits(options)

        then:
        result
        result.data.size() == 1
        result.data.every { it.b64Json }
    }
}
