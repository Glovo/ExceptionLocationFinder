package resolving;

import base.OtherThrower;
import base.SuperInterface;
import base.TestBase;
import base.Thrower;

public class ReturnTypeLocalAmbiResolved{

    public SuperInterface getThrower(boolean random) {
        SuperInterface superInterface = null;
        if(random)
            superInterface = new Thrower();
        else
            superInterface = new OtherThrower();
        return superInterface;
    }

    public static void main(String[] args) {
        <caret>getThrower().throwSuperInterface();
    }
}