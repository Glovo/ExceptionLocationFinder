package com.thomas.checkMate.discovery.general;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;

public class DiscoveredExceptionIndicator {
    private PsiElement indicator;
    private PsiMethod encapsulatingMethod;

    public DiscoveredExceptionIndicator(PsiElement indicator, PsiMethod encapsulatingMethod) {
        this.encapsulatingMethod = encapsulatingMethod;
        this.indicator = indicator;
    }

    public PsiMethod getEncapsulatingMethod() {
        return encapsulatingMethod;
    }

    public PsiElement getIndicator() {
        return indicator;
    }
}
