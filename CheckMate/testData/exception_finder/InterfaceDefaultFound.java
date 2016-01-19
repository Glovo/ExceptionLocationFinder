package exception_finder;

import base.TestBase;

public class InterfaceDefaultFound extends TestBase {
    public void testInterfaceDefaultFound() {
        <caret>thrower.usedDefault();
    }
}
