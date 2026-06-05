package underdog.sd.cli.openai

interface OpenAIClient {

    ImagesResponse imageGeneration(GenerationsRequest request)

    ModelsResponse models()

    ImagesResponse edits(EditsRequest request)
}