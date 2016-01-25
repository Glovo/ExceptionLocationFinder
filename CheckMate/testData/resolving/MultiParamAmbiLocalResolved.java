package resolving;

import base.OtherThrower;
import base.Thrower;
import base.SuperInterface;
import base.TestBase;

public class MultiParamAmbiLocalResolved extends TestBase{
    public void otherTest(boolean random) {
        SuperInterface superInterface = null;
        if(random)
            superInterface = new OtherThrower();
        else
            superInterface = new Thrower();
        OtherInterface otherInterface = new Thrower();
        test(superInterface, otherInterface);
    }

    public void test(SuperInterface superInterface, SuperInterface otherInterface) {
        <caret>superInterface.throwSuperInterface();
        otherInterface.throwSuperInterface();
    }
}