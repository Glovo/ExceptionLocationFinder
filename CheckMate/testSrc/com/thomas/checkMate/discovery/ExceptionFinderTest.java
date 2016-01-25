package com.thomas.checkMate.discovery;

import com.thomas.checkMate.CheckMateTest;
import com.thomas.checkMate.configuration.CheckMateSettings;

public class ExceptionFinderTest extends CheckMateTest {
    @Override
    protected String getTestDir() {
        return "exception_finder/";
    }

    public void testCustomFound() {
        configure();
        expect(CUSTOM_UNCHECKED);
    }

    public void testRuntimeFound() {
        configure();
        expect(RUNTIME);
    }

    public void testCheckedIgnored() {
        configure();
        expectNone();
    }

    public void testSuperFound() {
        configure();
        expect(CUSTOM_UNCHECKED);
    }

    public void testInterfaceDefaultFound() {
        configure();
        expect(CUSTOM_UNCHECKED);
    }

    public void testChainingFound() {
        configure();
        expect(CUSTOM_UNCHECKED, RUNTIME);
    }

    public void testInnerCaughtIgnored() {
        configure();
        expectNone();
    }

    public void testOuterCaughtIgnored() {
        configure();
        expectNone();
    }

    public void testInnerSuperCaughtIgnored() {
        configure();
        expectNone();
    }

    public void testOuterSuperCaughtIgnored() {
        configure();
        expectNone();
    }

    public void testInnerUncaughtFound() {
        configure();
        expect(OTHER_UNCHECKED);
    }

    public void testOuterUncaughtFound() {
        configure();
        expect(OTHER_UNCHECKED);
    }

    public void testInnerToOuterUncaughtFound() {
        configure();
        expect(OTHER_UNCHECKED);
    }

    public void testConstructorFound() {
        configure();
        expect(RUNTIME);
    }

    public void testThrowDocFound() {
        configure();
        CheckMateSettings.getInstance().setIncludeJavaDocs(true);
        expect(CUSTOM_UNCHECKED);
    }

    public void testDocsIgnoredWhenSet() {
        configure("ThrowDocFound.java");
        CheckMateSettings.getInstance().setIncludeJavaDocs(false);
        expectNone();
    }

    public void testOverriddenIgnored() {
        configure();
        expect(OTHER_UNCHECKED);
    }

    public void testOverrideIgnoredWhenSet() {
        configure("OverrideFound.java");
        CheckMateSettings.getInstance().setIncludeInheritors(false);
        expectNone();
    }

    public void testRepeatedMethodFound() {
        configure();
        expect(CUSTOM_UNCHECKED);
    }

    public void testInfiniteLoopPrevented() {
        configure();
        expectNone();
    }
}
