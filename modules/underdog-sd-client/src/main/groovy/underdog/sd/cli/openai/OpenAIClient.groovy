package underdog.sd.cli.openai

interface OpenAIClient {

    ImagesResult imageGeneration(GenerationsOptions options)

    ModelsResult models()

    ImagesResult edits(EditsOptions options)
}