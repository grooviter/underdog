package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlElementWithValue

import java.time.LocalDateTime

class HtmlTimePicker extends HtmlElementWithValue<LocalDateTime> {
    LocalDateTime from
    LocalDateTime to
}
