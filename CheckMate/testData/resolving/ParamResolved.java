package resolving;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;

public class ParamResolved extends TestBase{
    public void otherTest() {
        test(new OtherThrower());
    }

    public void test(SuperInterface superInterface) {
        <caret>superInterface.throwSuperInterface();
    }
}