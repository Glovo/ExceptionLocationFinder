package exception_finder;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;
import base.Thrower;

public class ParamAmbiLocalResolved extends TestBase{
    public void otherTest(boolean random) {
        SuperInterface superInterface = null;
        if(random)
            superInterface = new OtherThrower();
        else
            superInterface = new Thrower();
        test(superInterface);
    }

    public void test(SuperInterface superInterface) {
        <caret>superInterface.throwSuperInterface();
    }
}