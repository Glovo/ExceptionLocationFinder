package resolving;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;
import base.Thrower;

public class ParamAmbiPassResolved extends TestBase{
    public void test(SuperInterface superInterface) {
        <caret>superInterface.throwSuperInterface();
    }
}

class Passer {
    ParamAmbiPassResolved resolved = new ParamAmbiPassResolved();
    public void pass(SuperInterface superInterface) {
        resolved.test(superInterface);
    }
}

class OtherPasser {
    ParamAmbiPassResolved resolved = new ParamAmbiPassResolved();
    public void otherPass(SuperInterface superInterface) {
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

class OtherOrigin {
    OtherPasser passer = new OtherPasser();
    public void otherOrigin() {
        SuperInterface superInterface = new Thrower();
        passer.otherPass(superInterface);
    }
}