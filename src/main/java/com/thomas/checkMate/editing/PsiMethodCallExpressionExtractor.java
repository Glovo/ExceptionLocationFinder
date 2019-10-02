package com.thomas.checkMate.editing;

import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PsiMethodCallExpressionExtractor {
    private final PsiStatementExtractor psiStatementExtractor;
    private final ScopeTracker<PsiCallExpression> scopeTracker = new ScopeTracker<>(PsiCallExpression.class);

    public PsiMethodCallExpressionExtractor(PsiFile psiFile, int startOffset, int endOffSet) {
        this.psiStatementExtractor = new PsiStatementExtractor(psiFile, startOffset, endOffSet);
    }

    public PsiMethodCallExpressionExtractor(PsiStatementExtractor psiStatementExtractor) {
        this.psiStatementExtractor = psiStatementExtractor;
    }

    public Set<PsiCallExpression> extract() throws MultipleMethodException {
        Set<PsiCallExpression> selectedExpressions = new HashSet<>();
        psiStatementExtractor.extract().stream().forEach(s -> {
            Collection<PsiCallExpression> psiCallExpressions = PsiTreeUtil.findChildrenOfType(s, PsiCallExpression.class);
            psiCallExpressions.stream()
                    .filter(e -> !scopeTracker.foundInScopes(e, selectedExpressions))
                    .forEach(selectedExpressions::add);
        });
        return selectedExpressions;
    }
}
