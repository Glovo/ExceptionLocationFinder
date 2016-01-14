package writing;

import base.CustomUncheckedException;
import base.TestBase;
import base.other_package.OtherCustomUncheckedException;

public class BeforeExceptionHierarchyWritten extends TestBase {
    public void testExceptionHierarchyWritten() {
        try {
            thrower.throwRuntime();
            thrower.throwOther();
            thrower.throwCustomUnChecked();
        } catch (CustomUncheckedException e) {
            e.printStackTrace();
        } catch (OtherCustomUncheckedException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}