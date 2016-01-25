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
        configure("LocalVarInheritorsResolved.java");
        expect(OTHER_UNCHECKED);
    }

    public void testIndirectLocalVarResolved() {
        configure("IndirectLocalVarResolved.java");
        expect(OTHER_UNCHECKED);
    }

    public void testAmbiLocalVarInheritorsResolved() {
        configure("AmbiLocalVarResolved.java");
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testFieldInheritorsResolved() {
        configure("FieldInheritorsResolved.java");
        expect(OTHER_UNCHECKED);
    }

    public void testAmbiFieldInheritorsResolved() {
        configure("AmbiFieldInheritorsResolved.java");
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testParamResolved() {
        configure("ParamResolved.java");
        expect(OTHER_UNCHECKED);
    }

    public void testMultiParamResolved() {
        configure("MultiParamResolved.java");
        expect(OTHER_UNCHECKED);
    }

    public void testAmbiParamResolved() {
        configure("AmbiParamResolved.java");
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testParamLocalResolved() {
        configure("ParamLocalResolved.java");
        expect(OTHER_UNCHECKED);
    }

    public void testParamAmbiLocalResolved() {
        configure("ParamAmbiLocalResolved.java");
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testMultiParamLocalResolved() {
        configure("MultiParamLocalResolved.java");
        expect(OTHER_UNCHECKED);
    }

    public void testMultiParamAmbiLocalResolved() {
        configure("MultiParamAmbiLocalResolved.java");
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }


    public void testParamFieldResolved() {
        configure("ParamFieldResolved.java");
        expect(OTHER_UNCHECKED);
    }

    public void testParamAmbiFieldResolved() {
        configure("ParamAmbiFieldResolved.java");
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testMultiParamFieldResolved() {
        configure("MultiParamFieldResolved.java");
        expect(OTHER_UNCHECKED);
    }

    public void testMultiParamAmbiFieldResolved() {
        configure("MultiParamAmbiFieldResolved.java");
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testExternalParamResolved() {
        configure("OtherExternalParamResolved.java", "ExternalParamResolved.java");
        expect(OTHER_UNCHECKED);
    }

    public void testParamPassResolved() {
        configure("ParamPassResolved.java");
        expect(OTHER_UNCHECKED);
    }

    public void testParamAmbiPassResolved() {
        configure("ParamAmbiPassResolved.java");
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testParamSuperPassResolved() {
        configure("ParamSuperPassResolved.java");
        expect(CUSTOM_UNCHECKED);
    }

    public void testParamSuperPassOtherIgnored() {
        configure("ParamSuperPassOtherIgnored.java");
        expectNone();
    }

    public void testAmbiParamSuperPassResolved() {
        configure("AmbiParamSuperPassResolved.java");
        expect(CUSTOM_UNCHECKED, OTHER_UNCHECKED);
    }

    public void testParamInfiniteLoopPrevented() {
        configure("ParamInfiniteLoopPrevented.java");
        expect(CUSTOM_UNCHECKED);
    }
}
