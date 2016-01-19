package exception_finder;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;
import base.Thrower;

public class MultipleLocalVarInheritorsResolved extends TestBase {
    public void test(boolean random) {
        SuperInterface superInterface = null;
        if (random)
            superInterface = new OtherThrower();
        else {
            superInterface = new Thrower();
        }
        <caret>superInterface.throwSuperInterface();
    }
}
