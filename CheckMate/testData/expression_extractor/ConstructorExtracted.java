package extractor;

import base.TestBase;

public class ConstructorExtracted extends TestBase {
    public void testConstructorExtracted() {
        <caret>new Thrower();
    }
}
