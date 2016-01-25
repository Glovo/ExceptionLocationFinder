package com.thomas.checkMate.resolving;

import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;

import java.util.ArrayList;
import java.util.List;

public class TypeResolver {
    public static List<PsiType> resolve(PsiExpression expression) {
        List<PsiType> plausibleTypes = new ArrayList<>();
        PsiVariable variable = VariableResolver.resolveVariable(expression);
        if (variable != null) {
            List<PsiExpression> assignmentExpressions = AssignmentSearcher.search(variable);
            if (assignmentExpressions.size() > 0) {
                resolveDeeperOrAdd(assignmentExpressions, plausibleTypes);
            } else {
                List<PsiExpression> paramExpressions = MethodParamResolver.resolve(variable);
                resolveDeeperOrAdd(paramExpressions, plausibleTypes);
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
