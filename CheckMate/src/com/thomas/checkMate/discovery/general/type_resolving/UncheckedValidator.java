package com.thomas.checkMate.discovery.general.type_resolving;

import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import com.thomas.checkMate.utilities.PsiTypeUtil;

import java.util.ArrayList;
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
        List<PsiType> superTypes = new ArrayList<>();
        PsiTypeUtil.getDeepSupers(psiType, superTypes);
        boolean isUncheckedOrError = psiType.equals(javaLangRuntimeException) || superTypes.contains(javaLangRuntimeException);
        if (includeErrors) {
            isUncheckedOrError = isUncheckedOrError || psiType.equals(javaLangError) || superTypes.contains(javaLangError);
        }
        return isUncheckedOrError;
    }
}
