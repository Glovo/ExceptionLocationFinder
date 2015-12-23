package tests;

import base.CustomUncheckedException;
import base.TestBase;

public class AfterCustomFound extends TestBase {
    public void testCustomFound() {
        try {
            thrower.throwCustomUnChecked();
        } catch (CustomUncheckedException e0) {
            e0.printStackTrace();
        }
    }
}
