package exception_finder;

import base.OtherThrower;
import base.Thrower;
import base.SuperInterface;
import base.TestBase;

public class MultiParamLocalResolved extends TestBase{
    public void otherTest() {
        SuperInterface superInterface = new OtherThrower();
        OtherInterface otherInterface = new Thrower();
        test(superInterface, otherInterface);
    }

    public void test(SuperInterface superInterface, SuperInterface otherInterface) {
        <caret>superInterface.throwSuperInterface();
        otherInterface.throwSuperInterface();
    }
}