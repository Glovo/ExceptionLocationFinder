package writing;

import base.TestBase;

public class BeforeRuntimeWritten extends TestBase {
    public void testRuntimeWritten() {
        <caret>thrower.throwRuntime();
    }
}