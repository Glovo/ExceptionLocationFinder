package writing;

import base.CustomUncheckedException;
import base.TestBase;

public class BeforeCustomWritten extends TestBase {
    public void testCustomWritten() {
        try {
            thrower.throwCustomUnChecked();
        } catch (CustomUncheckedException e) {
            e.printStackTrace();
        }
    }
}