package base;

import base.TestBase;

public class SuperThrower extends TestBase {
    public void superThrow() {
        throw new CustomUncheckedException("SuperThrow");
    }

    public void throwRuntime() {
        //overridden by thrower
    }

    public void throwCustomUnChecked() {
        //overridden by thrower
    }
}
