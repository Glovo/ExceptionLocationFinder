package exception_finder;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;
import base.Thrower;

public class AmbiFieldInheritorsResolved extends TestBase {
    private SuperInterface superInterface = new OtherThrower();

    public void test(boolean random) {
        if(random) {
            superInterface = new Thrower();
        }
        <caret>superInterface.throwSuperInterface();
    }
}