package com.thomas.checkMate.discovery.doc_throw_tag;

import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiType;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;
import com.thomas.checkMate.discovery.general.ExceptionTypeResolver;

public class DocTagTypeResolver implements ExceptionTypeResolver<PsiDocTag> {
    private final PsiElementFactory elementFactory;

    public DocTagTypeResolver(PsiElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    @Override
    public PsiType resolve(PsiDocTag psiDocTag) {
        PsiDocTagValue value = psiDocTag.getValueElement();
        if (value != null) {
            return elementFactory.createTypeFromText(value.getText(), null);
        }
        return null;
    }
}
