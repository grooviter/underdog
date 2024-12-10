package underdog.plots.ast

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.GenericsType
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.expr.ClassExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression

import static org.codehaus.groovy.ast.ClassHelper.make
import static org.codehaus.groovy.ast.tools.GeneralUtils.*

@CompileStatic
class ComplexFieldTransformer extends FieldToMethodTransformer {
    ComplexFieldTransformer(FieldNode fieldNode) {
        super(fieldNode)
    }

    @Override
    MethodNode toMethod() {
        Parameter[] methodParams = resolveParams()
        Expression executionMapX = resolveExecutionMap(methodParams)
        // map[fieldNode.name] = executionMapX === map.putAt(fieldNode.name, executionMapX)
        Expression mapPutAtFieldValue = putAtX(varX('map'), fieldNode.name, executionMapX)
        return markMethodAsGenerated(createPublicVoidMethod(mapPutAtFieldValue, methodParams))
    }

    private static Expression putAtX(Expression target, String propertyName, Expression argument) {
        return callX(target, 'putAt', args(constX(propertyName), argument))
    }

    protected Expression resolveExecutionMap(Parameter[] parameters){
        String name = fieldNode.name
        ClassNode type = fieldNode.type

        // new type().tapX(paramClosureNameX).getMapX()
        VariableExpression paramClosureNameX = varX(name)
        Expression newFieldTypeX = parameters.size() == 2
                ? callX(varX('clazz'), "newInstance") // clazz.newInstance()
                : ctorX(type) // new type()

        MethodCallExpression tapX = callX(newFieldTypeX, "tap", args(paramClosureNameX))
        MethodCallExpression getMapX = callX(tapX, "getMap")

        return getMapX
    }

    protected Parameter[] resolveParams() {
        if (isAnnotatedWith(AllowInheritance)) {
            List<AnnotationNode> annotations = fieldNode.getAnnotations(make(AllowInheritance))
            AnnotationNode annotation = annotations.find()
            ClassExpression annotationValue = annotation.getMember('value') as ClassExpression

            // creates a generics placeholder, can be used for any class as a generics placeholder
            ClassNode placeHolder = make("T")
            placeHolder.setGenericsPlaceHolder(true)

            // creates a generics type with a placeholder and and a wildcard <? extends annotationValue.type>
            GenericsType genericsType = new GenericsType(placeHolder, [annotationValue.type] as ClassNode[], annotationValue.type)
            genericsType.setPlaceholder(true)
            genericsType.setWildcard(true)

            // to avoid problems using generics we need to get the reference of the ClassNode using generics
            // BEFORE using the ClassNode
            ClassNode classParamType = make(Class<?>, true).plainNodeReference
            classParamType.setUsingGenerics(true)
            classParamType.setGenericsTypes([genericsType] as GenericsType[])

            Parameter classParam = param(classParamType, "clazz")
            classParam.addAnnotation(new AnnotationNode(make(DelegatesTo.Target)))
            Parameter methodParam = param(closureClassNode, fieldNode.name)
            methodParam.addAnnotation(createDelegatesToTarget())

            return [classParam, methodParam] as Parameter[]
        }
        Parameter methodParam = param(closureClassNode, fieldNode.name)
        methodParam.addAnnotation(createDelegatesTo(fieldNode.type))
        return [methodParam] as Parameter[]
    }

    private static AnnotationNode createDelegatesToTarget() {
        AnnotationNode delegatesTo = new AnnotationNode(make(DelegatesTo))
        delegatesTo.setMember("genericTypeIndex", constX(0))
        return delegatesTo
    }

    @Override
    boolean shouldProcess() {
        return isComplex()
    }
}
