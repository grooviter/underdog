package underdog.plots.ast

import groovy.transform.InheritConstructors
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.stmt.BlockStatement

import static org.codehaus.groovy.ast.tools.GeneralUtils.*

@InheritConstructors
class RepeatableFieldTransformer extends ComplexFieldTransformer {
    @Override
    MethodNode toMethod() {
        Parameter[] methodParams = resolveParams()
        Expression executionMap = resolveExecutionMap(methodParams)
        ConstantExpression fieldNodeNameX = constX(fieldNode.name)

        // map[fieldNodeName] = getMapX()
        BlockStatement mapPutAtFieldValue = macro {
            def currentNodeValue = map[$v{fieldNodeNameX}]
            // no items use it as object
            if (!currentNodeValue) {
                map[$v{fieldNodeNameX}] = $v{executionMap}
                return
            }
            // there are some items in the list
            if (currentNodeValue instanceof java.util.List) {
                map[$v{fieldNodeNameX}] = currentNodeValue + [$v{executionMap}]
                return
            }
            // just one item -> then create a list
            map[$v{fieldNodeNameX}] = [currentNodeValue, $v{executionMap}]
            return
        }

        return createPublicVoidMethod(mapPutAtFieldValue, methodParams)
            .with(this.&markMethodAsGenerated)
            .with(this.&markMethodAsCompileDynamic) as MethodNode
    }

    @Override
    boolean shouldProcess() {
        return isComplex() && isAnnotatedWith(RepeatableField)
    }
}
