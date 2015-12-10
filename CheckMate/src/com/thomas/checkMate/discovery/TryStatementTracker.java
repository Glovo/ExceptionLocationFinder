package com.thomas.checkMate.discovery;

import com.intellij.psi.PsiCatchSection;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiTryStatement;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;
import com.thomas.checkMate.utilities.CatchSectionUtil;

import java.util.*;

public class TryStatementTracker {
    private Stack<Set<PsiType>> exceptionTypesStack = new Stack<>();

    public TryStatementTracker(PsiElement psiElement) {
        handleEnclosingTryStatements(psiElement);
    }

    private void handleEnclosingTryStatements(PsiElement psiElement) {
        PsiTryStatement tryStatement = PsiTreeUtil.getParentOfType(psiElement, PsiTryStatement.class);
        if (tryStatement != null) {
            onTryStatementOpened(tryStatement);
            handleEnclosingTryStatements(tryStatement);
        }
    }

    public void onTryStatementOpened(PsiTryStatement psiTryStatement) {
        PsiCatchSection[] catchSections = psiTryStatement.getCatchSections();
        exceptionTypesStack.push(CatchSectionUtil.getExceptionTypes(catchSections));
    }

    public void onTryStatementClosed() {
        exceptionTypesStack.pop();
    }

    public boolean isNotCaughtByEnclosingCatchSections(PsiType psiType) {
        List<PsiType> typeAndSupers = new ArrayList<>();
        typeAndSupers.add(psiType);
        typeAndSupers.addAll(Arrays.asList(psiType.getSuperTypes()));
        for (Set<PsiType> psiTypes : exceptionTypesStack) {
            for (PsiType type : typeAndSupers) {
                if (psiTypes.contains(type)) {
                    return false;
                }
            }
        }
        return true;
    }
}
