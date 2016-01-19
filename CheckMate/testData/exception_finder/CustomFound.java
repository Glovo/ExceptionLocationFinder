package exception_finder;

import base.TestBase;

public class CustomFound extends TestBase {
    public void testCustomFound() {
        <caret>thrower.throwCustomUnChecked();
    }
}
