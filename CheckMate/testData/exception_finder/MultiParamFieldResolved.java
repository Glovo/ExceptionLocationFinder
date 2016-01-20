package exception_finder;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;
import base.Thrower;

public class MultiParamFieldResolved extends TestBase{
    private SuperInterface superInterface = new Thrower();
    private SuperInterface otherInterface = new OtherThrower();

    public void otherTest() {
        test(superInterface, otherInterface);
    }

    public void test(SuperInterface superInterface, SuperInterface otherInterface) {
        superInterface.throwSuperInterface();
        <caret>otherInterface.throwSuperInterface();
    }
}