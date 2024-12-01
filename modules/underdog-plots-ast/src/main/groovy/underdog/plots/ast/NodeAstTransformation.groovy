package underdog.plots.ast

import underdog.plots.dsl.NodeMap
import groovy.transform.CompileStatic
import groovyjarjarasm.asm.Opcodes
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@CompileStatic
@SuppressWarnings('unused')
@GroovyASTTransformation(phase= CompilePhase.SEMANTIC_ANALYSIS)
class NodeAstTransformation extends AbstractASTTransformation implements Opcodes {
    private static final String MAP_FIELD_NAME = 'map'

    @Override
    void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        ClassNode classNode = (ClassNode) astNodes[1]
        classNode.setSuperClass(ClassHelper.make(NodeMap))
        classNode.addAnnotation(new AnnotationNode(ClassHelper.make(CompileStatic)))
        FieldNode[] fields = classNode.fields.findAll(fieldIsNotMap)

        fields
            .collect {
                transformers(it).find { it.shouldProcess() }.toMethod()
            }
            .each {
                classNode.addMethod(it)
            }

        classNode.fields.removeIf(fieldIsNotMap)
        classNode.properties.removeIf { it.name != MAP_FIELD_NAME }
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
