package com.github.grooviter.underdog.plots.ast

import org.codehaus.groovy.transform.GroovyASTTransformationClass

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.TYPE])
@GroovyASTTransformationClass(["com.github.grooviter.underdog.plots.ast.NodeAstTransformation"])
@interface Node {
    boolean hasId()
}