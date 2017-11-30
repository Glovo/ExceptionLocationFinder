package writing;

import base.CustomUncheckedException;
import base.TestBase;
import base.other_package.OtherCustomUncheckedException;

public class BeforeUncompletedExistingRemoved extends TestBase {
    public void test() {
        try {
            thrower.throwOther()
        } catch (OtherCustomUncheckedException e) {
            e.printStackTrace();
        }
    }
}