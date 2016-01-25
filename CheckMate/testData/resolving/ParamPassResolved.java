package resolving;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;

public class ParamPassResolved extends TestBase{
    public void test(SuperInterface superInterface) {
        <caret>superInterface.throwSuperInterface();
    }
}

class Passer {
    ParamPassResolved resolved = new ParamPassResolved();
    public void pass(SuperInterface superInterface) {
        resolved.test(superInterface);
    }
}

class Origin {
    Passer passer = new Passer();
    public void origin() {
        SuperInterface superInterface = new OtherThrower();
        passer.pass(superInterface);
    }
}