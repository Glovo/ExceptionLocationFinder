package exception_finder;

import base.CustomUncheckedException;
import base.TestBase;

public class OuterCaughtIgnored extends TestBase {
    public void testOuterCaughtIgnored() {
        try {
            <caret>thrower.throwCustomUnChecked();
        } catch (CustomUncheckedException e0) {
            e0.printStackTrace();
        }
    }
}
