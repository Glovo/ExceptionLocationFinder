package com.thomas.checkMate.discovery;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.thomas.checkMate.utilities.ExceptionTypeAnalyser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ThrowStatementVisitor extends PsiRecursiveElementVisitor {
    private Map<PsiType, Set<DiscoveredThrowStatement>> discoveredExceptions = new HashMap<>();
    private TryStatementTracker tryStatementTracker;
    private ExceptionTypeAnalyser exceptionTypeAnalyser;

    public ThrowStatementVisitor(PsiElement elementToVisit) {
        this.exceptionTypeAnalyser = new ExceptionTypeAnalyser(elementToVisit.getManager(), elementToVisit.getResolveScope());
        this.tryStatementTracker = new TryStatementTracker(elementToVisit);
        elementToVisit.accept(this);
    }

    @Override
    public void visitElement(PsiElement element) {
        if (element instanceof PsiTryStatement) {
            tryStatementTracker.onTryStatementOpened((PsiTryStatement) element);
            super.visitElement(element);
            tryStatementTracker.onTryStatementClosed();
            return;
        }
        if (element instanceof PsiThrowStatement) {
            PsiThrowStatement psiThrowStatement = (PsiThrowStatement) element;
            PsiType psiType = exceptionTypeAnalyser.getExceptionTypeOfThrowStatement(psiThrowStatement);
            if (psiType != null && exceptionTypeAnalyser.isUncheckedOrError(psiType) && tryStatementTracker.isNotCaughtByEnclosingCatchSections(psiType)) {
                this.addToMap(psiThrowStatement);
            }
        }
        if (element instanceof PsiCallExpression) {
            PsiCallExpression psiCallExpression = (PsiCallExpression) element;
            PsiMethod psiMethod = psiCallExpression.resolveMethod();
            if (psiMethod != null) {
                super.visitElement(psiMethod);
            }
        }
        super.visitElement(element);
    }

    private void addToMap(PsiThrowStatement psiThrowStatement) {
        PsiMethod psiMethod = PsiTreeUtil.getParentOfType(psiThrowStatement, PsiMethod.class);
        PsiType psiType = exceptionTypeAnalyser.getExceptionTypeOfThrowStatement(psiThrowStatement);
        DiscoveredThrowStatement discoveredThrowStatement = new DiscoveredThrowStatement(psiThrowStatement, psiMethod);
        if (discoveredExceptions.containsKey(psiType)) {
            discoveredExceptions.get(psiType).add(discoveredThrowStatement);
        } else {
            Set<DiscoveredThrowStatement> discoveredThrowStatements = new HashSet<>();
            discoveredThrowStatements.add(discoveredThrowStatement);
            discoveredExceptions.put(psiType, discoveredThrowStatements);
        }
    }

    public Map<PsiType, Set<DiscoveredThrowStatement>> getDiscoveredExceptions() {
        return discoveredExceptions;
    }
}
