package underdog.sd.cli

import spock.lang.TempDir
import underdog.sd.cli.common.SDAwareSpec
import underdog.sd.cli.sdapi.request.Image2ImageRequest
import underdog.sd.cli.sdapi.response.Image2ImageResponse
import underdog.sd.cli.sdapi.response.LatentUpscaleModesResponse
import underdog.sd.cli.sdapi.response.LoraResponse
import underdog.sd.cli.sdapi.response.SDAPIOptionsResponse
import underdog.sd.cli.sdapi.response.SDModelResponse
import underdog.sd.cli.sdapi.response.SamplersResponse
import underdog.sd.cli.sdapi.response.SchedulersResponse
import underdog.sd.cli.sdapi.request.Txt2ImageRequest
import underdog.sd.cli.sdapi.response.Txt2ImgResponse
import underdog.sd.cli.sdapi.response.UpscalersResponse

import java.nio.file.Path

class SDAPIClientSpec extends SDAwareSpec {
    def "/sdapi/v1/sd-models"() {
        when:
        List<SDModelResponse> availableModels = sdapi.availableModels

        then:
        availableModels.size() == 1
    }

    def "/sdapi/v1/loras"() {
        when:
        List<LoraResponse> loras = sdapi.loras

        then:
        loras.size() >= 0
    }

    def "/sdapi/v1/upscalers"() {
        when:
        List<UpscalersResponse> upscalers = sdapi.upscalers

        then:
        upscalers.size() == 3
    }

    def "/sdapi/v1/latent-upscale-modes"() {
        when:
        List<LatentUpscaleModesResponse> upscaleModesResults = sdapi.latentUpscaleModes

        then:
        upscaleModesResults.size() == 6
    }

    def "/sdapi/v1/samplers"() {
        when:
        List<SamplersResponse> samplers = sdapi.samplers

        then:
        samplers.size() == 19
    }

    def "/sdapi/v1/schedulers"() {
        when:
        List<SchedulersResponse> schedulers = sdapi.schedulers

        then:
        schedulers.size() == 13
    }

    def "/sdapi/v1/options"() {
        when:
        SDAPIOptionsResponse optionsResult = sdapi.options

        then:
        optionsResult.sdModelCheckpoint

        and:
        optionsResult.samplesFormat
    }

    def "/sdapi/v1/txt2img"(@TempDir File imageDir) {
        when:
        Txt2ImageRequest options = Txt2ImageRequest.builder()
            .prompt("A dog")
            .height(393)
            .width(333)
            .cfgScale(4)
            .steps(12)
            .denoisingStrength(1)
            .build()

        and:
        Txt2ImgResponse result = sdapi.txt2Img(options)
        File image = new File(imageDir, "image.png")
        image << result.images[0].decodeBase64()

        then:
        result.images.size() == 1
        image.exists()
    }

    def "/sdapi/v1/img2img (init_images)"(@TempDir File imageDir) {
        setup:
        String prompt = """\
        | highly realistic restored color photograph of a man with a 
        | tribal tattoo in half of his face, natural skin tones, historically 
        | accurate colors, detailed, subtle film grain
        """

        String negativePrompt = """\
        | black and white, grayscale, oversaturated, cartoon, distorted, 
        | blurry, unrealistic colors
        """

        when:
        Image2ImageRequest options = Image2ImageRequest.builder()
            .initImages([alfredHitchcockBase64Image])
            .prompt(prompt.stripMargin().stripIndent())
            .negativePrompt(negativePrompt.stripMargin().stripIndent())
            .height(512)
            .width(512)
            .seed(200_000)
            .cfgScale(4.5)
            .samplerName("DPM++ 2M Karras")
            .denoisingStrength(0.28)
            .build()

        and:
        Image2ImageResponse result = sdapi.img2img(options)
        File image = new File(imageDir, "image.png")
        image << result.images[0].decodeBase64()

        then:
        image.exists()
        result.images.size() == 1
    }

    def '/sdapi/v1/img2img (init_images + mask)'(@TempDir Path outputPath) {
        setup:
        String prompt = """\
        | A roundabout surrounded by a continuos urban european park. The filled-in area should match 
        | the perspective, lighting, and camera angle of the original photograph. Create a realistic park 
        | environment with grass, small trees, shrubs, and paved pedestrian paths that seamlessly connect 
        | with the existing surroundings of the roundabout. There must be no buildings in the roundabout.
        | Maintain coherent shadows and lighting direction so the result looks physically plausible and unedited. 
        | Ensure there are no traces, outlines, or artifacts of the removed monument. 
        | The final image should appear as an uninterrupted, naturally designed circular park integrated 
        | into the city environment.
        """.stripMargin().stripIndent()

        String negativePrompt = """\
        | monument, statue, sculpture, memorial, obelisk, column, fountain centerpiece, architectural centerpiece, 
        | landmark structure, ruins, pedestal, pedestal base, structure remnants, carved stone, historical figure, 
        | text, inscriptions, plaques, signage, people, vehicles overlapping the center, distorted perspective, 
        | warped geometry, blurry textures, low detail, smudges, ghosting, double objects, duplicate structures, 
        | artificial symmetry, floating objects, unrealistic shadows, mismatched lighting, inconsistent scale, 
        | artifacts, noise, overexposed, underexposed
        """.stripMargin().stripIndent()

        when:
        Image2ImageRequest options = Image2ImageRequest.builder()
            .initImages([puertaDeAlcalaBase64Image])
            .mask(puertaDeAlcalaMaskBase64Image)
            .seed(2000)
            .cfgScale(cfgScale)
            .denoisingStrength(denoising)
            .steps(steps)
            .width(1024)
            .height(768)
            .samplerName(samplers)
            .prompt(prompt)
            .negativePrompt(negativePrompt)
            .build()


        and:
        Image2ImageResponse result = sdapi.img2img(options)
        File destination = new File(outputPath.toFile(),"ds-${denoising}-cfg-${cfgScale}-sampler-${samplers}-steps-${steps}.png")
        Images.base64ToFile(result.images[0], destination)

        then:
        result.images

        where:
        denoising | cfgScale | samplers    | steps
        0.9       |    1.0   |  "euler_a"  | 20
        1.0       |    1.0   |  "euler_a"  | 20
        0.9       |    1.0   |  "lcm"      | 8
        1.0       |    1.0   |  "lcm"      | 8
    }

    def '/sdapi/v1/txt2img (rescaling)'() {
        setup:
        String prompt = """\
        | highly realistic photograph of a man
        """.stripMargin().stripIndent()

        String negativePrompt = """\
        | black and white, grayscale, oversaturated, cartoon, distorted, 
        | blurry, unrealistic colors
        """.stripMargin().stripIndent()

        and:
        def (srcWidth, srcHeight) = Images.getDimensions(
                Images.base64ToTempFile(puertaDeAlcalaBase64Image))

        when:
        Image2ImageRequest options = Image2ImageRequest.builder()
            .initImages([puertaDeAlcalaBase64Image])
            .prompt(prompt)
            .negativePrompt(negativePrompt)
            .denoisingStrength(0.1)
            .cfgScale(7)
            .width(1024)
            .height(768)
            .samplerName("euler_a")
            .steps(20)
            .build()

        and:
        Image2ImageResponse response = sdapi.img2img(options)
        def (dstWidth, dstHeight) = Images.getDimensions(Images.base64ToTempFile(response.images[0]))

        then:
        dstWidth == 1024
        dstHeight == 768

        and:
        srcWidth > dstWidth
        srcHeight > dstHeight
    }
}
