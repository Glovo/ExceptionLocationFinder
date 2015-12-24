package extractor;

import base.TestBase;

public class MultiPartialSelectionExtracted extends TestBase {
    public void testMultiPartialSelectionExtracted() {
        thrower.t<selection>hrowCustomUnChecked();
        thrower.t</selection>hrowOther();
    }
}
