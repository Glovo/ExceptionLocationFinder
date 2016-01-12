package com.thomas.checkMate.discovery.general.type_resolving;

import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UncheckedValidator {
    private final PsiClassType javaLangError;
    private final PsiClassType javaLangRuntimeException;
    private final boolean includeErrors;

    public UncheckedValidator(PsiManager psiManager, GlobalSearchScope globalSearchScope, boolean includeErrors) {
        this.javaLangError = PsiType.getJavaLangError(psiManager, globalSearchScope);
        this.javaLangRuntimeException = PsiType.getJavaLangRuntimeException(psiManager, globalSearchScope);
        this.includeErrors = includeErrors;
    }

    public boolean isUncheckedOrError(PsiType psiType) {
        List<PsiType> superTypes = getSupers(psiType, new ArrayList<>());
        boolean isUncheckedOrError = psiType.equals(javaLangRuntimeException) || superTypes.contains(javaLangRuntimeException);
        if (includeErrors) {
            isUncheckedOrError = isUncheckedOrError || psiType.equals(javaLangError) || superTypes.contains(javaLangError);
        }
        return isUncheckedOrError;
    }

    private List<PsiType> getSupers(PsiType psiType, List<PsiType> superTypes) {
        List<PsiType> parentTypes = Arrays.asList(psiType.getSuperTypes());
        superTypes.addAll(parentTypes);
        for (PsiType parentType : parentTypes) {
            getSupers(parentType, superTypes);
        }
        return superTypes;
    }
}
