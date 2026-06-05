package underdog.sd.cli.sdapi

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

interface SDAPIClient {

    /**
     * Generate images from other images
     *
     * @param request The options to generate images
     * @return The result of the generation
     * @since 0.1.0
     */
    Image2ImageResponse img2img(Image2ImageRequest request)

    /**
     * Generate images from text
     *
     * @param request The options to generate images.
     * @return The result of the generation.
     * @since 0.1.0
     */
    Txt2ImgResponse txt2Img(Txt2ImageRequest request)

    /**
     * @return
     * @since 0.1.0
     */
    List<SDModelResponse> getAvailableModels()

    /**
     * @return
     * @since 0.1.0
     */
    List<LoraResponse> getLoras()

    /**
     * @return
     * @since 0.1.0
     */
    List<UpscalersResponse> getUpscalers()

    /**
     * @return
     * @since 0.1.0
     */
    List<LatentUpscaleModesResponse> getLatentUpscaleModes()

    /**
     * @return
     * @since 0.1.0
     */
    List<SamplersResponse> getSamplers()

    /**
     * @return
     * @since 0.1.0
     */
    List<SchedulersResponse> getSchedulers()

    /**
     * @return
     * @since 0.1.0
     */
    SDAPIOptionsResponse getOptions()
}
