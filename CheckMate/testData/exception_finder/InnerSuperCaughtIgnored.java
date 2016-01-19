package exception_finder;

import base.TestBase;

public class InnerSuperCaughtIgnored extends TestBase {
    public void testInnerSuperCaughtIgnored() {
        <caret>thrower.superTryCatch();
    }
}
