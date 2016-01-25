package com.thomas.checkMate.util;

import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiType;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.thomas.checkMate.discovery.ExceptionFinder;
import com.thomas.checkMate.discovery.factories.DiscovererFactory;
import com.thomas.checkMate.discovery.general.Discovery;
import com.thomas.checkMate.discovery.general.ExceptionIndicatorDiscoverer;
import com.thomas.checkMate.editing.PsiMethodCallExpressionExtractor;
import com.thomas.checkMate.editing.TestExtractorFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static junit.framework.TestCase.fail;

public class TestExceptionFinder {

    public static Map<PsiType, Set<Discovery>> findExceptions(CodeInsightTestFixture myFixture) {
        List<ExceptionIndicatorDiscoverer> discoverers = DiscovererFactory.createSelectedDiscovers(myFixture.getProject());
        PsiMethodCallExpressionExtractor expressionExtractor = TestExtractorFactory.createExpressionExtractor(myFixture);
        Set<PsiCallExpression> expressions = expressionExtractor.extract();
        if (expressions.size() < 1) {
            fail("No expressions found in selection");
        }
        return ExceptionFinder.find(expressionExtractor.extract(), discoverers);
    }
}
