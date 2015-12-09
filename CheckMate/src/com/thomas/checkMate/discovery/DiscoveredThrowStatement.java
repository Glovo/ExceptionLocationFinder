package com.thomas.checkMate.discovery;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiThrowStatement;

public class DiscoveredThrowStatement {
    private PsiThrowStatement psiThrowStatement;
    private PsiMethod encapsulatingMethod;

    public DiscoveredThrowStatement(PsiThrowStatement psiThrowStatement, PsiMethod encapsulatingMethod) {
        this.psiThrowStatement = psiThrowStatement;
        this.encapsulatingMethod = encapsulatingMethod;
    }

    public PsiThrowStatement getPsiThrowStatement() {
        return psiThrowStatement;
    }

    public PsiMethod getEncapsulatingMethod() {
        return encapsulatingMethod;
    }
}
