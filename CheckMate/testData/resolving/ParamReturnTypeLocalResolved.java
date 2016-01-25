package resolving;

import base.Thrower;
import base.SuperInterface;
import base.TestBase;

public class ParamReturnTypeLocalResolved extends TestBase{
    public SuperInterface getThrower() {
        SuperInterface superInterface = new Thrower();
        return superInterface;
    }
    public void otherTest() {
        test(getThrower());
    }

    public void test(SuperInterface superInterface) {
        <caret>superInterface.throwSuperInterface();
    }
}