package extractor;

import base.TestBase;

public class MethodExtracted extends TestBase {
    public void t<caret>est() {
        thrower.throwCustomUnChecked();
        thrower.throwOther();
    }
}
