package exception_finder;

import base.TestBase;

public class OverriddenIgnored extends TestBase {
    public void test()
    {
        <caret>thrower.ignoredDefault();
    }
}
