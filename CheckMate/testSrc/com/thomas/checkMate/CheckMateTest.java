package com.thomas.checkMate;

import com.intellij.openapi.application.PathManager;
import com.intellij.psi.PsiType;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.thomas.checkMate.util.ConfigurationUtil;
import com.thomas.checkMate.util.TestExceptionFinder;
import com.thomas.checkMate.util.WinTempFileVisitor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class CheckMateTest extends LightCodeInsightFixtureTestCase {
    private boolean cleaned;
    protected static final String CUSTOM_UNCHECKED = "base.CustomUncheckedException";
    protected static final String OTHER_UNCHECKED = "base.other_package.OtherCustomUncheckedException";
    protected static final String RUNTIME = "RuntimeException";

    @Override
    final protected String getTestDataPath() {
        String testOutput = PathManager.getJarPathForClass(CheckMateTest.class);
        return new File(testOutput, "/../../../testData").getPath();
    }

    @Override
    final protected void setUp() throws Exception {
        super.setUp();
        if (!cleaned) {
            //Clean temp files to prevent random stub index error.
            //TODO: find out cause of stub index error
            if (System.getProperty("os.name").toUpperCase().contains("WIN")) {
                Path path = Paths.get(System.getenv("AppData"), "../Local/Temp");
                Files.walkFileTree(path, new WinTempFileVisitor());
            }
            cleaned = true;
        }
    }

    final protected void expectNone() {
        assertEquals(0, TestExceptionFinder.findExceptions(myFixture).keySet().size());
    }

    final protected void expect(String... criteria) {
        Set<PsiType> types = TestExceptionFinder.findExceptions(myFixture).keySet();
        List<String> typeStrings = types.stream().map(PsiType::getCanonicalText).collect(Collectors.<String>toList());
        assertAllExceptionsFound(typeStrings, criteria);
        assertNoFalsePositive(typeStrings, criteria);
    }

    final protected void assertAllExceptionsFound(List<String> types, String... criteria) {
        for (String exception : criteria) {
            if (!types.stream().anyMatch(ts -> ts.equals(exception))) {
                fail(exception + " not found in " + types + ".");
            }
        }
    }

    final protected void assertNoFalsePositive(List<String> types, String... criteria) {
        types.stream().forEach(ts -> {
            if (!Arrays.asList(criteria).contains(ts))
                fail(ts + " should not have been found.");
        });
    }

    protected abstract String getTestDir();

    protected void configure(String... testFiles) {
        ConfigurationUtil.configure(myFixture, getTestDir(), testFiles);
    }

    protected void configure() {
        configure(getTestName(false) + ".java");
    }
}
