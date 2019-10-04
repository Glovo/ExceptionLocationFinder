package com.thomas.checkMate.resolving;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OverridingMethodResolver {
    public static List<PsiMethod> resolve(PsiMethod method) {
        PsiClass encapsulatingClass = PsiTreeUtil.getParentOfType(method, PsiClass.class);
        List<PsiMethod> overridingMethods = new ArrayList<>();
        if (encapsulatingClass != null) {
            Query<PsiClass> search = ClassInheritorsSearch.search(encapsulatingClass);
            Collection<PsiClass> implementingClasses = search.findAll();
            implementingClasses.forEach(ic -> {
                PsiMethod methodBySignature = ic.findMethodBySignature(method, true);
                overridingMethods.add(methodBySignature);
            });
        }
        return overridingMethods;
    }
}
