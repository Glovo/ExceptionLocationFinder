package exception_finder;

import base.TestBase;

public class ConstructorFound extends TestBase {
    public void testConstructor() {
        <caret>thrower.construct();
    }
}
