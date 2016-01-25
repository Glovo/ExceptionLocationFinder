package resolving;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;

public class ParamFieldResolved extends TestBase{
    private SuperInterface superInterface = new OtherThrower();

    public void otherTest() {
        test(superInterface);
    }

    public void test(SuperInterface superInterface) {
        <caret>superInterface.throwSuperInterface();
    }
}