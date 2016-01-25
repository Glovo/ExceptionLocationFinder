package com.thomas.checkMate.resolving;

import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.List;

public class TypeResolver {
    public static List<PsiType> resolve(PsiExpression expression) {
        List<PsiType> plausibleTypes = new ArrayList<>();
        if (expression instanceof PsiNewExpression) {
            plausibleTypes.add(expression.getType());
            return plausibleTypes;
        }
        if (expression instanceof PsiMethodCallExpression) {
            List<PsiExpression> returnExpressions = ReturnExpressionSearcher.search((PsiMethodCallExpression) expression);
            resolveDeeperOrAdd(returnExpressions, plausibleTypes);
            return plausibleTypes;
        }
        if (expression instanceof PsiReferenceExpression) {
            PsiVariable variable = VariableResolver.resolveVariable(expression);
            if (variable != null) {
                List<PsiExpression> assignmentExpressions = AssignmentSearcher.search(variable);
                if (assignmentExpressions.size() > 0) {
                    resolveDeeperOrAdd(assignmentExpressions, plausibleTypes);
                    return plausibleTypes;
                } else {
                    List<PsiExpression> paramExpressions = MethodParamResolver.resolve(variable);
                    resolveDeeperOrAdd(paramExpressions, plausibleTypes);
                    return plausibleTypes;
                }
            }
        }
        return plausibleTypes;
    }

    private static void resolveDeeperOrAdd(List<PsiExpression> expressions, List<PsiType> plausibleTypes) {
        expressions.forEach(ae -> {
            List<PsiType> subTypes = resolve(ae);
            if (subTypes.size() > 0) {
                plausibleTypes.addAll(subTypes);
            } else {
                plausibleTypes.add(ae.getType());
            }
        });
    }
}
