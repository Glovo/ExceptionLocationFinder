package writing;

import base.CustomUncheckedException;
import base.TestBase;

public class BeforeUncompletedExistingRemoved extends TestBase {
    public void test() {
        try {
            <caret>thrower.throwOther()
        } catch (CustomUncheckedException | Exc ) {

        }
    }
}