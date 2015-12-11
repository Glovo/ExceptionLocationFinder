package com.thomas.checkMate.discovery.throw_statement;

import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiThrowStatement;
import com.intellij.psi.PsiType;
import com.thomas.checkMate.discovery.general.ExceptionTypeResolver;

public class ThrowStatementTypeResolver implements ExceptionTypeResolver<PsiThrowStatement> {
    @Override
    public PsiType resolve(PsiThrowStatement psiThrowStatement) {
        PsiExpression exception = psiThrowStatement.getException();
        if (exception != null) {
            return exception.getType();
        }
        return null;
    }
}
