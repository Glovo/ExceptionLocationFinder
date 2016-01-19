package com.thomas.checkMate.writing;

import com.intellij.openapi.application.PathManager;
import com.intellij.psi.PsiType;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.thomas.checkMate.discovery.ExceptionFinderTestUtil;
import com.thomas.checkMate.editing.PsiStatementExtractor;
import com.thomas.checkMate.editing.TestExtractorFactory;
import com.thomas.checkMate.util.TestExceptionFinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TryCatchStatementWriterTest extends LightCodeInsightFixtureTestCase {
    private static final String TEST_FILE_DIR = "writing/";

    @Override
    protected String getTestDataPath() {
        String testOutput = PathManager.getJarPathForClass(TryCatchStatementWriterTest.class);
        return new File(testOutput, "/../../../testData").getPath();
    }

    public void testRuntimeWritten() {
        configure("BeforeRuntimeWritten.java");
        writeStatement(findExceptionTypes());
        checkResult("AfterRuntimeWritten.java");
    }

    public void testCustomWritten() {
        configure("BeforeCustomWritten.java");
        writeStatement(findExceptionTypes());
        checkResult("AfterCustomWritten.java");
    }

    public void testImportWritten() {
        configure("BeforeImportWritten.java");
        writeStatement(findExceptionTypes());
        checkResult("AfterImportWritten.java");
    }

    public void testExceptionHierarchyRespected() {
        configure("BeforeExceptionHierarchyWritten.java");
        writeStatement(findExceptionTypes());
        checkResult("AfterExceptionHierarchyWritten.java");
    }

    public void testExistingWrittenSuper() {
        configure("BeforeExistingWrittenSuper.java");
        writeStatement(findExceptionTypes());
        checkResult("AfterExistingWrittenSuper.java");
    }

    private void configure(String... testFiles) {
        ExceptionFinderTestUtil.configure(myFixture, TEST_FILE_DIR, testFiles);
    }

    private void checkResult(String afterFile) {
        myFixture.checkResultByFile(TEST_FILE_DIR + afterFile);
    }

    private Set<PsiType> findExceptionTypes() {
        return TestExceptionFinder.findExceptions(myFixture).keySet();
    }

    private void writeStatement(Set<PsiType> exceptionTypes) {
        PsiStatementExtractor extractor = TestExtractorFactory.createStatementExtractor(myFixture);
        List<PsiType> exceptionList = new ArrayList<>();
        exceptionList.addAll(exceptionTypes);
        new TryCatchStatementWriter(myFixture.getProject(), extractor.extract(), exceptionList).write();
    }
}
