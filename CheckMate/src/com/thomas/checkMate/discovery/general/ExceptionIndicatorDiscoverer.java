package com.thomas.checkMate.discovery.general;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTreeUtil;
import com.thomas.checkMate.configuration.CheckMateSettings;
import com.thomas.checkMate.discovery.general.type_resolving.ExceptionTypeResolver;
import com.thomas.checkMate.discovery.general.type_resolving.UncheckedValidator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public abstract class ExceptionIndicatorDiscoverer<T extends PsiElement> {
    private ExceptionTypeResolver<T> exceptionTypeResolver;
    private TryStatementTracker tryStatementTracker;
    private UncheckedValidator uncheckedValidator;
    private Class<T> elementClass;
    private final CheckMateSettings checkMateSettings = CheckMateSettings.getInstance();
    private static final Logger logger = Logger.getLogger(ExceptionIndicatorDiscoverer.class.getName());

    public ExceptionIndicatorDiscoverer(ExceptionTypeResolver<T> exceptionTypeResolver, Class<T> clazz) {
        this.exceptionTypeResolver = exceptionTypeResolver;
        this.elementClass = clazz;
    }

    public Map<PsiType, Set<DiscoveredExceptionIndicator>> addDiscoveries(T psiElement, Map<PsiType, Set<DiscoveredExceptionIndicator>> discoveredExceptions) {
        if (getElementClass().isAssignableFrom(psiElement.getClass())) {
            if (isIndicator(psiElement)) {
                PsiType psiType = exceptionTypeResolver.resolve(psiElement);
                if (psiType != null && !checkMateSettings.getExcBlackList().contains(psiType.getCanonicalText()) && isUnChecked(psiType) && isUncaught(psiType)) {
                    PsiMethod psiMethod = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
                    DiscoveredExceptionIndicator discoveredExceptionIndicator = new DiscoveredExceptionIndicator(psiElement, psiMethod);
                    notifyProgress(psiType.getCanonicalText(), psiMethod);
                    this.addToMap(psiType, discoveredExceptionIndicator, discoveredExceptions);
                }
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

    private void notifyProgress(String exceptionName, PsiMethod encapsulatingMethod) {
        String exceptionFound = String.format("%s in %s", exceptionName, encapsulatingMethod.getName());
        ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
        if (indicator != null) {
            indicator.setText2(exceptionFound);
        }
        logger.info(exceptionFound);
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
