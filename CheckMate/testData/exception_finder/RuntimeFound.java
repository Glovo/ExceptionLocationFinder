package tests;

import base.TestBase;

public class RuntimeFound extends TestBase {
    public void testRuntime() {
        <caret>thrower.throwRuntime();
    }
}
