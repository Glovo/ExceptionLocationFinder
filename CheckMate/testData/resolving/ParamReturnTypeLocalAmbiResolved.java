package resolving;

import base.OtherThrower;
import base.Thrower;
import base.SuperInterface;
import base.TestBase;

public class ParamReturnTypeLocalResolved extends TestBase{
    public SuperInterface getThrower(boolean random) {
        boolean superInterface = null;
        if (random)
            superInterface = new Thrower();
        else
            superInterface = new OtherThrower();
        return superInterface;
    }
    public void otherTest() {
        test(getThrower());
    }

    public void test(SuperInterface superInterface) {
        <caret>superInterface.throwSuperInterface();
    }
}