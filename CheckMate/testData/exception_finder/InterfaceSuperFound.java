package tests;

import base.TestBase;

public class InterfaceSuperFound extends TestBase {
    public void testInterfaceSuperFound() {
        <caret>thrower.throwSuperInterface();
    }
}
