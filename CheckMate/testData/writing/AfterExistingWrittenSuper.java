package writing;

import base.CustomUncheckedException;
import base.TestBase;
import base.other_package.OtherCustomUncheckedException;

public class BeforeExistingWrittenSuper extends TestBase {
    public void test() {
        try {
            thrower.throwRuntime();
        } catch (CustomUncheckedException e) {
            e.printStackTrace();
        } catch (OtherCustomUncheckedException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}