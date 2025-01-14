package underdog.spectacle.templates

import groovy.text.Template
import groovy.text.markup.MarkupTemplateEngine
import groovy.text.markup.TemplateConfiguration
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import underdog.spectacle.dsl.HtmlContainer
import underdog.spectacle.dsl.HtmlElement
import underdog.spectacle.dsl.Markdown

class TemplateEngine {

    String render(HtmlContainer container) {
        String childrenContent = container.children
            .collect { render(it) }
            .join("\n")

        return executeTemplate(container.class.simpleName, [element: container, childrenContent: childrenContent])
    }

    String render(HtmlElement element) {
        return executeTemplate(element.class.simpleName, [element: element])
    }

    String render(Markdown markdown) {
        Parser parser = Parser.builder().build()
        Node document = parser.parse(markdown.markdown.stripMargin().stripIndent())
        HtmlRenderer renderer = HtmlRenderer.builder().build()
        return renderer.render(document)
    }

    String executeTemplate(String templateName, Map<String,Object> model) {
        TemplateConfiguration config = new TemplateConfiguration();
        MarkupTemplateEngine engine = new MarkupTemplateEngine(config);
        URL templatePath = this.class.classLoader.getResource("templates/${templateName}.tpl")
        Template template = engine.createTemplate(templatePath)
        Writable output = template.make(model);
        StringWriter writer = new StringWriter() // TODO: use response writer
        output.writeTo(writer);
        return writer.toString()
    }
}
