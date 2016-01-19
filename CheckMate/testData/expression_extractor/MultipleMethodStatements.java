package extractor;

import base.TestBase;

public class MultipleMethodIdentifier extends TestBase {
    public void test() {
        thrower.throw<selection>CustomUnChecked();
        thrower.throwOther();
    }

    public void test2() {
        thrower.thr</selection>owOther();
    }
}