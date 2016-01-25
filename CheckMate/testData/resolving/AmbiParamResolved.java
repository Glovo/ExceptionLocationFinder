package resolving;

import base.OtherThrower;
import base.Thrower;
import base.SuperInterface;
import base.TestBase;

public class AmbiParamResolved extends TestBase{
    public void otherTest() {
        test(new OtherThrower());
        test(new Thrower());
    }

    public void test(SuperInterface superInterface) {
        <caret>superInterface.throwSuperInterface();
    }
}