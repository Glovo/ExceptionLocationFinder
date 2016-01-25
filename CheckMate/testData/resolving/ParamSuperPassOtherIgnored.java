package resolving;

import base.SuperInterface;
import base.TestBase;
import base.Thrower;

public class ParamSuperPassOtherIgnored extends TestBase {
    public void test(SuperInterface superInterface) {
        <caret>superInterface.throwSuperInterface();
    }
}

interface Passer {
    void pass(SuperInterface superInterface);
}

class PasserImpl implements Passer {
    ParamSuperPassOtherIgnored resolved = new ParamSuperPassOtherIgnored();

    public void pass(SuperInterface superInterface) {
        resolved.test(superInterface);
    }
}

class OtherPasser implements Passer {
    public void pass(SuperInterface superInterface) {

    }
}

class Origin {
    Passer passer = new OtherPasser();
    public void origin() {
        SuperInterface superInterface = new Thrower();
        passer.pass(superInterface);
    }
}