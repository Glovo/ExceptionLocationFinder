package resolving;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;

public class FieldInheritorsResolved extends TestBase {
    private SuperInterface superInterface = new OtherThrower();

    public void test() {
        <caret>superInterface.throwSuperInterface();
    }
}