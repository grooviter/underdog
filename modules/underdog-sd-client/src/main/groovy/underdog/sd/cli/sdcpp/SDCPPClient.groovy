package underdog.sd.cli.sdcpp

interface SDCPPClient {

    /**
     * @return
     * @since 0.1.0
     */
    CapabilitiesResult getCapabilities()

    /**
     * @param jobID
     * @return
     * @since 0.1.0
     */
    JobStatusResult getJob(String jobID)

    /**
     * @param jobID
     * @return
     * @since 0.1.0
     */
    JobCancellationResult cancelJob(String jobID)

    /**
     * @param options
     * @return
     * @since 0.1.0
     */
    JobExecutionResult imgGen(ImageGenerationOptions options)

    /**
     * @param options
     * @return
     * @since 0.1.0
     */
    JobExecutionResult vidGen(VideoGenerationOptions options)
}