package com.thomas.checkMate.discovery.general;

import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Arrays;
import java.util.List;

public class UncheckedValidator {
    private final PsiClassType javaLangError;
    private final PsiClassType javaLangRuntimeException;

    public UncheckedValidator(PsiManager psiManager, GlobalSearchScope globalSearchScope) {
        this.javaLangError = PsiType.getJavaLangError(psiManager, globalSearchScope);
        this.javaLangRuntimeException = PsiType.getJavaLangRuntimeException(psiManager, globalSearchScope);
    }

    public boolean isUncheckedOrError(PsiType psiType) {
        List<PsiType> parentTypes = Arrays.asList(psiType.getSuperTypes());
        return psiType.equals(javaLangRuntimeException) || psiType.equals(javaLangError) || parentTypes.contains(javaLangRuntimeException) || parentTypes.contains(javaLangError);
    }
}
