package resolving;

import base.Thrower;
import base.SuperInterface;
import base.TestBase;

public class ParamReturnTypeResolved extends TestBase{
    public SuperInterface getThrower() {
        return new Thrower();
    }
    public void otherTest() {
        test(getThrower());
    }

    public void test(SuperInterface superInterface) {
        <caret>superInterface.throwSuperInterface();
    }
}