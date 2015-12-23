package tests;

import base.TestBase;

public class BeforeCustomFound extends TestBase {
    public void testCustomFound() {
        <caret>thrower.throwCustomUnChecked();
    }
}
