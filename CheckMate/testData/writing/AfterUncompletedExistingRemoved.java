package writing;

import base.CustomUncheckedException;
import base.TestBase;

public class BeforeUncompletedExistingRemoved extends TestBase {
    public void test() {
        try {
            thrower.throwCustomUnChecked()
        } catch (CustomUncheckedException e) {
            e.printStackTrace();
        }
    }
}