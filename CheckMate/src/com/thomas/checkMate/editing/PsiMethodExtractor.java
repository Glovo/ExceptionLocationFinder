package com.thomas.checkMate.editing;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;

public class PsiMethodExtractor {
    public static PsiMethod extract(PsiFile psiFile, int offset) {
        PsiElement element = psiFile.findElementAt(offset);
        return PsiTreeUtil.getParentOfType(element, PsiMethod.class);
    }
}
