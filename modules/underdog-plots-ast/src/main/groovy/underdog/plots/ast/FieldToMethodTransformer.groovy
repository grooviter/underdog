package underdog.plots.ast

import groovy.transform.CompileDynamic
import groovy.transform.Generated
import groovy.transform.TupleConstructor
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.MethodNode
import groovyjarjarasm.asm.Opcodes
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ReturnStatement

import static org.codehaus.groovy.ast.tools.GeneralUtils.*

@TupleConstructor
abstract class FieldToMethodTransformer implements Opcodes {
    FieldNode fieldNode

    abstract MethodNode toMethod()
    abstract boolean shouldProcess()

    protected BlockStatement executeExpressionAndReturnVoid(Expression expr) {
        return block(stmt(expr), returnS(nullX()))
    }

    protected MethodNode markMethodAsGenerated(MethodNode methodNode) {
        methodNode.addAnnotation(ClassHelper.make(Generated))
        return methodNode
    }

    protected MethodNode markMethodAsCompileDynamic(MethodNode methodNode) {
        methodNode.addAnnotation(ClassHelper.make(CompileDynamic))
        return methodNode
    }

    protected ClassNode getClosureClassNode() {
        return closureX(stmt(constX(null))).type
    }

    protected AnnotationNode createDelegatesTo(ClassNode classNodeToDelegateTo, int strategy = Closure.DELEGATE_FIRST) {
        AnnotationNode delegatesTo = new AnnotationNode(ClassHelper.make(DelegatesTo))
        delegatesTo.addMember("value", classX(classNodeToDelegateTo))
        delegatesTo.addMember("strategy", constX(strategy))
        return delegatesTo
    }

    protected MethodNode createPublicVoidMethod(Expression expressionToeExecute, Parameter... params) {
        return new MethodNode(
                fieldNode.name,
                ACC_PUBLIC,
                ClassHelper.void_WRAPPER_TYPE,
                params,
                [] as ClassNode[],
                executeExpressionAndReturnVoid(expressionToeExecute)
        )
    }

    protected MethodNode createPublicVoidMethod(BlockStatement stmt, Parameter... params) {
        return new MethodNode(
                fieldNode.name,
                ACC_PUBLIC,
                ClassHelper.void_WRAPPER_TYPE,
                params,
                [] as ClassNode[],
                stmt
        )
    }

    protected boolean isSimple() {
        return ClassHelper.isNumberType(fieldNode.type) ||
                ClassHelper.isStringType(fieldNode.type) ||
                ClassHelper.isWrapperBoolean(fieldNode.type) ||
                ClassHelper.isPrimitiveBoolean(fieldNode.type) ||
                fieldNode.type == ClassHelper.Number_TYPE ||
                fieldNode.type.name.contains('List')
    }

    protected boolean isAnnotatedWith(Class annotation) {
        return fieldNode.getAnnotations(ClassHelper.make(annotation))
    }

    protected  boolean isComplex() {
        return !isSimple()
    }
}
