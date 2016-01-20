package exception_finder;

import base.OtherThrower;
import base.Thrower;
import base.SuperInterface;
import base.TestBase;

public class ParamResolved extends TestBase{
    public void otherTest() {
        test(new OtherThrower());
        test(new Thrower());
    }

    public void test(SuperInterface superInterface) {
        <caret>superInterface.throwSuperInterface();
    }
}