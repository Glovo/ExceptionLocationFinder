package resolving;

import base.OtherThrower;
import base.Thrower;
import base.SuperInterface;
import base.TestBase;

public class MultiParamResolved extends TestBase{
    public void otherTest() {
        test(new Thrower, new OtherThrower());
    }

    public void test(SuperInterface superInterface, SuperInterface otherInterface) {
        superInterface.throwSuperInterface();
        <caret>otherInterface.throwSuperInterface();
    }
}