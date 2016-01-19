package exception_finder;

import base.TestBase;

public class InnerCaughtIgnored extends TestBase {
    public void testInnerCaughtIgnored() {
        <caret>thrower.tryCatch();
    }
}
