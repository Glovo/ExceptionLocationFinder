package tests;

import base.TestBase;

public class InterfaceSuperIgnored extends TestBase {
    public void testInterfaceSuperIgnored() {
        <caret>thrower.throwSuperInterface();
    }
}
