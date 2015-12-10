package com.thomas.checkMate.writing;

import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiStatement;
import com.intellij.psi.PsiTryStatement;

import java.util.List;

public class TryStatementWriter {

    private final PsiElementFactory elementFactory;

    public TryStatementWriter(PsiElementFactory psiElementFactory) {
        this.elementFactory = psiElementFactory;
    }

    public PsiTryStatement write(List<PsiStatement> statementsToSurround) {
        StringBuilder sb = new StringBuilder("try{");
        for (PsiStatement psiStatement : statementsToSurround) {
            sb.append(psiStatement.getText());
        }
        sb.append("}");
        return (PsiTryStatement) elementFactory.createStatementFromText(sb.toString(), null);
    }
}
