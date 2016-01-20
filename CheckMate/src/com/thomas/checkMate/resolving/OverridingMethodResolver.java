package com.thomas.checkMate.resolving;

import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.ArrayList;
import java.util.List;

public class OverridingMethodResolver {
    public static List<PsiMethod> resolve(PsiCallExpression callExpression, PsiMethod methodToResolve) {
        List<PsiType> psiTypes = InheritorResolver.resolve(callExpression);
        List<PsiClass> psiClasses = convert(psiTypes, JavaPsiFacade.getInstance(callExpression.getProject()));
        List<PsiMethod> overridingMethods = new ArrayList<>();
        for (PsiClass psiClass : psiClasses) {
            PsiMethod overridingMethod = psiClass.findMethodBySignature(methodToResolve, false);
            if (overridingMethod != null) {
                overridingMethods.add(overridingMethod);
            }
        }
        return overridingMethods;
    }

    private static List<PsiClass> convert(List<PsiType> types, JavaPsiFacade facade) {
        List<PsiClass> classes = new ArrayList<>();
//        types.stream().filter(type -> type != null).forEach(type -> {
//            PsiClass psiClass = facade.findClass(type.getCanonicalText(), GlobalSearchScope.allScope(facade.getProject()));
//            if (psiClass != null) {
//                classes.add(psiClass);
//            }
//        });
        for (PsiType type : types){
            String text = type.getCanonicalText();
            GlobalSearchScope scope = GlobalSearchScope.allScope(facade.getProject());
            PsiClass psiClass = facade.findClass(text, scope);
            if(psiClass != null) {
                classes.add(psiClass);
            }
        }
        return classes;
    }
}
