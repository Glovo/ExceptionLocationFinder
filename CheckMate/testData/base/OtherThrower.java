package base;

import base.other_package.OtherCustomUncheckedException;

public class OtherThrower extends BridgeSuper implements SuperInterface {
    @Override
    public void throwSuperInterface() {
        throw new OtherCustomUncheckedException("hehe");
    }
}
