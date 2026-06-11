package underdog.guide.sd

import spock.lang.Ignore
import spock.lang.IgnoreIf
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.TempDir
import underdog.sd.cli.SD
import underdog.sd.cli.openai.request.EditsRequest
import underdog.sd.cli.openai.request.GenerationsRequest
import underdog.sd.cli.openai.response.ImagesResponse

@IgnoreIf( { !System.getenv('OPENAI_API_BASE_URL') })
class OpenAISpec extends Specification {
    @Shared
    @TempDir
    File openAIGuideDir

    @Shared
    File images = new File('src/test/resources/underdog/sd/openai')

    @Ignore
    def "openai: getting started"() {
        expect:
        // --8<-- [start:getting_started]
        // generating an image
        ImagesResponse response = SD.openai()
            .imageGeneration(GenerationsRequest.builder()
            .prompt('A cat having a coffee at the coffee shop, manga style')
            .model('z-image-turbo')
            .size("256x256")
            .build())

        // images are returned as base64 strings so we may want to store them in disk
        File imageFile = new File(openAIGuideDir, "cat.png")
        imageFile << response.data.b64Json[0].decodeBase64()
        // --8<-- [end:getting_started]
        imageFile.exists()
    }

    @Ignore
    def "edit (init image)"() {
        expect:
        // --8<-- [start:edit]
        File hitchcock = new File(images, 'hitchcock.jpg')

        String prompt = """\
        | highly realistic restored color photograph of a man with a 
        | tribal tattoo in half of his face, natural skin tones, historically 
        | accurate colors, detailed, subtle film grain
        """.asPrompt()

        ImagesResponse response = SD.openai().edits(EditsRequest.builder()
            .images([hitchcock.toImage()])
            .prompt(prompt)
            .size("512x512")
            .model('z-image-turbo')
            .build())

        // --8<-- [end:edit]
        File imageFile = new File(openAIGuideDir, "hitchcock_edited.png")
        imageFile << response.data.b64Json[0].decodeBase64()
        imageFile.exists()
    }

    def "edit (with mask)"() {
        expect:
        // --8<-- [start:edit_mask]
        String prompt = """\
        | highly realistic restored grayscale photograph of a person 
        | chasing the only race car in the picture. 
        | Natural tones, historically accurate colors, detailed, subtle film grain
        """.asPrompt()

        File originalImage = new File(images, 'wikipedia.jpg')
        File maskImage = new File(images, 'wikipedia_mask.jpg')

        ImagesResponse response = SD.openai().edits(EditsRequest.builder()
            .images([originalImage.toImage()])
            .mask(maskImage.toImage())
            .prompt(prompt)
            .size("512x337")
            .model('z-image-turbo')
            .build())

        // --8<-- [end:edit_mask]
        File imageFile = new File(openAIGuideDir, "wikipedia_edited.png")
        imageFile << response.data.b64Json[0].decodeBase64()
        imageFile.exists()
    }
}
