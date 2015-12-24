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

    @Override
    public int hashCode() {
        return indicator.hashCode() + encapsulatingMethod.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscoveredExceptionIndicator)) return false;
        DiscoveredExceptionIndicator that = (DiscoveredExceptionIndicator) o;
        return getIndicator().equals(that.getIndicator()) && getEncapsulatingMethod().equals(that.getEncapsulatingMethod());
    }
}
