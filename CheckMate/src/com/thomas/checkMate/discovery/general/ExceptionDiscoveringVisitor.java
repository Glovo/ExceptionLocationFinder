package com.thomas.checkMate.discovery.general;

import com.intellij.psi.*;
import com.intellij.psi.impl.compiled.ClsMethodImpl;
import com.intellij.refactoring.psi.MethodInheritanceUtils;
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
        if (psiMethod != null) {
            visitMethod(psiMethod);
        }
        super.visitCallExpression(callExpression);
    }

//    public void visitSourceOf(PsiMethod psiMethod) {
//        PsiMethod sourceMethod = null;
//        if (psiMethod instanceof ClsMethodImpl) {
//            sourceMethod = ((ClsMethodImpl) psiMethod).getSourceMirrorMethod();
//        }
//        if (sourceMethod != null && !alreadyVisited(sourceMethod)) {
//            super.visitMethod(sourceMethod);
//        } else {
//            super.visitMethod(psiMethod);
//        }
//    }

    public void visitMethod(PsiMethod method) {
        visitSource(method);
        Set<PsiMethod> siblings = MethodInheritanceUtils.calculateSiblingMethods(method);
        siblings.forEach(this::visitSource);
    }

    public void visitSource(PsiMethod method) {
        if (method instanceof ClsMethodImpl) {
            PsiMethod sourceMirrorMethod = ((ClsMethodImpl) method).getSourceMirrorMethod();
            if (sourceMirrorMethod != null) {
                uniqueVisit(sourceMirrorMethod);
            }
        }
        uniqueVisit(method);
    }

    public void uniqueVisit(PsiMethod method) {
        if (!visitedElements.contains(method)) {
            visitedElements.add(method);
            super.visitMethod(method);
        }
    }

    private boolean alreadyVisited(PsiElement element) {
        return visitedElements.contains(element);
    }

    public Map<PsiType, Set<DiscoveredExceptionIndicator>> getDiscoveredExceptions() {
        return discoveredExceptions;
    }
}
