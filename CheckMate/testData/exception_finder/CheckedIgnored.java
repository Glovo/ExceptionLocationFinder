package exception_finder;

import base.CustomCheckedException;
import base.TestBase;

public class CheckedIgnored extends TestBase {
    public void testCheckedIgnored() throws CustomCheckedException {
        <caret>thrower.throwCustomChecked();
    }
}
