package com.thomas.checkMate.resolving;

import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OverridingMethodEstimator {
    private static Map<PsiMethodCallExpression, List<PsiMethod>> estimationCache = new HashMap<>();

    public static List<PsiMethod> estimate(PsiMethodCallExpression methodCallExpression, PsiMethod methodToResolve) {
        if (estimationCache.containsKey(methodCallExpression)) {
            return estimationCache.get(methodCallExpression);
        }
        List<PsiMethod> overridingMethods = new ArrayList<>();
        PsiExpression qualifierExpression = methodCallExpression.getMethodExpression().getQualifierExpression();
        if (qualifierExpression != null) {
            List<PsiType> psiTypes = TypeEstimator.estimate(qualifierExpression);
            List<PsiClass> psiClasses = convert(psiTypes, JavaPsiFacade.getInstance(methodCallExpression.getProject()));
            for (PsiClass psiClass : psiClasses) {
                PsiMethod overridingMethod = psiClass.findMethodBySignature(methodToResolve, false);
                if (overridingMethod != null) {
                    overridingMethods.add(overridingMethod);
                }
            }
        }
        estimationCache.put(methodCallExpression, overridingMethods);
        return overridingMethods;
    }

    private static List<PsiClass> convert(List<PsiType> types, JavaPsiFacade facade) {
        List<PsiClass> classes = new ArrayList<>();
        for (PsiType type : types) {
            String text = type.getCanonicalText();
            GlobalSearchScope scope = GlobalSearchScope.allScope(facade.getProject());
            PsiClass psiClass = facade.findClass(text, scope);
            if (psiClass != null) {
                classes.add(psiClass);
            }
        }
        return classes;
    }
}
