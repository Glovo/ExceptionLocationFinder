package com.thomas.checkMate.discovery.throw_statement;

import com.intellij.psi.PsiThrowStatement;
import com.thomas.checkMate.discovery.general.ExceptionIndicatorDiscoverer;
import com.thomas.checkMate.discovery.general.type_resolving.ExceptionTypeResolver;

public class ThrowStatementDiscoverer extends ExceptionIndicatorDiscoverer<PsiThrowStatement> {
    public ThrowStatementDiscoverer(ExceptionTypeResolver<PsiThrowStatement> exceptionTypeResolver) {
        super(exceptionTypeResolver, PsiThrowStatement.class);
    }

    @Override
    protected boolean isIndicator(PsiThrowStatement psiThrowStatement) {
        return true;
    }
}
