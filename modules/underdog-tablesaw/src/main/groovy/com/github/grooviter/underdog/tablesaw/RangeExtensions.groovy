package com.github.grooviter.underdog.tablesaw

import groovy.text.SimpleTemplateEngine
import groovy.text.Template

class RangeExtensions {
    static List<String> rightShiftUnsigned(IntRange source, String template) {
        Template compiledTemplate = new SimpleTemplateEngine().createTemplate(template)
        return source.collect { compiledTemplate.make(it: it).toString() }
    }
}
