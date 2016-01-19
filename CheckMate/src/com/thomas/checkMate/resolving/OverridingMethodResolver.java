package com.thomas.checkMate.resolving;

import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.ArrayList;
import java.util.List;

public class OverridingMethodResolver {
    public static List<PsiMethod> resolve(PsiCallExpression callExpression, PsiMethod methodToResolve) {
        List<PsiClass> psiClasses = InheritorResolver.resolveInheritors(callExpression);
        List<PsiMethod> overridingMethods = new ArrayList<>();
        for (PsiClass psiClass : psiClasses) {
            PsiMethod overridingMethod = psiClass.findMethodBySignature(methodToResolve, false);
            if (overridingMethod != null) {
                overridingMethods.add(overridingMethod);
            }
        }
        return overridingMethods;
    }
}
