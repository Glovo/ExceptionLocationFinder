package com.thomas.checkMate.resolving;

import com.thomas.checkMate.CheckMateTest;
import com.thomas.checkMate.configuration.CheckMateSettings;

public class ResolversTest extends CheckMateTest {
    @Override
    protected String getTestDir() {
        return "resolving/";
    }

    @Override
    protected void configure(String... testFiles) {
        super.configure(testFiles);
        CheckMateSettings.getInstance().setIncludeInheritors(true);
    }

    public void testLocalVarInheritorsResolved() {
//        configure();
        configure();
        expect(OTHER_UNCHECKED);
    }

    public void testIndirectLocalVarResolved() {
        configure();
        expect(OTHER_UNCHECKED);
    }

    public void testAmbiLocalVarResolved() {
        configure();
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testFieldInheritorsResolved() {
        configure();
        expect(OTHER_UNCHECKED);
    }

    public void testAmbiFieldResolved() {
        configure();
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testParamResolved() {
        configure();
        expect(OTHER_UNCHECKED);
    }

    public void testMultiParamResolved() {
        configure();
        expect(OTHER_UNCHECKED);
    }

    public void testAmbiParamResolved() {
        configure();
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testParamLocalResolved() {
        configure();
        expect(OTHER_UNCHECKED);
    }

    public void testParamAmbiLocalResolved() {
        configure();
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testMultiParamLocalResolved() {
        configure();
        expect(OTHER_UNCHECKED);
    }

    public void testMultiParamAmbiLocalResolved() {
        configure();
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }


    public void testParamFieldResolved() {
        configure();
        expect(OTHER_UNCHECKED);
    }

    public void testParamAmbiFieldResolved() {
        configure();
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testMultiParamFieldResolved() {
        configure();
        expect(OTHER_UNCHECKED);
    }

    public void testMultiParamAmbiFieldResolved() {
        configure();
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testExternalParamResolved() {
        configure("OtherExternalParamResolved.java", "ExternalParamResolved.java");
        expect(OTHER_UNCHECKED);
    }

    public void testParamPassResolved() {
        configure();
        expect(OTHER_UNCHECKED);
    }

    public void testParamAmbiPassResolved() {
        configure();
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testParamSuperPassResolved() {
        configure();
        expect(CUSTOM_UNCHECKED);
    }

    public void testParamSuperPassOtherIgnored() {
        configure();
        expectNone();
    }

    public void testAmbiParamSuperPassResolved() {
        configure();
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testParamInfiniteLoopPrevented() {
        configure();
        expect(CUSTOM_UNCHECKED);
    }

    public void testReturnTypeResolved() {
        configure();
        expect(CUSTOM_UNCHECKED);
    }

    public void testConstructorTypeResolved() {
        configure();
        expect(CUSTOM_UNCHECKED);
    }

    public void testReturnTypeLocalResolved() {
        configure();
        expect(CUSTOM_UNCHECKED);
    }

    public void testReturnTypeLocalAmbiResolved() {
        configure();
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testReturnTypeFieldResolved() {
        configure();
        expect(CUSTOM_UNCHECKED);
    }

    public void testReturnTypeFieldAmbiResolved() {
        configure();
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testParamReturnTypeResolved() {
        configure();
        expect(CUSTOM_UNCHECKED);
    }

    public void testParamReturnTypeLocalResolved() {
        configure();
        expect(CUSTOM_UNCHECKED);
    }
    public void testParamReturnTypeLocalAmbiResolved() {
        configure();
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }
}
