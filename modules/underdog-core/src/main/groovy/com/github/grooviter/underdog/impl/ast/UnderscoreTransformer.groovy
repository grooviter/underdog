package com.github.grooviter.underdog.impl.ast

import groovy.transform.InheritConstructors
import org.codehaus.groovy.GroovyBugError
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.GenericsType
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.syntax.Types

import static org.codehaus.groovy.ast.tools.GeneralUtils.callX
import static org.codehaus.groovy.ast.tools.GeneralUtils.classX
import static org.codehaus.groovy.macro.matcher.ASTMatcher.matches
import static org.codehaus.groovy.macro.matcher.ASTMatcher.withConstraints

@InheritConstructors
class UnderscoreTransformer extends ConditionalExpressionTransformer {

    @Override
    Expression transform(Expression expr) {
        if (expr == null) {
            return null
        }

        if (isExpression(expr)) {
            BinaryExpression tableAndColsX = expr as BinaryExpression
            VariableExpression tableX = tableAndColsX.leftExpression as VariableExpression
            Expression colsX = extractColsX(tableAndColsX.rightExpression as ListExpression)

            return callX(tableX, 'selectColumns', colsX)
        }

        return expr.transformExpression(this)
    }

    boolean isExpression(ASTNode node){
        ASTNode indexedActionPattern = withConstraints(macro { table[__, cols] }) {
            placeholder('table', 'cols')
            token {
                type == Types.LEFT_SQUARE_BRACKET
            }
            eventually {
                BinaryExpression binaryExpression = node as BinaryExpression
                Expression rightX = binaryExpression.rightExpression

                return rightX instanceof ListExpression &&
                    rightX.expressions.size() > 1 &&
                    rightX.expressions[0] instanceof VariableExpression &&
                    rightX.expressions[0].name == '__'
            }
        }

        return matches(node, indexedActionPattern)
    }

    // [_, cols]
    private static Expression extractColsX(ListExpression underscoreAndCols) {
        Expression cols = underscoreAndCols.expressions.last()
        switch(cols.class) {
            case ListExpression:     return extractColsFromColumnListX(cols as ListExpression)
            case RangeExpression:    return extractColsFromIntRangeX(cols as RangeExpression)
            case VariableExpression: return extractColsFromVarX(cols as VariableExpression)
            default:
                return forceToList(cols)
        }
    }

    private static Expression forceToList(Expression expression) {
        return callX(expression, 'toList')
    }

    private static Expression extractColsFromVarX(VariableExpression varX) {
        ClassNode variableClassNode = varX.type

        if (variableClassNode == new ClassNode(List)) {
            GenericsType[] generics = variableClassNode.getGenericsTypes()
            if (generics.size() == 1 && generics[0].type == new ClassNode(Integer)) {
                    return callX(varX, 'asType', classX(int[]))
            }
            return callX(varX, 'asType', classX(String[]))
        }

        return varX
    }

    private static MethodCallExpression extractColsFromIntRangeX(RangeExpression cols) {
        if ([cols.from.type, cols.to.type].every {it == new ClassNode(int)}) {
            return callX(cols, 'asType', classX(int[]))
        }

        return callX(cols, 'asType', classX(String[]))
    }

    private static MethodCallExpression extractColsFromColumnListX(ListExpression cols) {
        boolean isColNames = cols.expressions.type.every { new ClassNode(String) == it }
        boolean isColIndexes = cols.expressions.type.every { new ClassNode(int) == it }

        if (isColNames) {
            return callX(cols, 'asType', classX(String[].class))
        }

        if (isColIndexes) {
            return callX(cols, 'asType', classX(int[].class))
        }

        throw new GroovyBugError('list can be only of col names or col indexes')
    }
}
