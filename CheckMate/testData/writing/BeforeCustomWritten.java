package writing;

import base.TestBase;

public class BeforeCustomWritten extends TestBase {
    public void testCustomWritten() {
        <caret>thrower.throwCustomUnChecked();
    }
}