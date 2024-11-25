package com.github.grooviter.underdog.plots.ast

import groovy.transform.InheritConstructors
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.stmt.BlockStatement

import static org.codehaus.groovy.ast.tools.GeneralUtils.*

@InheritConstructors
class RepeatableFieldTransformer extends ComplexFieldTransformer {
    @Override
    MethodNode toMethod() {
        Parameter methodParam = param(closureClassNode, fieldNode.name)
        methodParam.addAnnotation(createDelegatesTo(fieldNode.type))

        // map[fieldNodeName] = getMapX()
        BlockStatement mapPutAtFieldValue = macro {
            def currentNodeValue = map[$v{constX(fieldNode.name)}]

            // no items use it as object
            if (!currentNodeValue) {
                map[$v{constX(fieldNode.name)}] = $v{delegatedExecutionMap}
                return
            }

            // there are some items in the list
            if (currentNodeValue instanceof java.util.List) {
                map[$v{constX(fieldNode.name)}] = currentNodeValue + [$v{delegatedExecutionMap}]
                return
            }

            // just one item -> then create a list
            map[$v{constX(fieldNode.name)}] = [currentNodeValue, $v{delegatedExecutionMap}]
            return
        }

        return createPublicVoidMethod(mapPutAtFieldValue, methodParam)
                .with(this.&markMethodAsGenerated)
                .with(this.&markMethodAsCompileDynamic) as MethodNode
    }

    @Override
    boolean shouldProcess() {
        return isComplex() && isAnnotatedWith(RepeatableField)
    }
}
