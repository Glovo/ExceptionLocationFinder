package resolving;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;

public class LocalVarInheritorsResolved extends TestBase{
    public void test() {
        SuperInterface superInterface = new OtherThrower();
        <caret>superInterface.throwSuperInterface();
    }
}
