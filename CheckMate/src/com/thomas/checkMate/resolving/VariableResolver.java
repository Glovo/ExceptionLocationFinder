package com.thomas.checkMate.resolving;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.ResolveVariableUtil;

public class VariableResolver {
    public static PsiVariable resolveVariable(PsiExpression expression) {
        if (expression instanceof PsiMethodCallExpression)
            return resolveVariable((PsiMethodCallExpression) expression);
        if (expression instanceof PsiNewExpression)
            return resolveVariable((PsiNewExpression) expression);
        return null;
    }

    public static PsiVariable resolveVariable(PsiMethodCallExpression methodCallExpression) {
        PsiReferenceExpression methodExpression = methodCallExpression.getMethodExpression();
        PsiElement qualifier = methodExpression.getQualifier();
        if (qualifier != null) {
            PsiReference reference = qualifier.getReference();
            if (reference != null) {
                resolveIfReference(reference);
            }
        }
        return null;
    }

    public static PsiVariable resolveVariable(PsiNewExpression newExpression) {
        return ResolveVariableUtil.resolveVariable(null, null, null);
    }

    private static PsiVariable resolveIfReference(PsiReference reference) {
        if (reference instanceof PsiJavaCodeReferenceElement) {
            return ResolveVariableUtil.resolveVariable((PsiJavaCodeReferenceElement) reference, null, null);
        }
        return null;
    }
}
