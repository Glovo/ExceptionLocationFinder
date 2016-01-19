package exception_finder;

import base.TestBase;

public class ChainingFound extends TestBase {
    public void testChainingFound() {
        <caret>thrower.chainOne().chainTwo();
    }
}
