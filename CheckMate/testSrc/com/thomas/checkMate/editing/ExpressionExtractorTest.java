package com.thomas.checkMate.editing;

import com.intellij.openapi.application.PathManager;
import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.thomas.checkMate.discovery.ExceptionFinderTest;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ExpressionExtractorTest extends LightCodeInsightFixtureTestCase {
    private static final String CUSTOM_UNCHECKED_EXPRESSION = "thrower.throwCustomUnChecked()";
    private static final String OTHER_EXPRESSION = "thrower.throwOther()";
    private static final String CONSTRUCTOR_EXPRESSION = "new Thrower()";
    private PsiMethodCallExpressionExtractor extractor;

    @Override
    protected String getTestDataPath() {
        String testOutput = PathManager.getJarPathForClass(ExceptionFinderTest.class);
        return new File(testOutput, "/../../../testData/expression_extractor").getPath();
    }

    public void testCaretInFrontExtracted() {
        configure("CaretInFrontExtracted.java");
        assertCorrectExpressionExtracted(extractor.extract(), CUSTOM_UNCHECKED_EXPRESSION);
    }

    public void testCaretInCenterExtracted() {
        configure("CaretInCenterExtracted.java");
        assertCorrectExpressionExtracted(extractor.extract(), CUSTOM_UNCHECKED_EXPRESSION);
    }

    public void testPartialSelectionExtracted() {
        configure("PartialSelectionExtracted.java");
        assertCorrectExpressionExtracted(extractor.extract(), CUSTOM_UNCHECKED_EXPRESSION);
    }

    public void testFullSelectionExtracted() {
        configure("FullSelectionExtracted.java");
        assertCorrectExpressionExtracted(extractor.extract(), CUSTOM_UNCHECKED_EXPRESSION);
    }

    public void testMultiPartialSelectionExtracted() {
        configure("MultiPartialSelectionExtracted.java");
        assertCorrectExpressionExtracted(extractor.extract(), CUSTOM_UNCHECKED_EXPRESSION, OTHER_EXPRESSION);
    }

    public void testMultiFullSelectionExtracted() {
        configure("MultiFullSelectionExtracted.java");
        assertCorrectExpressionExtracted(extractor.extract(), CUSTOM_UNCHECKED_EXPRESSION, OTHER_EXPRESSION);
    }

    public void testInBlockExtracted() {
        configure("InBlockExtracted.java");
        assertCorrectExpressionExtracted(extractor.extract(), CUSTOM_UNCHECKED_EXPRESSION, OTHER_EXPRESSION);
    }

    public void testInDeepBlockExtracted() {
        configure("InDeepBlockExtracted.java");
        assertCorrectExpressionExtracted(extractor.extract(), CUSTOM_UNCHECKED_EXPRESSION, OTHER_EXPRESSION);
    }

    public void testInAssignmentExtracted() {
        configure("InAssignmentExtracted.java");
        assertCorrectExpressionExtracted(extractor.extract(), CUSTOM_UNCHECKED_EXPRESSION);
    }

    public void testConstructorExtracted() {
        configure("ConstructorExtracted.java");
        assertCorrectExpressionExtracted(extractor.extract(), CONSTRUCTOR_EXPRESSION);
    }

    private void configure(String testFile) {
        myFixture.configureByFile(testFile);
        extractor = TestExtractorFactory.createExpressionExtractor(myFixture);
    }

    private void assertCorrectExpressionExtracted(Set<PsiCallExpression> extractions, String... criteria) {
        Set<String> extractionStrings = extractions.stream().map(PsiElement::getText).collect(Collectors.toSet());
        allCriteriaExtracted(extractionStrings, criteria);
        noFalsePositives(extractionStrings, criteria);
    }

    private void allCriteriaExtracted(Set<String> extractionStrings, String[] criteria) {
        for (String criterion : criteria) {
            if (!extractionStrings.stream().anyMatch(s -> s.equals(criterion)))
                fail(criterion + " not found in: " + extractionStrings);
        }
    }

    private void noFalsePositives(Set<String> extractionStrings, String[] criteria) {
        extractionStrings.forEach(s -> {
            if (!Arrays.asList(criteria).contains(s))
                fail(s + " should not be extracted");
        });
    }
}
