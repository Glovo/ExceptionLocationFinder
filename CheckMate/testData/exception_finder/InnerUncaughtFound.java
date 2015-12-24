package tests;

import base.TestBase;

public class InnerUncaughtFound extends TestBase {
    public void testInnerUncaughtFound() {
        <caret>thrower.uncaughtTryCatch();
    }
}
