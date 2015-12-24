package extractor;

import base.TestBase;

public class InAssignmentExtracted extends TestBase {
    public void testInAssignmentExtracted() {
        int as<caret>signee = thrower.throwCustomUnChecked();
    }
}
