package com.thomas.checkMate.editing;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiStatement;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.ArrayList;
import java.util.List;

public class PsiStatementExtractor {
    private final PsiFile psiFile;
    private final int startOffset;
    private final int endOffset;
    private final ScopeTracker<PsiStatement> scopeTracker;

    public PsiStatementExtractor(PsiFile psiFile, int startOffset, int endOffset) {
        this.psiFile = psiFile;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.scopeTracker = new ScopeTracker<>(PsiStatement.class);
    }

    public List<PsiStatement> extract() {
        List<PsiStatement> selectedStatements = new ArrayList<>();
        for (int i = startOffset; i <= endOffset; i++) {
            PsiElement psiElement = psiFile.findElementAt(i);
            if (psiElement != null) {
                PsiStatement psiStatement = PsiTreeUtil.getParentOfType(psiElement, PsiStatement.class);
                if (psiStatement != null && !selectedStatements.contains(psiStatement) && !scopeTracker.foundInScopes(psiStatement, selectedStatements)) {
                    selectedStatements.add(psiStatement);
                    scopeTracker.removeSmallerScopes(psiStatement, selectedStatements);
                }
            }
        }
        return selectedStatements;
    }
}
