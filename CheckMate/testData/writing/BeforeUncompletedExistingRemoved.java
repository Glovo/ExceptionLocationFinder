package writing;

import base.TestBase;

public class BeforeUncompletedExistingRemoved extends TestBase {
    public void test() {
        try {
            <caret>thrower.throwCustomUnChecked()
        } catch (Excep) {

        }
    }
}