package underdog.plots.ast

import groovy.transform.InheritConstructors
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.expr.Expression
import static org.codehaus.groovy.ast.tools.GeneralUtils.*

@InheritConstructors
class SimpleFieldTransformer extends FieldToMethodTransformer {
    @Override
    MethodNode toMethod() {
        Parameter methodParam = param(fieldNode.type, fieldNode.name)

        Expression putAtMap = macro { map[$v{constX(fieldNode.name)}] = $v{varX(fieldNode.name)} }


        return markMethodAsGenerated(createPublicVoidMethod(putAtMap, methodParam))
    }

    @Override
    boolean shouldProcess() {
        return isSimple()
    }
}
