package writing;

import base.CustomUncheckedException;
import base.TestBase;
import base.other_package.OtherCustomUncheckedException;

public class BeforeExistingWrittenSuper extends TestBase {
    public void test() {
        try {
            <caret>thrower.throwRuntime();
        } catch (OtherCustomUncheckedException | CustomUncheckedException e0) {

        }
    }
}