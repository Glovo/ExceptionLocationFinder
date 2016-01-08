package com.thomas.checkMate.editing;

import com.intellij.openapi.editor.Caret;
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture;
import com.thomas.checkMate.editing.factories.ExtractorFactory;
import junit.framework.TestCase;

public class TestExtractorFactory {
    public static PsiMethodCallExpressionExtractor createExpressionExtractor(JavaCodeInsightTestFixture fixture) {
        Caret currentCaret = getCaret(fixture);
        return ExtractorFactory.createMethodCallExpressionExtractor(fixture.getFile(), currentCaret.getSelectionStart(), currentCaret.getSelectionEnd());
    }

    public static PsiStatementExtractor createStatementExtractor(JavaCodeInsightTestFixture fixture) {
        Caret currentCaret = getCaret(fixture);
        return ExtractorFactory.createStatementExtractor(fixture.getFile(), currentCaret.getSelectionStart(), currentCaret.getSelectionEnd());
    }

    private static Caret getCaret(JavaCodeInsightTestFixture fixture) {
        Caret currentCaret = fixture.getEditor().getCaretModel().getCurrentCaret();
        if (currentCaret.getSelectionStart() == 0 && currentCaret.getSelectionEnd() == 0)
            TestCase.fail("No valid caret found in test file");
        return currentCaret;
    }
}
