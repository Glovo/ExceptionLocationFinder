package com.thomas.checkMate.resolving;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;

import java.util.*;

public class OverridingMethodResolver {
    private static Map<PsiMethod, List<PsiMethod>> resolveCache = new HashMap<>();

    public static List<PsiMethod> resolve(PsiMethod method) {
        if (resolveCache.containsKey(method)) {
            return resolveCache.get(method);
        }
        List<PsiMethod> overridingMethods = new ArrayList<>();
        PsiClass encapsulatingClass = PsiTreeUtil.getParentOfType(method, PsiClass.class);
        if (encapsulatingClass != null) {
            Query<PsiClass> search = ClassInheritorsSearch.search(encapsulatingClass);
            Collection<PsiClass> implementingClasses = search.findAll();
            implementingClasses.forEach(ic -> {
                PsiMethod methodBySignature = ic.findMethodBySignature(method, true);
                overridingMethods.add(methodBySignature);
            });
        }
        resolveCache.put(method, overridingMethods);
        return overridingMethods;
    }
}
