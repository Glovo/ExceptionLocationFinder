package tests;

import base.CustomUncheckedException;
import base.TestBase;

public class InnerToOuterUncaughtFound extends TestBase {
    public void testInnerToOuterUncaughtFound() {
        try {
            <caret>thrower.throwBoth();
        } catch (CustomUncheckedException e0) {
            e0.printStackTrace();
        }
    }
}
