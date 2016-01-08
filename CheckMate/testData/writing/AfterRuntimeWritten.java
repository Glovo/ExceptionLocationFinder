package writing;

import base.TestBase;

public class BeforeRuntimeWritten extends TestBase {
    public void testRuntimeWritten() {
        try {
            thrower.throwRuntime();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}