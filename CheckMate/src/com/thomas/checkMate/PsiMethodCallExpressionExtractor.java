package com.thomas.checkMate;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiStatement;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PsiMethodCallExpressionExtractor {
    private final PsiFile psiFile;
    private final int startOffset;
    private final int endOffset;

    public PsiMethodCallExpressionExtractor(PsiFile psiFile, int startOffset, int endOffSet) {
        this.psiFile = psiFile;
        this.startOffset = startOffset;
        this.endOffset = endOffSet;
    }

    public Set<PsiMethodCallExpression> extract() {
        Set<PsiMethodCallExpression> selectedExpressions = new HashSet<>();
        for (int i = startOffset; i <= endOffset; i++) {
            PsiElement psiElement = psiFile.findElementAt(i);
            if (psiElement != null) {
                PsiStatement psiStatement = PsiTreeUtil.getParentOfType(psiElement, PsiStatement.class);
                if (psiStatement != null) {
                    Collection<PsiMethodCallExpression> psiMethodCallExpressions = PsiTreeUtil.findChildrenOfType(psiStatement, PsiMethodCallExpression.class);
                    psiMethodCallExpressions.stream().forEach(mce -> addIfNotFoundInLargerScope(mce, selectedExpressions));
                }
            }
        }
        return selectedExpressions;
    }

    private void addIfNotFoundInLargerScope(PsiMethodCallExpression methodCallExpression, Set<PsiMethodCallExpression> previouslyDiscoveredExceptions) {
        boolean foundInLargerScope = previouslyDiscoveredExceptions.stream()
                .map(mce -> PsiTreeUtil.findChildrenOfType(mce, PsiMethodCallExpression.class))
                .flatMap(Collection::stream)
                .anyMatch(c -> c.equals(methodCallExpression));
        if (!foundInLargerScope)
            previouslyDiscoveredExceptions.add(methodCallExpression);
    }
}
