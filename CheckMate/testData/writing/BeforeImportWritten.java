package writing;

import base.TestBase;

public class BeforeImportWritten extends TestBase {
    public void testImportWritten() {
        <caret>thrower.throwOther();
    }
}