package exception_finder;

import base.TestBase;

public class RepeatedMethodFound extends TestBase {
    public void test()
    {
        <caret>thrower.throwRepeated();
    }
}