package com.github.grooviter.underdog.plots.dsl

import com.github.grooviter.underdog.plots.ast.Node

@Node(hasId = true)
class Dataset {
    Object source // array or object(map)
    List dimensions // array of any
    Object sourceHeader // boolean number
    Number fromDatasetIndex
    String fromDatasetId
    Number fromTransformResult
}
