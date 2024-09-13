package com.github.grooviter.underdog.tablesaw.ast

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ModuleNode
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
class TablesawTransformation implements ASTTransformation  {
    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        SelectionTransformer selections = new SelectionTransformer(source)
        UnderscoreTransformer underscore = new UnderscoreTransformer(source)

        ModuleNode moduleNode = nodes.find() as ModuleNode

        moduleNode.classes.each {
            selections.visitClass(it)
            underscore.visitClass(it)
        }
    }
}
