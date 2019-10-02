package com.thomas.checkMate.resolving;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.impl.source.resolve.ResolveVariableUtil;

public class VariableResolver {
    public static PsiVariable resolveVariable(PsiExpression expression) {
        if (expression instanceof PsiMethodCallExpression)
            return resolveVariable((PsiMethodCallExpression) expression);
        if (expression instanceof PsiNewExpression)
            return resolveVariable((PsiNewExpression) expression);
        if (expression instanceof PsiReferenceExpression) {
            return resolveVariable((PsiReferenceExpression) expression);
        }
        return null;
    }

    public static PsiVariable resolveVariable(PsiMethodCallExpression methodCallExpression) {
        PsiReferenceExpression methodExpression = methodCallExpression.getMethodExpression();
        PsiElement qualifier = methodExpression.getQualifier();
        if (qualifier != null) {
            PsiReference reference = qualifier.getReference();
            if (reference != null) {
                return resolveIfReference(reference);
            }
        }
        return null;
    }

    public static PsiVariable resolveVariable(PsiNewExpression newExpression) {
        return resolveIfReference(newExpression.getReference());
    }

    public static PsiVariable resolveVariable(PsiReferenceExpression referenceExpression) {
        return resolveIfReference(referenceExpression);
    }

    private static PsiVariable resolveIfReference(PsiReference reference) {
        if (reference instanceof PsiJavaCodeReferenceElement) {
            return ResolveVariableUtil.resolveVariable((PsiJavaCodeReferenceElement) reference, null, null);
        }
        return null;
    }
}
