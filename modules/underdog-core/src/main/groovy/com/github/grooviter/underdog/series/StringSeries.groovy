package com.github.grooviter.underdog.series

import com.github.grooviter.underdog.Series

interface StringSeries extends Series {

    StringSeries matches(String regex)
    StringSeries replace(String regex, String to)
    StringSeries replace(Map<String, String> fromTo)
    StringSeries replaceAll(String regex, String to)
    StringSeries strip()
}