package com.thomas.checkMate.resolving;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.searches.SuperMethodsSearch;
import com.intellij.psi.util.MethodSignatureBackedByPsiMethod;
import com.intellij.util.Query;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CachedSuperSearch {
    private static Map<PsiMethod, Collection<MethodSignatureBackedByPsiMethod>> superCache = new HashMap<>();

    public static Collection<MethodSignatureBackedByPsiMethod> search(PsiMethod method) {
        if (superCache.containsKey(method)) {
            return superCache.get(method);
        }
        Query<MethodSignatureBackedByPsiMethod> superSearch = SuperMethodsSearch.search(method, null, true, true);
        Collection<MethodSignatureBackedByPsiMethod> supers = superSearch.findAll();
        superCache.put(method, supers);
        return supers;
    }
}
