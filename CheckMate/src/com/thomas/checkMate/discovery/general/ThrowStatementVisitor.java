package com.thomas.checkMate.discovery.general;

import com.intellij.psi.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThrowStatementVisitor extends PsiRecursiveElementVisitor {
    private Map<PsiType, Set<DiscoveredExceptionIndicator>> discoveredExceptions = new HashMap<>();
    private TryStatementTracker tryStatementTracker;
    private List<ExceptionIndicatorDiscoverer> discoverers;


    public ThrowStatementVisitor(PsiElement elementToVisit, List<ExceptionIndicatorDiscoverer> discoverers) {
        this.tryStatementTracker = new TryStatementTracker(elementToVisit);
        this.discoverers = discoverers;
        UncheckedValidator uncheckedValidator = new UncheckedValidator(elementToVisit.getManager(), elementToVisit.getResolveScope());
        this.discoverers.forEach(d -> {
            d.setTryStatementTracker(tryStatementTracker);
            d.setUncheckedValidator(uncheckedValidator);
        });
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
        if (element instanceof PsiCallExpression) {
            PsiCallExpression psiCallExpression = (PsiCallExpression) element;
            PsiMethod psiMethod = psiCallExpression.resolveMethod();
            if (psiMethod != null) {
                super.visitElement(psiMethod);
            }
        }
        discoverers.forEach(d -> {
            Class<PsiElement> elementClass = d.getElementClass();
            if (elementClass.isAssignableFrom(element.getClass())) {
                d.addDiscoveries(element, discoveredExceptions);
            }
        });
        super.visitElement(element);
    }

    public Map<PsiType, Set<DiscoveredExceptionIndicator>> getDiscoveredExceptions() {
        return discoveredExceptions;
    }
}
