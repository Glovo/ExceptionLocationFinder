package resolving;

import base.SuperInterface;
import base.TestBase;
import base.Thrower;

import java.lang.Override;

public class LocalVarInheritorsResolved extends TestBase {
    public void test() {
        SuperType superType = new ImplementationType();
        SuperInterface superInterface = superType.getSuperInterface();
        <caret>superInterface.throwSuperInterface();
    }
}

interface SuperType {
    SuperInterface getSuperInterface();
}

class ImplementationType implements SuperType {
    @Override
    public SuperInterface getSuperInterface() {
        return new Thrower();
    }
}