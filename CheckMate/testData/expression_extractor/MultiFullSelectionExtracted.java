package extractor;

import base.TestBase;

public class MultiFullSelectionExtracted extends TestBase {
    public void testMultiFullSelectionExtracted() {
        <selection>thrower.throwCustomUnChecked();
        thrower.throwOther();</selection>
    }
}
