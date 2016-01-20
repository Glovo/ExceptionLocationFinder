package com.thomas.checkMate.discovery;

import com.intellij.openapi.application.PathManager;
import com.intellij.psi.PsiType;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.thomas.checkMate.configuration.CheckMateSettings;
import com.thomas.checkMate.discovery.general.Discovery;
import com.thomas.checkMate.util.TestExceptionFinder;
import com.thomas.checkMate.util.WinTempFileVisitor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ExceptionFinderTest extends LightCodeInsightFixtureTestCase {
    private static final String CUSTOM_UNCHECKED = "base.CustomUncheckedException";
    private static final String OTHER_UNCHECKED = "base.other_package.OtherCustomUncheckedException";
    private static final String RUNTIME = "RuntimeException";
    private static final String TEST_FILE_DIR = "exception_finder/";
    private static boolean cleaned;

    @Override
    protected String getTestDataPath() {
        String testOutput = PathManager.getJarPathForClass(ExceptionFinderTest.class);
        return new File(testOutput, "/../../../testData").getPath();
    }

    @Override
    protected void setUp() throws Exception {
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

    public void testCustomFound() {
        configure("CustomFound.java");
        expect(CUSTOM_UNCHECKED);
    }

    public void testRuntimeFound() {
        configure("RuntimeFound.java");
        expect(RUNTIME);
    }

    public void testCheckedIgnored() {
        configure("CheckedIgnored.java");
        assertEquals(0, findExceptions().keySet().size());
    }

    public void testSuperFound() {
        configure("SuperFound.java");
        expect(CUSTOM_UNCHECKED);
    }

    public void testInterfaceDefaultFound() {
        configure("InterfaceDefaultFound.java");
        expect(CUSTOM_UNCHECKED);
    }

    public void testChainingFound() {
        configure("ChainingFound.java");
        expect(CUSTOM_UNCHECKED, RUNTIME);
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
        expect(OTHER_UNCHECKED);
    }

    public void testOuterUncaughtFound() {
        configure("OuterUncaughtFound.java");
        expect(OTHER_UNCHECKED);
    }

    public void testInnerToOuterUncaughtFound() {
        configure("InnerToOuterUncaughtFound.java");
        expect(OTHER_UNCHECKED);
    }

    public void testConstructorFound() {
        configure("ConstructorFound.java");
        expect(RUNTIME);
    }

    public void testThrowDocFound() {
        configure("ThrowDocFound.java");
        CheckMateSettings.getInstance().setIncludeJavaDocs(true);
        expect(CUSTOM_UNCHECKED);
    }

    public void testDocsIgnoredWhenSet() {
        configure("ThrowDocFound.java");
        CheckMateSettings.getInstance().setIncludeJavaDocs(false);
        assertEquals(0, findExceptions().size());
    }

    public void testOverriddenIgnored() {
        configure("OverriddenIgnored.java");
        expect(OTHER_UNCHECKED);
    }

    public void testOverrideIgnoredWhenSet() {
        configure("OverrideFound.java");
        CheckMateSettings.getInstance().setIncludeInheritors(false);
        assertEquals(0, findExceptions().keySet().size());
    }

    public void testRepeatedMethodFound() {
        configure("RepeatedMethodFound.java");
        expect(CUSTOM_UNCHECKED);
    }

    public void testInfiniteLoopPrevented() {
        configure("InfiniteLoopPrevented.java");
        assertEquals(0, findExceptions().keySet().size());
    }

    public void testLocalVarInheritorsResolved() {
        configure("LocalVarInheritorsResolved.java");
        CheckMateSettings.getInstance().setIncludeInheritors(true);
        expect(OTHER_UNCHECKED);
    }

    public void testIndirectLocalVarResolved() {
        configure("IndirectLocalVarResolved.java");
        CheckMateSettings.getInstance().setIncludeInheritors(true);
        expect(OTHER_UNCHECKED);
    }

    public void testAmbiLocalVarInheritorsResolved() {
        configure("AmbiLocalVarResolved.java");
        CheckMateSettings.getInstance().setIncludeInheritors(true);
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testFieldInheritorsResolved() {
        configure("FieldInheritorsResolved.java");
        CheckMateSettings.getInstance().setIncludeInheritors(true);
        expect(OTHER_UNCHECKED);
    }

    public void testAmbiFieldInheritorsResolved() {
        configure("AmbiFieldInheritorsResolved.java");
        CheckMateSettings.getInstance().setIncludeInheritors(true);
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testParamResolved() {
        configure("ParamResolved.java");
        CheckMateSettings.getInstance().setIncludeInheritors(true);
        expect(OTHER_UNCHECKED);
    }

    public void testMultiParamResolved() {
        configure("MultiParamResolved.java");
        CheckMateSettings.getInstance().setIncludeInheritors(true);
        expect(OTHER_UNCHECKED);
    }

    public void testAmbiParamResolved() {
        configure("AmbiParamResolved.java");
        CheckMateSettings.getInstance().setIncludeInheritors(true);
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testParamLocalResolved() {
        configure("ParamLocalResolved.java");
        CheckMateSettings.getInstance().setIncludeInheritors(true);
        expect(OTHER_UNCHECKED);
    }

    public void testParamAmbiLocalResolved() {
        configure("ParamAmbiLocalResolved.java");
        CheckMateSettings.getInstance().setIncludeInheritors(true);
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testMultiParamLocalResolved() {
        configure("MultiParamLocalResolved.java");
        CheckMateSettings.getInstance().setIncludeInheritors(true);
        expect(OTHER_UNCHECKED);
    }

    public void testMultiParamAmbiLocalResolved() {
        configure("MultiParamAmbiLocalResolved.java");
        CheckMateSettings.getInstance().setIncludeInheritors(true);
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }


    public void testParamFieldResolved() {
        configure("ParamFieldResolved.java");
        CheckMateSettings.getInstance().setIncludeInheritors(true);
        expect(OTHER_UNCHECKED);
    }

    public void testParamAmbiFieldResolved() {
        configure("ParamAmbiFieldResolved.java");
        CheckMateSettings.getInstance().setIncludeInheritors(true);
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testMultiParamFieldResolved() {
        configure("MultiParamFieldResolved.java");
        CheckMateSettings.getInstance().setIncludeInheritors(true);
        expect(OTHER_UNCHECKED);
    }

    public void testMultiParamAmbiFieldResolved() {
        configure("MultiParamAmbiFieldResolved.java");
        CheckMateSettings.getInstance().setIncludeInheritors(true);
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    private Map<PsiType, Set<Discovery>> findExceptions() {
        return TestExceptionFinder.findExceptions(myFixture);
    }

    private void expect(String... criteria) {
        Set<PsiType> types = TestExceptionFinder.findExceptions(myFixture).keySet();
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
        ExceptionFinderTestUtil.configure(myFixture, TEST_FILE_DIR, testFiles);
    }
}
