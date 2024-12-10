package underdog.plots.ast

import org.codehaus.groovy.ast.expr.MapEntryExpression
import org.codehaus.groovy.ast.stmt.Statement
import underdog.plots.dsl.NodeMap
import groovy.transform.CompileStatic
import groovyjarjarasm.asm.Opcodes
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import static org.codehaus.groovy.ast.tools.GeneralUtils.assignX
import static org.codehaus.groovy.ast.tools.GeneralUtils.mapEntryX
import static org.codehaus.groovy.ast.tools.GeneralUtils.mapX
import static org.codehaus.groovy.ast.tools.GeneralUtils.stmt
import static org.codehaus.groovy.ast.tools.GeneralUtils.thisPropX

@CompileStatic
@SuppressWarnings('unused')
@GroovyASTTransformation(phase= CompilePhase.SEMANTIC_ANALYSIS)
class NodeAstTransformation extends AbstractASTTransformation implements Opcodes {
    private static final String MAP_FIELD_NAME = 'map'

    @Override
    void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        ClassNode classNode = (ClassNode) astNodes[1]

        if (!hasAnnotation(classNode.superClass, ClassHelper.make(Node))) {
            classNode.setSuperClass(ClassHelper.make(NodeMap))
        }
        classNode.addAnnotation(new AnnotationNode(ClassHelper.make(CompileStatic)))

        FieldNode[] fields = classNode.fields.findAll(fieldIsNotMap)
        createInitialNodeMap(classNode, fields)
        createDSLMethods(classNode, fields)
        cleaningUpClassNode(classNode)
    }

    private static cleaningUpClassNode(ClassNode classNode) {
        classNode.fields.removeIf(fieldIsNotMap)
        classNode.properties.removeIf { it.name != MAP_FIELD_NAME }
    }

    private static createInitialNodeMap(ClassNode classNode, FieldNode[] fields) {
        List<MapEntryExpression> initialMapExpressions = fields
            .findAll { it.initialValueExpression }
            .collect {mapEntryX(it.name, it.initialValueExpression)}

        Statement stmt = stmt(assignX(thisPropX(true, MAP_FIELD_NAME), mapX(initialMapExpressions)))
        classNode.addConstructor(new ConstructorNode(ACC_PUBLIC, stmt))
    }

    private static createDSLMethods(ClassNode classNode, FieldNode[] fields){
        fields
            .collect {
                transformers(it)
                    .find { it.shouldProcess() }
                    .toMethod()
            }
            .each {
                classNode.addMethod(it)
            }
    }

    private static Closure<Boolean> getFieldIsNotMap() {
        return { FieldNode node ->
            return node.name != MAP_FIELD_NAME
        }
    }

    private static List<FieldToMethodTransformer> transformers(FieldNode fieldNode) {
        return [
            new SimpleFieldTransformer(fieldNode),
            new RepeatableFieldTransformer(fieldNode),
            new ComplexFieldTransformer(fieldNode),
        ]
    }
}
