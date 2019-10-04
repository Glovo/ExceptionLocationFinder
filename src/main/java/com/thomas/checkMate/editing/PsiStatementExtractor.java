package com.thomas.checkMate.editing;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiStatement;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PsiStatementExtractor {
    private final PsiFile psiFile;
    private final int startOffset;
    private final int endOffset;
    private final ScopeTracker<PsiStatement> scopeTracker;
    private PsiMethod extractionMethod;

    public PsiStatementExtractor(PsiFile psiFile, int startOffset, int endOffset) {
        this.psiFile = psiFile;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.scopeTracker = new ScopeTracker<>(PsiStatement.class);
    }

    public List<PsiStatement> extract() throws MultipleMethodException {
        List<PsiStatement> selectedStatements = new ArrayList<>();
        boolean methodFound = false;
        for (int i = startOffset; i <= endOffset; i++) {
            PsiElement psiElement = psiFile.findElementAt(i);
            checkMultiple(psiElement);
            if (!methodFound && psiElement != null) {
                PsiElement parent = psiElement.getParent();
                if (parent != null && parent instanceof PsiMethod) {
                    selectedStatements = getMethodStatements(parent);
                    methodFound = true;
                } else {
                    PsiStatement psiStatement = PsiTreeUtil.getParentOfType(psiElement, PsiStatement.class);
                    addToStatements(psiStatement, selectedStatements);
                }
            }
        }
        return selectedStatements;
    }

    private void checkMultiple(PsiElement psiElement) throws MultipleMethodException {
        PsiMethod currentMethod = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
        if (extractionMethod == null) {
            extractionMethod = currentMethod;
        } else {
            if (!extractionMethod.equals(currentMethod)) {
                throw new MultipleMethodException("Can't extract statements from multiple methods");
            }
        }
    }

    private List<PsiStatement> getMethodStatements(PsiElement psiMethod) {
        List<PsiStatement> selectedStatements = new ArrayList<>();
        Collection<PsiStatement> psiStatements = PsiTreeUtil.findChildrenOfType(psiMethod, PsiStatement.class);
        psiStatements.forEach(s -> addToStatements(s, selectedStatements));
        return selectedStatements;
    }

    private void addToStatements(PsiStatement psiStatement, List<PsiStatement> selectedStatements) {
        if (psiStatement != null && !selectedStatements.contains(psiStatement) && !scopeTracker.foundInScopes(psiStatement, selectedStatements)) {
            selectedStatements.add(psiStatement);
            scopeTracker.removeSmallerScopes(psiStatement, selectedStatements);
        }
    }
}
