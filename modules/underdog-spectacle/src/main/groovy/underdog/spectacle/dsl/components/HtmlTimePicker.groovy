package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlElement

import java.time.LocalDateTime

class HtmlTimePicker extends HtmlElement<LocalDateTime> {
    LocalDateTime from
    LocalDateTime to
}
