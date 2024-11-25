package com.github.grooviter.underdog.plots.ast

import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression

import static org.codehaus.groovy.ast.tools.GeneralUtils.*

class ComplexFieldTransformer extends FieldToMethodTransformer {
    ComplexFieldTransformer(FieldNode fieldNode) {
        super(fieldNode)
    }

    @Override
    MethodNode toMethod() {
        Parameter methodParam = param(closureClassNode, fieldNode.name)
        methodParam.addAnnotation(createDelegatesTo(fieldNode.type))

        // map[fieldNodeName] = getMapX()
        Expression mapPutAtFieldValue = macro { map[$v{constX(fieldNode.name)}] = $v{delegatedExecutionMap} }

        return markMethodAsGenerated(createPublicVoidMethod(mapPutAtFieldValue, methodParam))
    }

    Expression getDelegatedExecutionMap() {
        // new FieldTypeX().tapX(paramClosureNameX).getMapX()
        VariableExpression paramClosureNameX = varX(fieldNode.name)
        ConstructorCallExpression newFieldTypeX = ctorX(fieldNode.type)
        MethodCallExpression tapX = callX(newFieldTypeX, "tap", args(paramClosureNameX))
        MethodCallExpression getMapX = callX(tapX, "getMap")

        return getMapX
    }

    @Override
    boolean shouldProcess() {
        return isComplex()
    }
}
