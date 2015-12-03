package com.thomas.checkMate;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ThrowStatementVisitor extends PsiRecursiveElementVisitor {
    private Map<PsiThrowStatement, Set<PsiMethod>> throwStatements = new HashMap<>();
    private TryStatementTracker tryStatementTracker;
    private ExceptionTypeAnalyser exceptionTypeAnalyser;

    public ThrowStatementVisitor(PsiMethod methodToVisit) {
        this.exceptionTypeAnalyser = new ExceptionTypeAnalyser(methodToVisit.getManager(), methodToVisit.getResolveScope());
        this.tryStatementTracker = new TryStatementTracker();
        methodToVisit.accept(this);
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
        if (throwStatements.containsKey(psiThrowStatement)) {
            throwStatements.get(psiThrowStatement).add(psiMethod);
        } else {
            Set<PsiMethod> psiMethods = new HashSet<>();
            psiMethods.add(psiMethod);
            throwStatements.put(psiThrowStatement, psiMethods);
        }
    }

    public Map<PsiThrowStatement, Set<PsiMethod>> getThrowStatements() {
        for (Map.Entry<PsiThrowStatement, Set<PsiMethod>> entry : throwStatements.entrySet()) {
            System.out.println(entry.getKey().getText());
            for (PsiMethod psiMethod : entry.getValue()) {
                System.out.println("\t" + psiMethod);
            }
        }
        return throwStatements;
    }
}
