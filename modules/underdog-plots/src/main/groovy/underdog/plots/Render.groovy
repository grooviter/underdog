package underdog.plots

import groovy.text.StreamingTemplateEngine
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

    String render(Options options, String path = temporalFile, String height = "100%", String width = "100%") {
        String html = compileTemplate(TEMPLATE_PATH, options, height, width)
        if (desktopSupported) {
            writeHtml(html, path)
            desktop.browse(new File(path).toURI())
        }
        return html
    }

    private static String getTemporalFile() {
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
     * @param height the height of the chart, ends with "px" or "%"
     * @param width the width of the chart, ends with "px" or "%"
     * @return HTML in String. Empty string when an exception is occurred.
     */
    private static String compileTemplate(String templateName, Options options, String height, String width) {
        try {
            URL templateURL = Render.getResource(templateName)
            Map templateParams = [meta: [height: height, width: width], option: options.toJson(true)]
            String templateOutput = new StreamingTemplateEngine().createTemplate(templateURL).make(templateParams)

            return templateOutput
                .replaceAll("\"${FUNCTION_START}", EMPTY_CONTENT)
                .replaceAll("${FUNCTION_ENDS}\"", EMPTY_CONTENT)
        } catch (IOException e) {
            log.error(e.message)
        }
        return EMPTY_CONTENT
    }
}
