package tests;

import base.CustomUncheckedException;
import base.TestBase;

public class OuterUncaughtFound extends TestBase {
    public void testOuterUncaughtFound() {
        try {
            <selection>thrower.throwCustomUnChecked();
            thrower.throwOther();</selection>
        } catch (CustomUncheckedException e0) {
            e0.printStackTrace();
        }
    }
}
