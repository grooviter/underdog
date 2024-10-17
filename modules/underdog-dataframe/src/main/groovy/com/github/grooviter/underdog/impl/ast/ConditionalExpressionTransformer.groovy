package com.github.grooviter.underdog.impl.ast

import groovy.transform.TupleConstructor
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassCodeExpressionTransformer
import org.codehaus.groovy.control.SourceUnit

@TupleConstructor
abstract class ConditionalExpressionTransformer extends ClassCodeExpressionTransformer {
    SourceUnit sourceUnit

    abstract boolean isExpression(ASTNode node)
}
