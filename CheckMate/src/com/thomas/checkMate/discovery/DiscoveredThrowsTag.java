package com.thomas.checkMate.discovery;

import com.intellij.psi.PsiMethod;

public class DiscoveredThrowsTag extends DiscoveredExceptionIndicator {
    private PsiMethod encapsulatingMethod;
    private String exceptionString;

    public DiscoveredThrowsTag(String exceptionString, PsiMethod encapsulatingMethod) {
        super(encapsulatingMethod, IndicationType.DOC_THROW_TAG);
        this.encapsulatingMethod = encapsulatingMethod;
        this.exceptionString = exceptionString;
    }

    public PsiMethod getEncapsulatingMethod() {
        return encapsulatingMethod;
    }

    public String getExceptionString() {
        return exceptionString;
    }
}
