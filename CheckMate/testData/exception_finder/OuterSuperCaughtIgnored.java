package tests;

import base.TestBase;

public class OuterSuperCaughtIgnored extends TestBase {
    public void testOuterSuperCaughtIgnored() {
        try {
            <caret>thrower.throwCustomUnChecked();
        } catch (RuntimeException e0) {
            e0.printStackTrace();
        }
    }
}
