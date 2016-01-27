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
import com.thomas.checkMate.configuration.util.BlackListUtil;

import java.util.logging.Logger;

public abstract class ExceptionIndicatorDiscoverer<T extends PsiElement> {
    private static final Logger logger = Logger.getLogger(ExceptionIndicatorDiscoverer.class.getName());
    private final CheckMateSettings checkMateSettings = CheckMateSettings.getInstance();
    private ExceptionTypeResolver<T> exceptionTypeResolver;
    private UncheckedValidator uncheckedValidator;
    private Class<T> elementClass;

    public ExceptionIndicatorDiscoverer(ExceptionTypeResolver<T> exceptionTypeResolver, Class<T> clazz) {
        this.exceptionTypeResolver = exceptionTypeResolver;
        this.elementClass = clazz;
    }

    public Discovery discover(T psiElement) {
        if (getElementClass().isAssignableFrom(psiElement.getClass())) {
            if (isIndicator(psiElement)) {
                PsiType psiType = exceptionTypeResolver.resolve(psiElement);
                if (psiType != null && BlackListUtil.isAllowed(psiType, checkMateSettings.getExcBlackList()) && isUnChecked(psiType)) {
                    PsiMethod psiMethod = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
                    Discovery discovery = new Discovery(psiType, psiElement, psiMethod);
                    notifyProgress(psiType.getCanonicalText(), psiMethod);
                    return discovery;
                }
            }
        }
        return null;
    }

    protected abstract boolean isIndicator(T psiElement);

    private boolean isUnChecked(PsiType psiType) {
        return uncheckedValidator.isUncheckedOrError(psiType);
    }

    private void notifyProgress(String exceptionName, PsiMethod encapsulatingMethod) {
        String exceptionFound = String.format("%s in %s", exceptionName, encapsulatingMethod.getName());
        ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
        if (indicator != null) {
            indicator.setText2(exceptionFound);
        }
        logger.info(exceptionFound);
    }

    public void setUncheckedValidator(UncheckedValidator uncheckedValidator) {
        this.uncheckedValidator = uncheckedValidator;
    }

    public Class<T> getElementClass() {
        return elementClass;
    }
}
