package exception_finder;

import base.TestBase;

public class SuperFound extends TestBase {
    public void testSuperFound() {
        <caret>thrower.superThrow();
    }
}
