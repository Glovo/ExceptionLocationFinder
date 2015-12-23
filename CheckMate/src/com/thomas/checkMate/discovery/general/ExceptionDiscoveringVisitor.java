package com.thomas.checkMate.discovery.general;

import com.intellij.psi.*;
import com.intellij.psi.impl.compiled.ClsMethodImpl;
import com.thomas.checkMate.discovery.general.type_resolving.UncheckedValidator;

import java.util.*;

public class ExceptionDiscoveringVisitor extends JavaRecursiveElementVisitor {
    private Map<PsiType, Set<DiscoveredExceptionIndicator>> discoveredExceptions = new HashMap<>();
    private TryStatementTracker tryStatementTracker;
    private List<ExceptionIndicatorDiscoverer> discovererList;
    private Set<PsiElement> visitedElements = new HashSet<>();

    public ExceptionDiscoveringVisitor(PsiElement elementToVisit, List<ExceptionIndicatorDiscoverer> discovererList) {
        this.tryStatementTracker = new TryStatementTracker(elementToVisit);
        this.discovererList = discovererList;
        UncheckedValidator uncheckedValidator = new UncheckedValidator(elementToVisit.getManager(), elementToVisit.getResolveScope());
        this.discovererList.forEach(d -> {
            d.setTryStatementTracker(tryStatementTracker);
            d.setUncheckedValidator(uncheckedValidator);
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public void visitElement(PsiElement element) {
        discovererList.forEach(d -> d.addDiscoveries(element, discoveredExceptions));
        super.visitElement(element);
    }

    @Override
    public void visitTryStatement(PsiTryStatement statement) {
        tryStatementTracker.onTryStatementOpened(statement);
        super.visitTryStatement(statement);
        tryStatementTracker.onTryStatementClosed();
    }

    @Override
    public void visitCallExpression(PsiCallExpression callExpression) {
        PsiMethod psiMethod = callExpression.resolveMethod();
        if (psiMethod != null && !visitedElements.contains(psiMethod)) {
            visitedElements.add(psiMethod);
            visitSourceOf(psiMethod);
        }
        super.visitCallExpression(callExpression);
    }

    public void visitSourceOf(PsiMethod psiMethod) {
        if (psiMethod instanceof ClsMethodImpl) {
            PsiMethod sourceMirrorMethod = ((ClsMethodImpl) psiMethod).getSourceMirrorMethod();
            if (sourceMirrorMethod != null && !visitedElements.contains(psiMethod)) {
                visitedElements.add(sourceMirrorMethod);
                psiMethod = sourceMirrorMethod;
            }
        }
        super.visitMethod(psiMethod);
    }

    public Map<PsiType, Set<DiscoveredExceptionIndicator>> getDiscoveredExceptions() {
        return discoveredExceptions;
    }
}
