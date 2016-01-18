package com.thomas.checkMate.util;

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

public class TestExceptionFinder {

    public static Map<PsiType, Set<Discovery>> findExceptions(CodeInsightTestFixture myFixture) {
        List<ExceptionIndicatorDiscoverer> discoverers = DiscovererFactory.createSelectedDiscovers(myFixture.getProject());
        PsiMethodCallExpressionExtractor expressionExtractor = TestExtractorFactory.createExpressionExtractor(myFixture);
        return ExceptionFinder.find(expressionExtractor.extract(), discoverers);
    }
}
