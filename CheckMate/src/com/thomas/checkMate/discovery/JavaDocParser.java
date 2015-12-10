package com.thomas.checkMate.discovery;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.javadoc.PsiDocTagValue;

public class JavaDocParser {
    public static void findExceptionsInDocumentation(PsiMethod psiMethod) {
        PsiDocComment docComment = psiMethod.getDocComment();
        if (docComment != null) {
            PsiDocTag[] throwTags = docComment.findTagsByName("throws");
            for (PsiDocTag throwTag : throwTags) {
                PsiDocTagValue exceptionValue = throwTag.getValueElement();
                if (exceptionValue != null) {
                    String exceptionString = exceptionValue.getText();

                }
            }
        }
    }
}
