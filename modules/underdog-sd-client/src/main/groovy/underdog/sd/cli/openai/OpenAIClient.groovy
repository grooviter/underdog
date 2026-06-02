package underdog.sd.cli.openai

interface OpenAIClient {

    ImagesResult imageGeneration(GenerationsOptions options)
}