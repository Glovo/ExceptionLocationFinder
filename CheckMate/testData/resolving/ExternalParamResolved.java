package resolving;

import base.OtherThrower;
import base.Thrower;
import base.SuperInterface;
import base.TestBase;

public class ExternalParamResolved extends TestBase{
    public void test(SuperInterface superInterface) {
        <caret>superInterface.throwSuperInterface();
    }
}