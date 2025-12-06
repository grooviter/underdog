package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlElementWithValue

import java.time.LocalDate

class HtmlDatePicker extends HtmlElementWithValue<LocalDate> {
    LocalDate from
    LocalDate to
}
