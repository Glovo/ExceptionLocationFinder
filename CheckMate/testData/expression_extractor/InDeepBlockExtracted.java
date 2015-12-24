package extractor;

import base.TestBase;

public class InDeepBlockExtracted extends TestBase {
    public void testInDeepBlockExtracted() {
        if(true) {
            if(false) {
                <selection>
            } else {
                if(false) {
                    if(thrower.throwCustomUnChecked()) {
                        thrower.t</selection>hrowOther();
                    }
                }
            }
        }

    }
}
