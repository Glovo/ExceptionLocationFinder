package resolving;

import base.SuperInterface;
import base.TestBase;
import base.Thrower;
import base.OtherThrower;

public class AmbiParamSuperPassResolved extends TestBase {
    public void test(SuperInterface superInterface) {
        <caret>superInterface.throwSuperInterface();
    }
}

interface Passer {
    void pass(SuperInterface superInterface);
}

class PasserImpl implements Passer {
    AmbiParamSuperPassResolved resolved = new AmbiParamSuperPassResolved();

    public void pass(SuperInterface superInterface) {
        resolved.test(superInterface);
    }
}

class OtherPasser implements Passer {
    AmbiParamSuperPassResolved resolved = new AmbiParamSuperPassResolved();
    public void pass(SuperInterface superInterface) {
        resolved.test(superInterface);
    }
}

class Origin {
    Passer passer = new PasserImpl();
    public void origin() {
        SuperInterface superInterface = new Thrower();
        passer.pass(superInterface);
    }
}

class OtherOrigin {
    Passer passer = new OtherPasser();
    public void otherOrigin() {
        SuperInterface superInterface = new OtherThrower();
        passer.pass(superInterface);
    }
}
