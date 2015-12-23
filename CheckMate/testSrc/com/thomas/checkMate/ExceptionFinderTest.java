package com.thomas.checkMate;

import com.intellij.openapi.application.PathManager;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.io.File;

public class ExceptionFinderTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        String testOutput = PathManager.getJarPathForClass(ExceptionFinderTest.class);
        return new File(testOutput, "/../../../testData/tests").getPath();
    }

    public void testCustomFound() {
        myFixture.configureByFile("BeforeCustomFound.java");
        myFixture.performEditorAction("findUncheckedExceptions");
        myFixture.checkResultByFile("AfterCustomFound.java");
    }
}
