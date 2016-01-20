package exception_finder;

import base.OtherThrower;
import base.Thrower;
import base.SuperInterface;
import base.TestBase;

public class MultiParamAmbiFieldResolved extends TestBase{
    private SuperInterface superInterface = null;

    public void otherTest(boolean random) {
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