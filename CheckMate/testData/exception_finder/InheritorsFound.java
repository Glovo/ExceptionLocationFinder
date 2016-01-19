package exception_finder;

import base.SuperInterface;
import base.TestBase;

public class InheritorsFound extends TestBase {
    public void test() {
        SuperInterface superInterface = null;
        <caret>superInterface.throwSuperInterface();
    }
}