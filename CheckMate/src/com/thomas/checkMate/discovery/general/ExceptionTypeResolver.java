package com.thomas.checkMate.discovery.general;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;

public interface ExceptionTypeResolver<T extends PsiElement> {
    PsiType resolve(T psiElement);
}
