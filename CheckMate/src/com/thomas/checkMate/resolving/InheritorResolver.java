package com.thomas.checkMate.resolving;

import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;

import java.util.ArrayList;
import java.util.List;

public class InheritorResolver {
    public static List<PsiType> resolve(PsiExpression expression) {
        List<PsiType> inheritors = new ArrayList<>();
        PsiVariable variable = VariableResolver.resolveVariable(expression);
        if (variable != null) {
            List<PsiExpression> assignmentExpressions = new ArrayList<>();
            PsiExpression initializer = variable.getInitializer();
            if (initializer != null) {
                assignmentExpressions.add(initializer);
            }
            assignmentExpressions.addAll(AssignmentResolver.resolve(variable));
            if (assignmentExpressions.size() > 0) {
                process(assignmentExpressions, inheritors);
            } else {
                List<PsiExpression> paramExpressions = MethodParamResolver.resolve(variable, expression);
                process(paramExpressions, inheritors);
            }
        }
        return inheritors;
    }

    private static void process(List<PsiExpression> expressions, List<PsiType> inheritors) {
        expressions.forEach(ae -> {
            List<PsiType> subInheritors = resolve(ae);
            if (subInheritors.size() > 0) {
                inheritors.addAll(subInheritors);
            } else {
                inheritors.add(ae.getType());
            }
        });
    }
}
