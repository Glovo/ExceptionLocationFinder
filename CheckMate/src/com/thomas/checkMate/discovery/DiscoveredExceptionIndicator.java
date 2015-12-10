package com.thomas.checkMate.discovery;

import com.intellij.psi.PsiMethod;

public class DiscoveredExceptionIndicator {
    private PsiMethod encapsulatingMethod;
    private IndicationType indicationType;

    public DiscoveredExceptionIndicator(PsiMethod encapsulatingMethod, IndicationType indicationType) {
        this.encapsulatingMethod = encapsulatingMethod;
        this.indicationType = indicationType;
    }

    public PsiMethod getEncapsulatingMethod() {
        return encapsulatingMethod;
    }

    public IndicationType getIndicationType() {
        return indicationType;
    }
}
