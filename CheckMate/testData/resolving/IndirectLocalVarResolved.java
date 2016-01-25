package resolving;

import base.BridgeSuper;
import base.OtherThrower;
import base.SuperInterface;

public class IndirectLocalVarResolved {
    public void test() {
        BridgeSuper bridgeSuper = new OtherThrower();
        SuperInterface superInterface = bridgeSuper;
        <caret>superInterface.throwSuperInterface();
    }
}
