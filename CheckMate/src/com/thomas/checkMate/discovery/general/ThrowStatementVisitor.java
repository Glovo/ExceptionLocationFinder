package com.thomas.checkMate.discovery.general;

import com.intellij.psi.*;
import com.intellij.psi.impl.compiled.ClsMethodImpl;
import com.thomas.checkMate.discovery.general.type_resolving.UncheckedValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThrowStatementVisitor extends PsiRecursiveElementVisitor {
    private Map<PsiType, Set<DiscoveredExceptionIndicator>> discoveredExceptions = new HashMap<>();
    private TryStatementTracker tryStatementTracker;
    private List<ExceptionIndicatorDiscoverer> discoverers;
    private int stackCounter = 0;


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
        stackCounter++;
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
                psiMethod.getHierarchicalMethodSignature().getSuperSignatures().forEach(m -> {
                    super.visitElement(m.getMethod());
                });
                super.visitElement(psiMethod);
                if (psiMethod instanceof ClsMethodImpl) {
                    PsiMethod sourceMirrorMethod = ((ClsMethodImpl) psiMethod).getSourceMirrorMethod();
                    if (sourceMirrorMethod != null) {
                        super.visitElement(sourceMirrorMethod);
                    }
                }
            }
        }
        discoverers.forEach(d -> {
            Class<PsiElement> elementClass = d.getElementClass();
            if (elementClass.isAssignableFrom(element.getClass())) {
                d.addDiscoveries(element, discoveredExceptions);
            }
        });
        super.visitElement(element);
        stackCounter--;
        System.out.println(stackCounter);
    }


    public Map<PsiType, Set<DiscoveredExceptionIndicator>> getDiscoveredExceptions() {
        return discoveredExceptions;
    }
}
