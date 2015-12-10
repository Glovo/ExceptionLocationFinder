package com.thomas.checkMate.writing;

import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiType;

public class PsiTypeWriter {
    private final PsiElementFactory elementFactory;

    public PsiTypeWriter(PsiElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    public PsiType write(String exceptionString) {
        return elementFactory.createTypeFromText(exceptionString, null);
    }
}
