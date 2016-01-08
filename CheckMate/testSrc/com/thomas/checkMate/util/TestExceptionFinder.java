package com.thomas.checkMate.util;

import com.intellij.psi.PsiType;
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture;
import com.thomas.checkMate.discovery.ExceptionFinder;
import com.thomas.checkMate.discovery.factories.DiscovererFactory;
import com.thomas.checkMate.discovery.general.DiscoveredExceptionIndicator;
import com.thomas.checkMate.discovery.general.ExceptionIndicatorDiscoverer;
import com.thomas.checkMate.editing.PsiMethodCallExpressionExtractor;
import com.thomas.checkMate.editing.TestExtractorFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestExceptionFinder {

    public static Map<PsiType, Set<DiscoveredExceptionIndicator>> findExceptions(JavaCodeInsightTestFixture myFixture) {
        PsiMethodCallExpressionExtractor expressionExtractor = TestExtractorFactory.createExpressionExtractor(myFixture);
        List<ExceptionIndicatorDiscoverer> allDiscoverers = DiscovererFactory.createAllDiscoverers(myFixture.getProject());
        return ExceptionFinder.find(expressionExtractor.extract(), allDiscoverers);
    }
}
