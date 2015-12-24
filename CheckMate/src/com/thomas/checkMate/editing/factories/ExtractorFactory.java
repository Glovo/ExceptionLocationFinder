package com.thomas.checkMate.editing.factories;

import com.intellij.psi.PsiFile;
import com.thomas.checkMate.editing.PsiMethodCallExpressionExtractor;
import com.thomas.checkMate.editing.PsiStatementExtractor;

public class ExtractorFactory {

    public static PsiStatementExtractor createStatementExtractor(PsiFile psiFile, int startOffset, int endOffset) {
        return new PsiStatementExtractor(psiFile, startOffset, endOffset);
    }

    public static PsiMethodCallExpressionExtractor createMethodCallExpressionExtractor(PsiFile psiFile, int startOffset, int endOffset) {
        return new PsiMethodCallExpressionExtractor(createStatementExtractor(psiFile, startOffset, endOffset));
    }

    public static PsiMethodCallExpressionExtractor createMethodCallExpressionExtractor(PsiStatementExtractor statementExtractor) {
        return new PsiMethodCallExpressionExtractor(statementExtractor);
    }
}
