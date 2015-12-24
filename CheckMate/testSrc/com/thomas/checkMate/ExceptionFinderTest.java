package com.thomas.checkMate;

import com.intellij.openapi.application.PathManager;
import com.intellij.psi.PsiType;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.thomas.checkMate.discovery.ExceptionFinder;
import com.thomas.checkMate.discovery.factories.DiscovererFactory;
import com.thomas.checkMate.discovery.general.DiscoveredExceptionIndicator;
import com.thomas.checkMate.discovery.general.ExceptionIndicatorDiscoverer;
import com.thomas.checkMate.editing.PsiMethodCallExpressionExtractor;
import com.thomas.checkMate.editing.TestExtractorFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ExceptionFinderTest extends LightCodeInsightFixtureTestCase {
    private static final String CUSTOM_UNCHECKED = "base.CustomUncheckedException";
    private static final String OTHER_UNCHECKED = "base.other_package.OtherCustomUncheckedException";
    private static final String RUNTIME = "RuntimeException";


    @Override
    protected String getTestDataPath() {
        String testOutput = PathManager.getJarPathForClass(ExceptionFinderTest.class);
        return new File(testOutput, "/../../../testData").getPath();
    }


    public void testCustomFound() {
        configure("BeforeCustomFound.java");
        assertCorrectExceptionsFound(findExceptions().keySet(), CUSTOM_UNCHECKED);
    }

    public void testRuntimeFound() {
        configure("RuntimeFound.java");
        assertCorrectExceptionsFound(findExceptions().keySet(), RUNTIME);
    }

    public void testCheckedIgnored() {
        configure("CheckedIgnored.java");
        assertEquals(0, findExceptions().keySet().size());
    }

    public void testSuperFound() {
        configure("SuperFound.java");
        assertCorrectExceptionsFound(findExceptions().keySet(), CUSTOM_UNCHECKED);
    }

    public void testInterfaceDefaultFound() {
        configure("InterfaceDefaultFound.java");
        assertCorrectExceptionsFound(findExceptions().keySet(), CUSTOM_UNCHECKED);
    }

    public void testChainingFound() {
        configure("ChainingFound.java");
        assertCorrectExceptionsFound(findExceptions().keySet(), CUSTOM_UNCHECKED, RUNTIME);
    }

    public void testInnerCaughtIgnored() {
        configure("InnerCaughtIgnored.java");
        assertEquals(0, findExceptions().keySet().size());
    }

    public void testOuterCaughtIgnored() {
        configure("OuterCaughtIgnored.java");
        assertEquals(0, findExceptions().keySet().size());
    }

    public void testInnerSuperCaughtIgnored() {
        configure("InnerSuperCaughtIgnored.java");
        assertEquals(0, findExceptions().keySet().size());
    }

    public void testOuterSuperCaughtIgnored() {
        configure("OuterSuperCaughtIgnored.java");
        assertEquals(0, findExceptions().keySet().size());
    }

    public void testInnerUncaughtFound() {
        configure("InnerUncaughtFound.java");
        assertCorrectExceptionsFound(findExceptions().keySet(), OTHER_UNCHECKED);
    }

    public void testOuterUncaughtFound() {
        configure("OuterUncaughtFound.java");
        assertCorrectExceptionsFound(findExceptions().keySet(), OTHER_UNCHECKED);
    }

    public void testInnerToOuterUncaughtFound() {
        configure("InnerToOuterUncaughtFound.java");
        assertCorrectExceptionsFound(findExceptions().keySet(), OTHER_UNCHECKED);
    }

    public void testConstructorFound() {
        configure("ConstructorFound.java");
        assertCorrectExceptionsFound(findExceptions().keySet(), RUNTIME);
    }

    public void testThrowDocFound() {
        configure("ThrowDocFound.java");
        assertCorrectExceptionsFound(findExceptions().keySet(), CUSTOM_UNCHECKED);
    }

    private Map<PsiType, Set<DiscoveredExceptionIndicator>> findExceptions() {
        PsiMethodCallExpressionExtractor expressionExtractor = TestExtractorFactory.createExpressionExtractor(myFixture);
        List<ExceptionIndicatorDiscoverer> allDiscoverers = DiscovererFactory.createAllDiscoverers(this.myFixture.getProject());
        return ExceptionFinder.find(expressionExtractor.extract(), allDiscoverers);
    }

    private void assertCorrectExceptionsFound(Set<PsiType> types, String... criteria) {
        List<String> typeStrings = types.stream().map(PsiType::getCanonicalText).collect(Collectors.<String>toList());
        assertAllExceptionsFound(typeStrings, criteria);
        assertNoFalsePositive(typeStrings, criteria);
    }

    private void assertAllExceptionsFound(List<String> types, String... criteria) {
        for (String exception : criteria) {
            if (!types.stream().anyMatch(ts -> ts.equals(exception))) {
                fail(exception + " not found in " + types + ".");
            }
        }
    }

    private void assertNoFalsePositive(List<String> types, String... criteria) {
        types.stream().forEach(ts -> {
            if (!Arrays.asList(criteria).contains(ts))
                fail(ts + " should not have been found.");
        });
    }

    private void configure(String... testFiles) {
        ExceptionFinderTestUtil.configure(myFixture, testFiles);
    }

}
