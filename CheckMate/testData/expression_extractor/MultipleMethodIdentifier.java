package extractor;

import base.TestBase;

public class MultipleMethodIdentifier extends TestBase {
    public void t<selection>est() {
        thrower.throwCustomUnChecked();
        thrower.throwOther();
    }

    public void te</selection>st2() {
        thrower.throwOther();
    }
}