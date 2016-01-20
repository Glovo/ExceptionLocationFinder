package exception_finder;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;

public class ParamLocalResolved extends TestBase{
    public void otherTest() {
        SuperInterface superInterface = new OtherThrower();
        test(superInterface);
    }

    public void test(SuperInterface superInterface) {
        <caret>superInterface.throwSuperInterface();
    }
}