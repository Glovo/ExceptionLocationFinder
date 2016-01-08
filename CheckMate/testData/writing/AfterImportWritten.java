package writing;

import base.TestBase;
import base.other_package.OtherCustomUncheckedException;

public class BeforeImportWritten extends TestBase {
    public void testImportWritten() {
        try {
            thrower.throwOther();
        } catch (OtherCustomUncheckedException e) {
            e.printStackTrace();
        }
    }
}