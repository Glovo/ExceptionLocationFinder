package resolving;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;
import base.Thrower;

public class ReturnTypeLocalResolved{

    public SuperInterface getThrower() {
        SuperInterface superInterface = new Thrower();
        return superInterface;
    }

    public static void main(String[] args) {
        <caret>getThrower().throwSuperInterface();
    }
}