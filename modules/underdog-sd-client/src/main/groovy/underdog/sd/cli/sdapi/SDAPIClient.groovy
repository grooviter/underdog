package underdog.sd.cli.sdapi

interface SDAPIClient {

    /**
     * Generate images from other images
     *
     * @param options The options to generate images
     * @return The result of the generation
     * @since 0.1.0
     */
    Image2ImageResult img2img(Image2ImageOptions options)

    /**
     * Generate images from text
     *
     * @param options The options to generate images.
     * @return The result of the generation.
     * @since 0.1.0
     */
    Txt2ImgResult txt2Img(Txt2ImageOptions options)

    /**
     * @return
     * @since 0.1.0
     */
    List<SDModelResult> getAvailableModels()

    /**
     * @return
     * @since 0.1.0
     */
    List<LoraResult> getLoras()

    /**
     * @return
     * @since 0.1.0
     */
    List<UpscalersResult> getUpscalers()

    /**
     * @return
     * @since 0.1.0
     */
    List<LatentUpscaleModesResult> getLatentUpscaleModes()

    /**
     * @return
     * @since 0.1.0
     */
    List<SamplersResult> getSamplers()

    /**
     * @return
     * @since 0.1.0
     */
    List<SchedulersResult> getSchedulers()

    /**
     * @return
     * @since 0.1.0
     */
    SDAPIOptionsResult getOptions()
}
