package exception_finder;

import base.*;

public class LocalVarInheritorsResolved extends TestBase{
    public void test() {
        SuperInterface superInterface = new OtherThrower();
        <caret>superInterface.throwSuperInterface();
    }
}
