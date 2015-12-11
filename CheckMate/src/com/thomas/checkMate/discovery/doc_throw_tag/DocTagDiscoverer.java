package com.thomas.checkMate.discovery.doc_throw_tag;

import com.intellij.psi.javadoc.PsiDocTag;
import com.thomas.checkMate.discovery.general.ExceptionIndicatorDiscoverer;
import com.thomas.checkMate.discovery.general.ExceptionTypeResolver;

public class DocTagDiscoverer extends ExceptionIndicatorDiscoverer<PsiDocTag> {
    public DocTagDiscoverer(ExceptionTypeResolver<PsiDocTag> exceptionTypeResolver) {
        super(exceptionTypeResolver, PsiDocTag.class);
    }

    @Override
    protected boolean isIndicator(PsiDocTag psiDocTag) {
        return psiDocTag.getName().equals("throws");
    }
}
