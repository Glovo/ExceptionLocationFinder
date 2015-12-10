package com.thomas.checkMate.utilities;

import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Arrays;
import java.util.List;

public class ExceptionTypeAnalyser {

    private final PsiClassType javaLangError;
    private final PsiClassType javaLangRuntimeException;

    public ExceptionTypeAnalyser(PsiManager psiManager, GlobalSearchScope globalSearchScope) {
        this.javaLangError = PsiType.getJavaLangError(psiManager, globalSearchScope);
        this.javaLangRuntimeException = PsiType.getJavaLangRuntimeException(psiManager, globalSearchScope);
    }

    public boolean isUncheckedOrError(PsiType psiType) {
        List<PsiType> parentTypes = Arrays.asList(psiType.getSuperTypes());
        return psiType.equals(javaLangRuntimeException) || psiType.equals(javaLangError) || parentTypes.contains(javaLangRuntimeException) || parentTypes.contains(javaLangError);
    }

    public PsiType getExceptionTypeOfThrowStatement(PsiThrowStatement psiThrowStatement) {
        PsiExpression exception = psiThrowStatement.getException();
        if (exception != null) {
            return exception.getType();
        }
        return null;
    }
}
