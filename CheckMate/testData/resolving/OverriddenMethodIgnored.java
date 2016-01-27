package resolving;

import base.TestBase;

public class OverriddenMethodIgnored extends TestBase{
    public void test() {
        <caret>superThrower.superThrow();
    }
}