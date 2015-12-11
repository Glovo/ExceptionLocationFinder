package com.thomas.checkMate.discovery.general;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class ExceptionIndicatorDiscoverer<T extends PsiElement> {
    private ExceptionTypeResolver<T> exceptionTypeResolver;
    private TryStatementTracker tryStatementTracker;
    private UncheckedValidator uncheckedValidator;
    private Class<T> elementClass;

    public ExceptionIndicatorDiscoverer(ExceptionTypeResolver<T> exceptionTypeResolver, Class<T> clazz) {
        this.exceptionTypeResolver = exceptionTypeResolver;
        this.elementClass = clazz;
    }

    public Map<PsiType, Set<DiscoveredExceptionIndicator>> addDiscoveries(T psiElement, Map<PsiType, Set<DiscoveredExceptionIndicator>> discoveredExceptions) {
        if (isIndicator(psiElement)) {
            PsiType psiType = exceptionTypeResolver.resolve(psiElement);
            if (psiType != null && isUnChecked(psiType) && isUncaught(psiType)) {
                PsiMethod psiMethod = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
                DiscoveredExceptionIndicator discoveredExceptionIndicator = new DiscoveredExceptionIndicator(psiElement, psiMethod);
                this.addToMap(psiType, discoveredExceptionIndicator, discoveredExceptions);
            }
        }
        return discoveredExceptions;
    }

    protected abstract boolean isIndicator(T psiElement);

    private boolean isUncaught(PsiType psiType) {
        return tryStatementTracker.isNotCaughtByEnclosingCatchSections(psiType);
    }

    private boolean isUnChecked(PsiType psiType) {
        return uncheckedValidator.isUncheckedOrError(psiType);
    }

    private void addToMap(PsiType psiType, DiscoveredExceptionIndicator discoveredExceptionIndicator, Map<PsiType, Set<DiscoveredExceptionIndicator>> discoveredExceptions) {
        if (discoveredExceptions.containsKey(psiType)) {
            discoveredExceptions.get(psiType).add(discoveredExceptionIndicator);
        } else {
            Set<DiscoveredExceptionIndicator> discoveredThrowStatements = new HashSet<>();
            discoveredThrowStatements.add(discoveredExceptionIndicator);
            discoveredExceptions.put(psiType, discoveredThrowStatements);
        }
    }

    public void setTryStatementTracker(TryStatementTracker tryStatementTracker) {
        this.tryStatementTracker = tryStatementTracker;
    }

    public void setUncheckedValidator(UncheckedValidator uncheckedValidator) {
        this.uncheckedValidator = uncheckedValidator;
    }

    public Class<T> getElementClass() {
        return elementClass;
    }
}
