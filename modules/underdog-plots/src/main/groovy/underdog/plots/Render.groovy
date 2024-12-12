package underdog.plots

import groovy.text.StreamingTemplateEngine
import groovy.transform.builder.Builder
import groovy.util.logging.Slf4j
import java.nio.file.Files

import static underdog.plots.dsl.NodeMap.FUNCTION_ENDS
import static underdog.plots.dsl.NodeMap.FUNCTION_START
import static java.awt.Desktop.desktop
import static java.awt.Desktop.desktopSupported

@Slf4j
class Render {
    private static final String TEMPLATE_PATH = '/templates/index.html'
    private static final String EMPTY_CONTENT = ''

    @Builder()
    static class Meta {
        String path, height, width, theme
    }

    String render(Options options, Meta meta = defaultMeta) {
        String html = compileTemplate(TEMPLATE_PATH, options, meta)
        String path = meta.path ?: temporalFilePath
        if (desktopSupported) {
            writeHtml(html, path)
            desktop.browse(new File(path).toURI())
        }
        return html
    }

    private static Meta getDefaultMeta() {
        return Meta.builder().height("600px").width("800px").build()
    }

    private static String getTemporalFilePath() {
        return Files.createTempFile("underdog-plot-", ".html").toAbsolutePath()
    }

    /**
     * Generate Html file according to the specified path
     *
     * @param html a String representing in html format
     * @param path path to save the html file
     */
    private static void writeHtml(String html, String path) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(html)
        } catch (IOException e) {
            log.info("Write Html failed: ${e.message}");
        }
    }

    /**
     * Compile HandleBar template into HTML String
     *
     * @param templateName name of the template selected
     * @param option the option used to init the chart
     * @param meta
     * @return HTML in String. Empty string when an exception is occurred.
     */
    private static String compileTemplate(String templateName, Options options, Meta meta) {
        try {
            Map templateParams = [
                meta: [
                    height: meta.height,
                    width: meta.width,
                    theme: "'${meta.theme}'"
                ],
                option: options.toJson(true)
            ]

            URL templateURL = Render.getResource(templateName)
            String templateOutput = new StreamingTemplateEngine().createTemplate(templateURL).make(templateParams)

            return templateOutput
                .replaceAll("\"${FUNCTION_START}", EMPTY_CONTENT)
                .replaceAll("${FUNCTION_ENDS}\"", EMPTY_CONTENT)
                .replaceAll("\\\\\"", '"')
        } catch (IOException e) {
            log.error(e.message)
        }
        return EMPTY_CONTENT
    }
}
