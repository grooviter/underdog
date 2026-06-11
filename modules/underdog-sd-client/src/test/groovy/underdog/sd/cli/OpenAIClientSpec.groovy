package underdog.sd.cli

import spock.lang.Shared
import spock.lang.TempDir
import underdog.sd.cli.common.SDAwareSpec
import underdog.sd.cli.openai.request.EditsRequest
import underdog.sd.cli.openai.request.GenerationsRequest
import underdog.sd.cli.openai.response.ImagesResponse
import underdog.sd.cli.openai.response.ModelsResponse
import underdog.sd.cli.openai.request.Moderation
import underdog.sd.cli.openai.Quality
import underdog.sd.cli.openai.request.Image
import underdog.sd.cli.openai.request.Style

class OpenAIClientSpec extends SDAwareSpec {
    @Shared
    @TempDir
    File imagesDir

    def '/v1/models'() {
        when:
        ModelsResponse result = openAI.models()

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
        GenerationsRequest options = GenerationsRequest.builder()
            .prompt(prompt)
            .size("256x256")
            .model("z-image-turbo")
            .moderation(Moderation.low)
            .style(Style.natural)
            .n(1)
            .quality(Quality.hd)
            .build()
        when:
        ImagesResponse result = openAI.imageGeneration(options)
        File image = new File(imagesDir, "dog.png")
        image << result.data.b64Json[0].decodeBase64()

        then:
        result
        result.data.size() == 1
        result.data.every { it.b64Json }

        and:
        image.exists()
    }

    def '/v1/images/edits'() {
        setup:
        String prompt = """\
        | highly realistic restored color photograph of a man with a 
        | tribal tattoo in half of his face, natural skin tones, historically 
        | accurate colors, detailed, subtle film grain
        """.stripIndent().stripMargin()

        and:
        EditsRequest options = EditsRequest.builder()
            .prompt(prompt)
            .size("512x512")
            .model("z-image-turbo")
            .images([Image.builder().imageURL(alfredHitchcockBase64Image).build()])
            .build()

        when:
        ImagesResponse result = openAI.edits(options)
        File image = new File(imagesDir, "hitchcock_edited.png")
        image << result.data.b64Json[0].decodeBase64()

        then:
        result
        result.data.size() == 1
        result.data.every { it.b64Json }
    }
}
