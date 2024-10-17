package com.github.grooviter.underdog.impl.ast


import groovy.transform.InheritConstructors
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.macro.matcher.ASTMatcher
import org.codehaus.groovy.syntax.Types

import static org.codehaus.groovy.ast.tools.GeneralUtils.args
import static org.codehaus.groovy.ast.tools.GeneralUtils.callX

@InheritConstructors
class SelectionTransformer extends ConditionalExpressionTransformer {

    @Override
    Expression transform(Expression expr) {
        if (expr == null) {
            return null
        }

        if (isExpression(expr)) {
            BinaryExpression binaryExpression = expr as BinaryExpression
            // leftExpression => table[column]
            BinaryExpression getAtX = binaryExpression.leftExpression as BinaryExpression
            VariableExpression varX = getAtX.leftExpression as VariableExpression
            ConstantExpression colX = getAtX.rightExpression as ConstantExpression
            // df.loc["column"]
            //     ^ ^
            //     | |
            //     | |
            //     | df.getAt("column") == df["column"]
            //     |
            //   df.loc == df.getLoc()
            MethodCallExpression columnX = callX(callX(varX, "getLoc"), "getAt", args(colX))

            return callX(columnX, resolveToken(binaryExpression.operation.type), args(binaryExpression.rightExpression))
        }

        return expr.transformExpression(this)
    }

    private static String resolveToken(int token){
        switch(token) {
            case Types.COMPARE_LESS_THAN_EQUAL: return "isLessThanOrEqualTo"
            case Types.COMPARE_LESS_THAN: return "isLessThan"
            case Types.COMPARE_GREATER_THAN: return "isGreaterThan"
            case Types.COMPARE_GREATER_THAN_EQUAL: return "isGreaterThanOrEqualTo"
            case Types.COMPARE_EQUAL: return "isEqualTo"
            case Types.COMPARE_NOT_EQUAL: return "isNotEqualTo"
            case Types.KEYWORD_IN: return "inList"
            case Types.MATCH_REGEX: return "matches"
        }
    }

    boolean isExpression(ASTNode node){
        ASTNode pattern = ASTMatcher.withConstraints(macro { table[column] > value }){
            placeholder("table", "column", "value")
            token {
                type in [
                    Types.COMPARE_GREATER_THAN,
                    Types.COMPARE_GREATER_THAN_EQUAL,
                    Types.COMPARE_LESS_THAN,
                    Types.COMPARE_LESS_THAN_EQUAL,
                    Types.COMPARE_EQUAL,
                    Types.COMPARE_NOT_EQUAL,
                    Types.KEYWORD_IN,
                    Types.MATCH_REGEX
                ]
            }
        }

        return ASTMatcher.matches(node, pattern)
    }
}
