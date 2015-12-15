package com.thomas.checkMate.discovery.doc_throw_tag;

import com.intellij.psi.javadoc.PsiDocTag;
import com.thomas.checkMate.discovery.general.ExceptionIndicatorDiscoverer;
import com.thomas.checkMate.discovery.general.type_resolving.ExceptionTypeResolver;

public class DocTagDiscoverer extends ExceptionIndicatorDiscoverer<PsiDocTag> {
    public DocTagDiscoverer(ExceptionTypeResolver<PsiDocTag> exceptionTypeResolver) {
        super(exceptionTypeResolver, PsiDocTag.class);
    }

    @Override
    protected boolean isIndicator(PsiDocTag psiDocTag) {
        String name = psiDocTag.getName();
        boolean hasThrowsTag = name.equals("throws");
        boolean hasExceptionTag = name.equals("exception");
        return hasThrowsTag || hasExceptionTag;
    }
}
