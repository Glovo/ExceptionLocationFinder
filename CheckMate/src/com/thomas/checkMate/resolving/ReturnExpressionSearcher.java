package com.thomas.checkMate.resolving;

import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReturnStatement;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReturnExpressionSearcher {
    public static List<PsiExpression> search(PsiMethodCallExpression methodCallExpression) {
        List<PsiExpression> returnExpressions = new ArrayList<>();
        PsiMethod method = methodCallExpression.resolveMethod();
        if (method != null) {
            List<PsiMethod> overridingMethods = OverridingMethodResolver.resolve(methodCallExpression, method);
            if (overridingMethods.size() < 1) {
                overridingMethods.add(method);
            }
            overridingMethods.forEach(om -> {
                Collection<PsiReturnStatement> returnStatements = PsiTreeUtil.findChildrenOfType(om, PsiReturnStatement.class);
                returnStatements.forEach(rs -> {
                    PsiExpression returnExpression = rs.getReturnValue();
                    if (returnExpression != null) {
                        returnExpressions.add(returnExpression);
                    }
                });
            });
        }
        return returnExpressions;
    }
}
