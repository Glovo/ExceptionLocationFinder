package resolving;

import base.OtherThrower;
import base.Thrower;
import base.SuperInterface;

public class OtherExternalParamResolved {
    private ExternalParamResolved resolved = null;
    public void test() {
        resolved.test(new OtherThrower());
    }
}