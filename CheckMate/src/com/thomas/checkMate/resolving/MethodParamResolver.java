package com.thomas.checkMate.resolving;

import com.intellij.psi.*;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MethodParamResolver {
    public static List<PsiExpression> resolve(PsiVariable variable, PsiExpression expression) {
        PsiMethod surroundingMethod = PsiTreeUtil.getParentOfType(expression, PsiMethod.class);
        List<PsiExpression> resolvedExpression = new ArrayList<>();
        if (surroundingMethod != null) {
            PsiParameterList parameterList = surroundingMethod.getParameterList();
            PsiParameter[] parameters = parameterList.getParameters();
            for (PsiParameter parameter : parameters) {
                PsiIdentifier nameIdentifier = parameter.getNameIdentifier();
                if (nameIdentifier != null && nameIdentifier.equals(variable.getNameIdentifier())) {
                    resolvedExpression.addAll(processMethod(surroundingMethod, parameterList.getParameterIndex(parameter)));
                }
            }
        }
        return resolvedExpression;
    }

    private static List<PsiExpression> processMethod(PsiMethod surroundingMethod, int argumentIndex) {
        Query<PsiReference> search = ReferencesSearch.search(surroundingMethod);
        Collection<PsiReference> methodReferences = search.findAll();
        List<PsiExpression> discoveredExpressions = new ArrayList<>();
        methodReferences.forEach(mr -> {
            if (mr instanceof PsiReferenceExpression) {
                PsiElement parent = ((PsiReferenceExpression) mr).getParent();
                if (parent != null && parent instanceof PsiMethodCallExpression) {
                    PsiExpressionList argumentList = ((PsiMethodCallExpression) parent).getArgumentList();
                    PsiExpression[] expressions = argumentList.getExpressions();
                    if (expressions.length > argumentIndex) {
                        discoveredExpressions.add(expressions[argumentIndex]);
                    }
                }
            }
        });
        return discoveredExpressions;
    }
}
