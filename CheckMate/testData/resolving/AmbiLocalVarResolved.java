package resolving;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;
import base.Thrower;

public class AmbiLocalVarResolved extends TestBase {
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
