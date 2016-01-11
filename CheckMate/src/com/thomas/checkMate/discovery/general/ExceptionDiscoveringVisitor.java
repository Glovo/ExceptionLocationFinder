package com.thomas.checkMate.discovery.general;

import com.intellij.psi.*;
import com.intellij.psi.impl.compiled.ClsMethodImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.psi.MethodInheritanceUtils;
import com.thomas.checkMate.discovery.general.type_resolving.UncheckedValidator;
import com.thomas.checkMate.utilities.JavaLangUtil;

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


    public void visitMethod(PsiMethod method) {
        visitSource(method);
        Set<PsiMethod> siblings = MethodInheritanceUtils.calculateSiblingMethods(method);
        List<PsiClass> implementingClasses = Arrays.asList(MethodInheritanceUtils.findAvailableSubClassesForMethod(method));
        siblings.forEach(s -> {
            if (implementingClasses.contains(PsiTreeUtil.getParentOfType(s, PsiClass.class))) {
                visitSource(s);
            }
        });
    }

    public void visitSource(PsiMethod method) {
        boolean sourceFound = false;
        if (method instanceof ClsMethodImpl) {
            if (!JavaLangUtil.isJavaSource(method)) {
                PsiMethod sourceMirrorMethod = ((ClsMethodImpl) method).getSourceMirrorMethod();
                if (sourceMirrorMethod != null) {
                    sourceFound = true;
                    uniqueVisit(sourceMirrorMethod);
                }
            }
        }
        if (!sourceFound) {
            uniqueVisit(method);
        }
    }

    public void uniqueVisit(PsiMethod method) {
        if (!visitedElements.contains(method)) {
            visitedElements.add(method);
            super.visitMethod(method);
        }
    }

    public Map<PsiType, Set<DiscoveredExceptionIndicator>> getDiscoveredExceptions() {
        return discoveredExceptions;
    }
}
