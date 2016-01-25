package resolving;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;
import base.Thrower;

public class ReturnTypeResolved{

    public SuperInterface getThrower() {
        return new Thrower();
    }

    public static void main(String[] args) {
        <caret>getThrower().throwSuperInterface();
    }
}