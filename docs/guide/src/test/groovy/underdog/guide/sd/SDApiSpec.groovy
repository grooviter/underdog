package underdog.guide.sd

import spock.lang.Shared
import spock.lang.Specification
import underdog.sd.cli.ApiOptions
import underdog.sd.cli.SD
import underdog.sd.cli.sdapi.SDAPIClient
import underdog.sd.cli.sdapi.request.HiResUpscaler
import underdog.sd.cli.sdapi.request.Image2ImageRequest
import underdog.sd.cli.sdapi.request.Txt2ImageRequest
import underdog.sd.cli.sdapi.response.Image2ImageResponse
import underdog.sd.cli.sdapi.response.LatentUpscaleModesResponse
import underdog.sd.cli.sdapi.response.LoraResponse
import underdog.sd.cli.sdapi.response.SDAPIOptionsResponse
import underdog.sd.cli.sdapi.response.SDModelResponse
import underdog.sd.cli.sdapi.response.SamplersResponse
import underdog.sd.cli.sdapi.response.SchedulersResponse
import underdog.sd.cli.sdapi.response.Txt2ImgResponse
import underdog.sd.cli.sdapi.response.UpscalersResponse

class SDApiSpec extends Specification {
    @Shared
    File images = new File('src/test/resources/underdog/sd/sdapi')

    @Shared
    SDAPIClient client = SD.sdapi(ApiOptions.builder().baseUrl('https://sdcpp.lab.bit2lab.com').build())

    def "txt2img: simple image generation"() {
        when:
        // --8<-- [start:txt2img]
        String whatToDo = """\
        | Photorealistic group of four friends having coffee in a cozy café, 
        | two women and two men, natural candid conversation, realistic lighting, 
        | high detail
        """.asPrompt()

        String whatToAvoid = """\
        | cartoon, anime, illustration, CGI, 3D render, painting, blurry, low quality, 
        | distorted faces, extra limbs, extra fingers, duplicate people, cropped, 
        | watermark, text
        |""".asPrompt()

        Txt2ImgResponse response = client.txt2Img(Txt2ImageRequest.builder()
            .prompt(whatToDo)
            .negativePrompt(whatToAvoid)
            .cfgScale(4.5)
            .width(512)
            .height(512)
            .seed(2_000)
            .build())

        File generatedImage = response.images[0].base64ToTempFile()
        // --8<-- [end:txt2img]
        then:
        generatedImage.exists()
    }

    def "txt2img: simple image generation (2)"() {
        when:
        // --8<-- [start:txt2img]
        String whatToDo = """\
        | Photorealistic fighter jet in a blank background, high detail
        """.asPrompt()

        String whatToAvoid = """\
        | cartoon, anime, illustration, CGI, 3D render, painting, blurry, low quality, 
        | distorted faces, extra limbs, cropped, watermark, text
        |""".asPrompt()

        Txt2ImgResponse response = client.txt2Img(Txt2ImageRequest.builder()
            .prompt(whatToDo)
            .negativePrompt(whatToAvoid)
            .cfgScale(4.5)
            .width(512)
            .height(512)
            .seed(2_000)
            .build())

        File generatedImage = response.images[0].base64ToTempFile()
        // --8<-- [end:txt2img]
        then:
        generatedImage.exists()
    }

    def "txt2img: image upscaling"() {
        when:
        // --8<-- [start:upscaling]
        // https://es.wikipedia.org/wiki/Torre_Eiffel

        String prompt = """\
        | Please restore and enhance the quality of this photo. Fix tears, 
        | scratches, discoloration, sharpen the details, and colorize it
        |""".asPrompt()

        Txt2ImgResponse response = client.txt2Img(Txt2ImageRequest.builder()
            .extraImages([new File(images, 'eiffel_tower.jpg').fileToBase64()])
            .prompt(prompt)
            .seed(200_313_002)
            .cfgScale(4.5)
            .width(512)
            .height(384)
            .steps(12)
            .denoisingStrength(0.28)
            .hrUpscaler(HiResUpscaler.Lanczos)
            .hrResizeX(1024)
            .hrResizeY(720)
            .enableHr(true)
        .build())

        File img2ImgFile = response.images[0].base64ToTempFile()
        // --8<-- [end:upscaling]
        then:
        img2ImgFile.exists()
    }

    def 'txt2img: edit image'() {
        when:
        // --8<-- [start:modifying]
        Txt2ImgResponse response = client.txt2Img(Txt2ImageRequest.builder()
            .extraImages([new File(images, 'desert.jpg').fileToBase64()])
            .prompt('Add three real camels to the image')
            .seed(200_313_002)
            .cfgScale(4.5)
            .width(512)
            .height(320)
            .steps(12)
            .build())

        File img2ImgFile = response.images[0].base64ToTempFile()
        // --8<-- [end:modifying]
        then:
        img2ImgFile.exists()
    }

    def 'img2img: combine'() {
        when:
        // --8<-- [start:combination]
        String prompt = """\
        | Combine the jet fighter from image 1 with the desert of image 2. 
        | Create a realistic composite.
        |""".asPrompt()

        Image2ImageRequest request = Image2ImageRequest.builder()
            .extraImages([
                new File(images, 'fighter_jet.png').fileToBase64(),
                new File(images, 'desert.jpg').fileToBase64()
            ])
            .prompt(prompt)
            .seed(200_414_002)
            .cfgScale(4.5)
            .width(1024)
            .height(720)
            .steps(20)
            .build()

        Image2ImageResponse response = client.img2img(request)
        File tempFile = response.images[0].base64ToTempFile()
        // --8<-- [end:combination]
        then:
        tempFile.exists()
    }

    def 'metadata'() {
        when:
        // --8<-- [start:meta]
        // models (in stable-diffusion.cpp normally there is only 1)
        List<SDModelResponse> models = client.getAvailableModels()

        // LoRAs
        List<LoraResponse> loras = client.getLoras()

        // upscalers
        List<UpscalersResponse> upscalers = client.getUpscalers()

        // latest modes
        List<LatentUpscaleModesResponse> modes = client.getLatentUpscaleModes()

        // samplers
        List<SamplersResponse> samplers = client.getSamplers()

        // schedulers
        List<SchedulersResponse> schedulers = client.getSchedulers()

        // other options
        SDAPIOptionsResponse options = client.getOptions()
        // --8<-- [end:meta]
        then: // models
        models.size() > 0
        models[0].title
        models[0].modelName
        models[0].filename
        models[0].hash
        models[0].sha256

        and: // loras
        loras.size() > 0
        loras[0].name
        loras[0].path

        and:
        upscalers

        and:
        modes

        and:
        samplers

        and:
        schedulers

        and:
        options
    }
}
