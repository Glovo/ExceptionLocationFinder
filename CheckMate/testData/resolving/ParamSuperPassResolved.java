package resolving;

import base.SuperInterface;
import base.TestBase;
import base.Thrower;

public class ParamSuperPassResolved extends TestBase {
    public void test(SuperInterface superInterface) {
        <caret>superInterface.throwSuperInterface();
    }
}

interface Passer {
    void pass(SuperInterface superInterface);
}

class PasserImpl implements Passer {
    ParamSuperPassResolved resolved = new ParamSuperPassResolved();

    public void pass(SuperInterface superInterface) {
        resolved.test(superInterface);
    }
}

class OtherPasser implements Passer {
    public void pass(SuperInterface superInterface) {
        //do nothing
    }
}

class Origin {
    Passer passer = new PasserImpl();
    public void origin() {
        SuperInterface superInterface = new Thrower();
        passer.pass(superInterface);
    }
}
