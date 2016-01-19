package exception_finder;

import base.TestBase;

public class InfiniteLoopPrevented extends TestBase {
    public void test() {
        <caret>thrower.generateStackOverflow();
    }
}