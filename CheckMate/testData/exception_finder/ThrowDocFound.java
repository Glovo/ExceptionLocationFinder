package exception_finder;

import base.TestBase;

public class ThrowDocFound extends TestBase {
    public void testThrowDocFound() {
        <caret>thrower.commentedMethod();
    }
}
