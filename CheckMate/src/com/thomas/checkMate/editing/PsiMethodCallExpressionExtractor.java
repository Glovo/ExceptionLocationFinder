package com.thomas.checkMate.editing;

import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PsiMethodCallExpressionExtractor {
    private final PsiStatementExtractor psiStatementExtractor;
    private final ScopeTracker<PsiMethodCallExpression> scopeTracker = new ScopeTracker<>(PsiMethodCallExpression.class);

    public PsiMethodCallExpressionExtractor(PsiFile psiFile, int startOffset, int endOffSet) {
        this.psiStatementExtractor = new PsiStatementExtractor(psiFile, startOffset, endOffSet);
    }

    public PsiMethodCallExpressionExtractor(PsiStatementExtractor psiStatementExtractor) {
        this.psiStatementExtractor = psiStatementExtractor;
    }

    public Set<PsiMethodCallExpression> extract() {
        Set<PsiMethodCallExpression> selectedExpressions = new HashSet<>();
        psiStatementExtractor.extract().stream().forEach(s -> {
            Collection<PsiMethodCallExpression> psiMethodCallExpressions = PsiTreeUtil.findChildrenOfType(s, PsiMethodCallExpression.class);
            psiMethodCallExpressions.stream()
                    .filter(e -> !scopeTracker.foundInScopes(e, selectedExpressions))
                    .forEach(selectedExpressions::add);
        });
        return selectedExpressions;
    }
}
