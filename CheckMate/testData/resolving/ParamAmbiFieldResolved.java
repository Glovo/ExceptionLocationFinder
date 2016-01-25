package resolving;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;
import base.Thrower;

public class ParamAmbiFieldResolved extends TestBase{
    private SuperInterface superInterface = null;

    public void otherTest(boolean random) {
        if(random)
            superInterface = new Thrower();
        else
            superInterface = new OtherThrower();
        test(superInterface);
    }

    public void test(SuperInterface superInterface) {
        <caret>superInterface.throwSuperInterface();
    }
}