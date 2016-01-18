package com.thomas.checkMate.discovery.general;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;

public class Discovery {
    private PsiType exceptionType;
    private PsiElement indicator;
    private PsiMethod encapsulatingMethod;

    public Discovery(PsiType exceptionType, PsiElement indicator, PsiMethod encapsulatingMethod) {
        this.exceptionType = exceptionType;
        this.encapsulatingMethod = encapsulatingMethod;
        this.indicator = indicator;
    }

    public PsiType getExceptionType() {
        return exceptionType;
    }

    public PsiMethod getEncapsulatingMethod() {
        return encapsulatingMethod;
    }

    public PsiElement getIndicator() {
        return indicator;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Discovery)) return false;

        Discovery discovery = (Discovery) o;
        return getExceptionType().equals(discovery.getExceptionType()) && getIndicator().equals(discovery.getIndicator()) && getEncapsulatingMethod().equals(discovery.getEncapsulatingMethod());

    }

    @Override
    public int hashCode() {
        int result = getExceptionType().hashCode();
        result = 31 * result + getIndicator().hashCode();
        result = 31 * result + getEncapsulatingMethod().hashCode();
        return result;
    }
}
