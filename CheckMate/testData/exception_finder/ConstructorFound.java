package tests;

import base.TestBase;
import base.Thrower;

public class ConstructorFound extends TestBase {
    public void testConstructor() {
        <caret>thrower.construct();
    }
}
