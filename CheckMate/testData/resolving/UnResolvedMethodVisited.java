package resolving;

import base.SuperThrower;

public class UnResolvedMethodVisited {

    public void test(SuperInterface superInterface) {
        SuperThrower superThrower = new SuperThrower();
        <caret>superThrower.superThrow();
    }
}