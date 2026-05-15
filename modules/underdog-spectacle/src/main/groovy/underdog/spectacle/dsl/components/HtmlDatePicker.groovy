package underdog.spectacle.dsl.components

import underdog.spectacle.dsl.HtmlElement

import java.time.LocalDate

class HtmlDatePicker extends HtmlElement<LocalDate> {
    LocalDate from
    LocalDate to
}
