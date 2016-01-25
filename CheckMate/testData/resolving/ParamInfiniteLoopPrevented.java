package resolving;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;
import base.Thrower;

public class ParamInfiniteLoopPrevented extends TestBase{
    public void test(SuperInterface superInterface) {
        superInterface = new Thrower();
        superInterface.throwSuperInterface();
    }

    public static void main(String[] args) {
        SuperInterface superInterface = new OtherThrower();
        <caret>new ParamInfiniteLoopPrevented().test(superInterface);
    }
}