def inputId = element.chatInputName
def bodyId = element.cardBodyName
def conversationId = element.conversationName

yieldUnescaped childrenContent

script(type: 'module') {
    yieldUnescaped """\
        | import { HtmlChatInitializer } from "/static/js/spc-chat.js";
        | 
        | new HtmlChatInitializer({
        |    inputId: '$inputId',
        |    bodyId: '$bodyId',
        |    conversationId: '$conversationId'
        | }).init();
    """.stripMargin().stripIndent()
}
