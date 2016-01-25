package com.thomas.checkMate.discovery;

import com.thomas.checkMate.CheckMateTest;
import com.thomas.checkMate.configuration.CheckMateSettings;

public class ExceptionFinderTest extends CheckMateTest {
    @Override
    protected String getTestDir() {
        return "exception_finder/";
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
        expectNone();
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
        expectNone();
    }

    public void testOuterCaughtIgnored() {
        configure("OuterCaughtIgnored.java");
        expectNone();
    }

    public void testInnerSuperCaughtIgnored() {
        configure("InnerSuperCaughtIgnored.java");
        expectNone();
    }

    public void testOuterSuperCaughtIgnored() {
        configure("OuterSuperCaughtIgnored.java");
        expectNone();
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
        expectNone();
    }

    public void testOverriddenIgnored() {
        configure("OverriddenIgnored.java");
        expect(OTHER_UNCHECKED);
    }

    public void testOverrideIgnoredWhenSet() {
        configure("OverrideFound.java");
        CheckMateSettings.getInstance().setIncludeInheritors(false);
        expectNone();
    }

    public void testRepeatedMethodFound() {
        configure("RepeatedMethodFound.java");
        expect(CUSTOM_UNCHECKED);
    }

    public void testInfiniteLoopPrevented() {
        configure("InfiniteLoopPrevented.java");
        expectNone();
    }
}
