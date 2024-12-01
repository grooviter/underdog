package underdog.impl.ast

import underdog.Wildcard
import groovy.transform.InheritConstructors
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.syntax.Types

import static org.codehaus.groovy.ast.tools.GeneralUtils.args
import static org.codehaus.groovy.ast.tools.GeneralUtils.callX
import static org.codehaus.groovy.ast.tools.GeneralUtils.classX
import static org.codehaus.groovy.ast.tools.GeneralUtils.constX
import static org.codehaus.groovy.ast.tools.GeneralUtils.propX
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
            Expression leftExpression = tableAndColsX.leftExpression
            Expression colsX = extractColsX(tableAndColsX.rightExpression as ListExpression)

            if (leftExpression instanceof VariableExpression) {
                return callX(leftExpression, 'selectColumns', colsX)
            }

            if (leftExpression instanceof PropertyExpression){
                Expression iloc = tableAndColsX.leftExpression
                Expression ALL = propX(classX(Wildcard.class), constX("ALL"))
                return callX(iloc, 'getAt', args(ALL, colsX))
            }
        }

        if (expr instanceof VariableExpression) {
            if (expr.text == "__") {
                return propX(classX(Wildcard), constX('ALL'))
            }
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
                    rightX.expressions[0].text == '__'
            }
        }

        return matches(node, indexedActionPattern)
    }

    // [_, cols]
    private static Expression extractColsX(ListExpression underscoreAndCols) {
        return underscoreAndCols.expressions.last()
    }
}
